package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocMapper {
    void insertGeneralDoc(Doc doc);

    void insertSign(List<Sign> list);

    void insertTripDoc(Long docId, DocTrip docTrip);

    void insertBuyDoc(DocBuy docBuy);

    void insertProducts(Long docBuyId, List<BuyItem> items);

    void insertVacationDoc(Long docId, DocVacation docVacation);
}
