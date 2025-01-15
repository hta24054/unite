package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Board;
import com.hta2405.unite.domain.Post;
import com.hta2405.unite.domain.PostFile;
import com.hta2405.unite.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
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

    int getSearchListCountByBoardId(Long boardId, String category, String query);

    List<Post> getSearchListByBoardId(int startRow, int endRow, Long boardId, String category, String query);

    void setReadCountUpdate(Long postId);

    //HashMap<String,Object> getDetail(Long postId);
    PostDetailDTO getDetail(Long postId);

    PostFile getPostFile(Long postFileId);

    List<PostFile> getDetailPostFile(Long postId);

    int updatePost(Post post);

    int deletePostFile(List<String> deletePostFileUUIDList);

    HashMap<String, Object> selectPostInfo(@Param("postId") Long postId);

    int deletePost(@Param("postReRef") int postReRef,
                   @Param("postReLev") int postReLev,
                   @Param("postReSeq") int postReSeq);

    void updateReplySeq(Long postReRef, Long postReSeq);

    int replyPostInsert(Post postData);

    Board findBoardByBoardId(Long boardId);

    List<Board> getBoardNamesByDeptId(Long deptId);

    int createBoard(Board board);

    int insertBoardManagement(Long boardId, List<String> managerIdList);

    List<Board> getBoardListByName1(String boardName1);

    List<BoardAndManagementDTO> getBoardAndManagement(Long boardId);

    int updateBoard(Board board);

    void deleteBoardManagement(Long boardId);

    List<String> findBoardManagerById(Long boardId);

    int deleteBoard(Long boardId);
}
