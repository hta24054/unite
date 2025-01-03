package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Board;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.dto.BoardDTO;
import com.hta2405.unite.dto.BoardHomeDeptDTO;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardPostMapper {
    List<BoardPostEmpDTO> getBoardListAll(Long deptId);

    List<BoardHomeDeptDTO> getBoardListByDeptId(Long deptId);

    int insertPost(Post post);

    int insertPostFile(Long postId, List<FileDTO> list);

    int refUpdate(Long postId);

    Long findBoardIdByName1Name2(BoardDTO boardDTO);

    int getListCountByBoardId(Long boardId);

    List<Post> getPostListByBoardId(int startRow, int endRow, Long boardId);
}
