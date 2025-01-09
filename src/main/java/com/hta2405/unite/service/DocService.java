package com.hta2405.unite.service;

import com.hta2405.unite.domain.*;
import com.hta2405.unite.dto.DocInProgressDTO;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProductDTO;
import com.hta2405.unite.enums.DocRole;
import com.hta2405.unite.mybatis.mapper.DocMapper;
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

import static com.hta2405.unite.domain.DocVacation.DocVacationBuilder;

@Service
public class DocService {
    private final HolidayService holidayService;
    private final EmpService empService;
    private final DeptService deptService;
    private final DocMapper docMapper;
    private final FileService fileService;
    private final String VACATION_FILE_DIR;

    public DocService(HolidayService holidayService,
                      EmpService empService,
                      DeptService deptService,
                      DocMapper docMapper,
                      FileService fileService,
                      @Value("${vacation.upload.directory}") String VACATION_FILE_DIR) {
        this.holidayService = holidayService;
        this.empService = empService;
        this.deptService = deptService;
        this.docMapper = docMapper;

        this.VACATION_FILE_DIR = VACATION_FILE_DIR;
        this.fileService = fileService;
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
                                DocVacationBuilder docVacationBuilder,
                                List<String> signers,
                                List<MultipartFile> files) {
        docMapper.insertGeneralDoc(doc);

        if (files != null && !files.isEmpty()) {
            //어차피 일단 정책상 아직 첨부파일 최대 1개
            FileDTO fileDTO = fileService.uploadFile(files.get(0), VACATION_FILE_DIR);
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

    public List<DocInProgressDTO> getInProgressDTO(String empId) {
        return docMapper.getInProgressDTO(empId);
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
}
