package com.hta2405.unite.service;

import com.hta2405.unite.domain.*;
import com.hta2405.unite.dto.ProductDTO;
import com.hta2405.unite.mybatis.mapper.DocMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DocService {
    private final EmpService empService;
    private final DeptService deptService;
    private final AttendService attendService;
    private final HolidayService holidayService;
    private final DocMapper docMapper;

    public int countVacation(LocalDate startDate, LocalDate endDate) {
        int allCount = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int holidayCount = holidayService.getHolidayList(startDate, endDate).size();
        return allCount - holidayCount;
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
