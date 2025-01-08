package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Doc;
import com.hta2405.unite.domain.Sign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DocMapper {
    int insertGeneralDoc(Doc doc);

    int insertSign(List<Sign> list);
}
