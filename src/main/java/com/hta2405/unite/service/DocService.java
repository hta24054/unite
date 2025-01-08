package com.hta2405.unite.service;

import com.hta2405.unite.domain.*;
import com.hta2405.unite.dto.FileDTO;
import com.hta2405.unite.dto.ProductDTO;
import com.hta2405.unite.mybatis.mapper.DocMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class DocService {
    private final HolidayService holidayService;
    private final DocMapper docMapper;
    private final String VACATION_FILE_DIR;
    private final FileService fileService;

    public DocService(HolidayService holidayService,
                      DocMapper docMapper,
                      @Value("${vacation.upload.directory}") String VACATION_FILE_DIR, FileService fileService) {
        this.holidayService = holidayService;
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
    public void saveVacationDoc(Doc doc, DocVacation.DocVacationBuilder docVacationBuilder, List<String> signers, List<MultipartFile> files) {
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
}
