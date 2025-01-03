package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardPostMapper {
    List<BoardPostEmpDTO> getBoardListAll(Long deptId);

    List<BoardHomeDeptDTO> getBoardListByDeptId(Long deptId);

    int insertPost(PostDTO postDTO);

    int insertPostFile(List<PostFileDTO> postFileDTOList, boolean postIdCheck);

    Long findBoardIdByName1Name2(BoardDTO boardDTO);

}
