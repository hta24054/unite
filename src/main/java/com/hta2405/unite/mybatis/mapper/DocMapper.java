package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.DocTrip;
import com.hta2405.unite.domain.Sign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocMapper {
    void insertGeneralDoc(Doc doc);

    void insertSign(List<Sign> list);

    void insertTripDoc(Long docId, DocTrip docTrip);
}
