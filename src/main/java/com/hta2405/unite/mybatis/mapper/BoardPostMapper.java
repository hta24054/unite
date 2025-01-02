package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.dto.BoardPostEmpDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface BoardPostMapper {
    Optional<BoardPostEmpDTO> getBoardListAll(Long deptId);

}
