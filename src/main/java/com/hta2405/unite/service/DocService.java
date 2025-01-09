package com.hta2405.unite.service;

import com.hta2405.unite.domain.*;
import com.hta2405.unite.dto.DocListDTO;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProductDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.enums.DocType;
import com.hta2405.unite.mybatis.mapper.DocMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;


@Service
public class DocService {
    private final HolidayService holidayService;
    private final EmpService empService;
    private final DeptService deptService;
    private final DocMapper docMapper;
    private final FileService fileService;
    private final String FILE_DIR;
    private final AttendService attendService;

    public DocService(HolidayService holidayService,
                      EmpService empService,
                      DeptService deptService,
                      DocMapper docMapper,
                      FileService fileService,
                      AttendService attendService,
                      @Value("${doc.upload.directory}") String FILE_DIR) {
        this.holidayService = holidayService;
        this.empService = empService;
        this.deptService = deptService;
        this.docMapper = docMapper;
        this.fileService = fileService;
        this.attendService = attendService;
        this.FILE_DIR = FILE_DIR;
    }

    @Transactional
    public void saveGeneralDoc(Doc doc, List<String> signers) {
        docMapper.insertGeneralDoc(doc);

        List<Sign> list = getSigns(doc, signers);
        docMapper.insertSign(list);
    }

    @Transactional
    public void saveTripDoc(Doc doc, DocTrip docTrip, List<String> signers) {
        docMapper.insertGeneralDoc(doc);
        docMapper.insertTripDoc(doc.getDocId(), docTrip);

        List<Sign> list = getSigns(doc, signers);
        docMapper.insertSign(list);
    }

    @Transactional
    public void saveBuyDoc(Doc doc, List<String> signers, List<ProductDTO> products) {
        docMapper.insertGeneralDoc(doc);
        DocBuy docBuy = DocBuy.builder().docId(doc.getDocId()).build();
        docMapper.insertBuyDoc(docBuy);

        List<BuyItem> buyItems = products.stream()
                .map(product -> BuyItem.builder()
                        .docBuyId(docBuy.getDocBuyId())
                        .productName(product.getProductName())
                        .standard(product.getStandard())
                        .quantity(product.getQuantity())
                        .price(product.getPrice())
                        .build())
                .toList();
        docMapper.insertProducts(docBuy.getDocBuyId(), buyItems);

        List<Sign> list = getSigns(doc, signers);
        docMapper.insertSign(list);
    }

    @Transactional
    public void saveVacationDoc(Doc doc,
                                DocVacation.DocVacationBuilder docVacationBuilder,
                                List<String> signers,
                                List<MultipartFile> files) {
        docMapper.insertGeneralDoc(doc);

        if (files != null && !files.isEmpty()) {
            //어차피 일단 정책상 아직 첨부파일 최대 1개
            FileDTO fileDTO = fileService.uploadFile(files.get(0), FILE_DIR);
            docVacationBuilder.vacationFilePath(fileDTO.getFilePath())
                    .vacationFileOriginal(fileDTO.getFileOriginal())
                    .vacationFileUUID(fileDTO.getFileUUID())
                    .vacationFileType(fileDTO.getFileType());
        }
        DocVacation docVacation = docVacationBuilder.build();

        docMapper.insertVacationDoc(doc.getDocId(), docVacation);

        List<Sign> list = getSigns(doc, signers);
        docMapper.insertSign(list);
    }

    public int countVacation(LocalDate startDate, LocalDate endDate) {
        int allCount = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int holidayCount = holidayService.getHolidayList(startDate, endDate).size();
        return allCount - holidayCount;
    }

    private static List<Sign> getSigns(Doc doc, List<String> signers) {
        return IntStream.range(0, signers.size())
                .mapToObj(i -> Sign.builder()
                        .empId(signers.get(i))
                        .docId(doc.getDocId())
                        .signOrder(i)
                        .signTime(i == 0 ? LocalDateTime.now() : null) // 첫 번째 항목(기안자) 결재 완료
                        .build())
                .toList();
    }

    public List<DocListDTO> getInProgressDTO(String empId) {
        return docMapper.getInProgressDocsByEmpId(empId);
    }

    /*
     * <문서 조회 권한 확인 메서드>
     * [전체 경우의 수]
     * 0. 권한 없는 사람(결재선 포함 안되며, 같은 부서원이 아닌 사람) - INVALID
     * 1. 문서 결재 전
     *      1-1. 기안자
     *          1-1-1. 결재전 - PRE_SIGNED_WRITER
     *          1-1-2. 결재후 - POST_SIGNED_WRITER
     *      1-2. 결재자
     *          1-2-1. 결재 전 - PRE_SIGNER
     *          1-2-2. 결재 후 - POST_SIGNER
     *      1-3. 같은 부서원 - VIEWER
     * 2. 결재 완료 - VIEWER
     */
    public DocRole checkRole(String loginEmpId, Long docId) {
        Doc doc = docMapper.getDocById(docId);
        Emp writer = empService.getEmpById(doc.getDocWriter());
        Emp loginEmp = empService.getEmpById(loginEmpId);
        List<Sign> signList = docMapper.getSignListByDocId(docId);

        // 0. 기본 검증
        if (writer == null || loginEmp == null || signList == null) {
            return DocRole.INVALID;
        }

        // 0. 권한 없는 사람 - INVALID
        if (signList.stream().noneMatch(sign -> sign.getEmpId().equals(loginEmpId))
                && !Objects.equals(writer.getDeptId(), loginEmp.getDeptId())) {
            return DocRole.INVALID;
        }

        // 2. 결재 완료 시 - VIEWER
        if (doc.isSignFinish()) {
            return DocRole.VIEWER;
        }

        // 1-1. 기안자
        if (writer.getEmpId().equals(loginEmpId)) {
            return isSignedByEmp(signList, writer.getEmpId())
                    ? DocRole.POST_SIGNED_WRITER : DocRole.PRE_SIGNED_WRITER;
        }

        // 1-2. 결재자
        if (signList.stream().anyMatch(sign -> sign.getEmpId().equals(loginEmpId))) {
            return isSignedByEmp(signList, loginEmpId)
                    ? DocRole.POST_SIGNER : DocRole.PRE_SIGNER;
        }

        // 1-3. 같은 부서 조회자
        return DocRole.VIEWER;
    }

    public void addCommonReadAttrToModel(Doc doc, DocRole docRole, Model model) {
        model.addAttribute("doc", doc);
        model.addAttribute("role", docRole.getType());
        model.addAttribute("writer", empService.getEmpById(doc.getDocWriter()));
        model.addAttribute("dept", deptService.getDeptByEmpId(doc.getDocWriter()));
        model.addAttribute("model", doc);
        model.addAttribute("signList", docMapper.getSignListByDocId(doc.getDocId()));
        model.addAttribute("nameMap", empService.getIdToENameMap());
    }

    private boolean isSignedByEmp(List<Sign> signList, String empId) {
        return signList.stream().anyMatch(sign -> sign.getEmpId().equals(empId) && sign.getSignTime() != null);
    }

    public List<BuyItem> getBuyItemListByDocId(Long docId) {
        return docMapper.getBuyItemListByDocId(docId);
    }

    public Doc getDocById(Long docId) {
        return docMapper.getDocById(docId);
    }

    public DocTrip getDocTripByDocId(Long docId) {
        return docMapper.getDocTripByDocId(docId);
    }

    public DocVacation getDocVacationByDocId(Long docId) {
        return docMapper.getDocVacationByDocId(docId);
    }


    public void downloadFile(String fileUUID, String fileName, HttpServletResponse response) {
        fileService.downloadFile(FILE_DIR, fileUUID, fileName, response);
    }

    public List<DocListDTO> getWaitingDocs(String empId) {
        return docMapper.getWaitingDocsByEmpId(empId);
    }

    @Transactional
    public boolean signDoc(Long docId, String empId) {
        // 현재 결재자인지 확인
        if (!docMapper.getNowSigner(docId).equals(empId)) {
            return false;
        }

        // 결재 수행
        if (docMapper.signDoc(docId, empId) != 1) {
            return false;
        }

        // 결재 완료 처리
        if (docMapper.checkSignFinished(docId)) {
            docMapper.setSignFinished(docId);
            Doc doc = docMapper.getDocById(docId);

            if (doc.getDocType() == DocType.VACATION) {
                attendService.insertVacation(doc.getDocWriter(), docMapper.getDocVacationByDocId(docId));
            }

            if (doc.getDocType() == DocType.TRIP) {
                attendService.insertTrip(doc.getDocWriter(), docMapper.getDocTripByDocId(docId));
            }
        }

        return true;
    }

    @Transactional
    public boolean revokeDoc(Long docId, String empId) {
        //결재한적이 없거나, 결재가 이미 끝난 문서면 false
        if (!docMapper.isDocSignedByEmpId(docId, empId) || docMapper.getDocById(docId).isSignFinish()) {
            return false;
        }
        return docMapper.revokeDoc(docId, empId) > 0;
    }

    @Transactional
    public boolean deleteDoc(Long docId, String empId) {
        Doc doc = docMapper.getDocById(docId);
        /*
            작성자가 아니거나, 현재 로그인 한 사람의 결재순번이 아니거나, 문서가 결재가 완료되었으면 fail
            즉, 로그인한 사람이 작성자면서 + 그 사람이 결재순번인 경우 + 문서결재가 완료되지 않은 경우만 success
         */
        if (!doc.getDocWriter().equals(empId)
                || !docMapper.getNowSigner(docId).equals(empId)
                || doc.isSignFinish()) {
            return false;
        }
        return docMapper.deleteDoc(docId) == 1;
    }
}
