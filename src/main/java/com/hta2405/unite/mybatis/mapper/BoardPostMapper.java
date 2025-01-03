package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.dto.BoardDTO;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.dto.PostDTO;
import com.hta2405.unite.dto.PostFileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardPostMapper {
    List<BoardPostEmpDTO> getBoardListAll(Long deptId);

    int insertPost(PostDTO postDTO);

    int insertPostFile(List<PostFileDTO> postFileDTOList, boolean postIdCheck);

    Long findBoardIdByName1Name2(BoardDTO boardDTO);
}
