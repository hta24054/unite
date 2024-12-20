package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Board;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Post;
import com.hta2405.unite.dto.PostComment;
import com.hta2405.unite.dto.PostFile;

public class BoardDao {
	private DataSource ds;
	
	public BoardDao() {
		try {
			InitialContext init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception e) {
			System.out.println("DB연결 실패 " + e.getMessage());
		}
	}

	public ArrayList<Object> getBoardListAll() {
		ArrayList<Object> list = new ArrayList<>();
		ArrayList<Board> boards = new ArrayList<>();
		ArrayList<Post> posts = new ArrayList<>();
		String sql = """
					select post.*, board.*, nvl(cnt,0) as cnt
					from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
						on post.post_id = pc.post_id
					join board
						on board.board_id = post.board_id
					order by post_date desc
				""";
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			try (ResultSet rs= pstmt.executeQuery()){
				while(rs.next()) {
					Board board = new Board();
					Post post = new Post();
					board.setBoardId(rs.getLong("board_id"));
					board.setBoardName1(rs.getString("board_name1"));
					board.setBoardName2(rs.getString("board_name2"));
					board.setDeptId(rs.getLong("dept_id"));
					post.setBoardId(rs.getLong("board_id"));
					post.setPostId(rs.getLong("post_id"));
					post.setPostWriter(rs.getString("post_writer"));
					post.setPostSubject(rs.getString("post_subject"));
					post.setPostContent(rs.getString("post_content"));
					post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
					post.setPostUpdateDate(rs.getTimestamp("post_update_date").toLocalDateTime());
					post.setPostReRef(rs.getLong("post_re_ref"));
					post.setPostReLev(rs.getLong("post_re_lev"));
					post.setPostReSeq(rs.getLong("post_re_seq"));
					post.setPostView(rs.getLong("post_view"));
					post.setPostCommentCnt(rs.getLong("cnt"));
					boards.add(board);
					posts.add(post);
				}
				list.add(boards);
				list.add(posts);
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardListAll() 에러:"+ e);
		}
		return null;
	}
	
	public ArrayList<Object> getBoardListAll(Long deptId) {
		ArrayList<Object> list = new ArrayList<>();
		ArrayList<Board> boards = new ArrayList<>();
		ArrayList<Post> posts = new ArrayList<>();
		ArrayList<Emp> emps = new ArrayList<>();
		String sql = """
					select post.*, board.*, nvl(cnt,0) as cnt, img_path, img_original, img_uuid, img_type
					from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
						on post.post_id = pc.post_id
					join board
						on board.board_id = post.board_id
					join emp
						on emp.emp_id = post.post_writer
					where board.dept_id IS NULL OR ? IN (0000, 1000, 1001)
					    OR (? = 1100 AND board.dept_id between 1100 and 1199)
					    OR (? = 1200 AND board.dept_id between 1200 and 1299)
					    OR (? = 1300 AND board.dept_id between 1300 and 1399)
					    OR (? = 1400 AND board.dept_id between 1400 and 1499)
					    OR (board.dept_id = ?)
					order by post_date desc
				""";
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			for(int i=1; i<=6; i++) {
				pstmt.setLong(i, deptId);
			}
			
			try (ResultSet rs= pstmt.executeQuery()){
				while(rs.next()) {
					Board board = new Board();
					Post post = new Post();
					Emp emp = new Emp();
					board.setBoardId(rs.getLong("board_id"));
					board.setBoardName1(rs.getString("board_name1"));
					board.setBoardName2(rs.getString("board_name2"));
					board.setDeptId(rs.getLong("dept_id"));
					post.setBoardId(rs.getLong("board_id"));
					post.setPostId(rs.getLong("post_id"));
					post.setPostWriter(rs.getString("post_writer"));
					post.setPostSubject(rs.getString("post_subject"));
					post.setPostContent(rs.getString("post_content"));
					post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
					post.setPostUpdateDate(rs.getTimestamp("post_update_date").toLocalDateTime());
					post.setPostReRef(rs.getLong("post_re_ref"));
					post.setPostReLev(rs.getLong("post_re_lev"));
					post.setPostReSeq(rs.getLong("post_re_seq"));
					post.setPostView(rs.getLong("post_view"));
					post.setPostCommentCnt(rs.getLong("cnt"));

					emp.setImgPath(rs.getString("img_path"));
					emp.setImgOriginal(rs.getString("img_original"));
					emp.setImgUUID(rs.getString("img_uuid"));
					emp.setImgType(rs.getString("img_type"));
					
					boards.add(board);
					posts.add(post);
					emps.add(emp);
				}
				list.add(boards);
				list.add(posts);
				list.add(emps);
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardListAll() 에러:"+ e);
		}
		return null;
	}


	public List<Board> getBoardListByDeptId(Long deptId) {
		List<Board> boardList = new ArrayList<>();
		String sql = """
				SELECT *
				FROM board
				WHERE 
				    ? IN (0000, 1000, 1001) AND dept_id IS Not Null
				    OR (? = 1100 AND dept_id between 1100 and 1199)
				    OR (? = 1200 AND dept_id between 1200 and 1299)
				    OR (? = 1300 AND dept_id between 1300 and 1399)
				    OR (? = 1400 AND dept_id between 1400 and 1499)
				    OR (dept_id = ?)
				""";
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			for(int i=1; i<=6; i++) {
				pstmt.setLong(i, deptId);
			}
			try (ResultSet rs= pstmt.executeQuery()){
				while(rs.next()) {
					Board board = new Board();
					board.setBoardId(rs.getLong("board_id"));
					board.setBoardName1(rs.getString("board_name1"));
					board.setBoardName2(rs.getString("board_name2"));
					board.setDeptId(rs.getLong("dept_id"));
					boardList.add(board);
				}
				return boardList;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardListByDeptId() 에러:"+ e);
		}
		return null;
	}
	
	public Board getBoardListByName2(String boardName2) {
		Board board = null;
		String sql = """
				SELECT * FROM BOARD
				where BOARD_NAME2 = ?
				""";
		
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, boardName2);
			try (ResultSet rs= pstmt.executeQuery()){
				if(rs.next()) {
					board = new Board();
					board.setBoardId(rs.getLong("board_id"));
					board.setBoardName1(rs.getString("board_name1"));
					board.setBoardName2(rs.getString("board_name2"));
					board.setDeptId(rs.getLong("dept_id"));
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardListByName2() 에러:"+ e);
		}
		return board;
	}
	
	

	public Boolean postAndFileInsert(Post postData, List<PostFile> postFiles) {
		int num2 = 0;
		try(Connection con =ds.getConnection();){
			int num1 = post_insert(con, postData);
			
			if(!postFiles.isEmpty()) {
				num2 = postFile_insert(con, postFiles);
			}else {
				num2 = 1;
			}
			
			if(num1>0 && num2>0) {
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postAndFileInsert() 에러"+e);
		}
		return false;
	}

	private int post_insert(Connection con, Post postData) {
		String getPostId_sql = "(SELECT NVL(MAX(POST_ID),0)+1 FROM POST)";
		String sql = """
				INSERT INTO POST(BOARD_ID,POST_WRITER,POST_SUBJECT,POST_CONTENT,
				POST_DATE,POST_UPDATE_DATE,POST_RE_REF,POST_RE_LEV,POST_RE_SEQ)
				VALUES( ?, ?, ?, ?, SYSDATE, SYSDATE, %1$s, 0, 0)
				""".formatted(getPostId_sql);
		
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postData.getBoardId());
			pstmt.setString(2, postData.getPostWriter());
			pstmt.setString(3, postData.getPostSubject());
			pstmt.setString(4, postData.getPostContent());
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("post_insert() 에러:"+ e);
		}
		return 0;
	}

	private int postFile_insert(Connection con, List<PostFile> postFiles) {
		return postFile_insert(con, postFiles, false);
	}
	
	private int postFile_insert(Connection con, List<PostFile> postFiles, boolean postIdCheck) {
		int num = 0;
		String sql = "";
		if(postIdCheck) {//글쓰기 수정(postId 있을 경우)
			sql = """
					INSERT INTO POST_FILE( POST_FILE_PATH, POST_FILE_ORIGINAL, POST_FILE_UUID, POST_FILE_TYPE, POST_ID)
					VALUES( ?, ?, ?, ?, ?)
					""";
		}else {//글쓰기(postId 없는 경우)
			String getPostId_sql = "(SELECT MAX(POST_ID) FROM POST)";
			sql = """
				INSERT INTO POST_FILE( POST_FILE_PATH, POST_FILE_ORIGINAL, POST_FILE_UUID, POST_FILE_TYPE, POST_ID)
				VALUES( ?, ?, ?, ?, %1$s)
				""".formatted(getPostId_sql);
		}
		
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			for(PostFile postFile : postFiles) {
				pstmt.setString(1, postFile.getPostFilePath());
				pstmt.setString(2, postFile.getPostFileOriginal());
				pstmt.setString(3, postFile.getPostFileUUID());
				pstmt.setString(4, postFile.getPostFileType());
				if(postIdCheck) {
					pstmt.setLong(5, postFile.getPostId());
				}
				num += pstmt.executeUpdate();
			}
			return num;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postFile_insert() 에러:"+ e);
		}
		return 0;
	}

	//글의 갯수 구하기
	public int getListCountByBoardId(Long boardId) {
		String sql = """
				select count(*) from post
				where board_id = ?
				""";
		int x=0;
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, boardId);
			
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					x = rs.getInt(1);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getListCountByBoardId() 에러: "+e);
		}
		return x;
	}
	
	
	public List<Post> getPostListByBoardId(int page, int limit, Long boardId) {
		
		//page : 페이지
		//limit : 페이지 당 목록의 수
		//board_re_ref desc, board_re_seq asc에 의해 정렬한 것을
		//조건절에 맞는 rnum의 범위 만큼 가져오는 쿼리문입니다.
		
		String post_list_sql ="""
				select *
				from ( select rownum rnum, j.*
						from (
								select post.*, nvl(cnt,0) as cnt
								from post left outer join (select post_id, count(*) cnt
															from post_comment
															group by post_id) pc
								on post.post_id = pc.post_id
								where board_id = ?
								order by post_re_ref desc, post_re_seq asc
						) j
						where rownum <= ? 
				)
				where rnum>=? and rnum<=?
				""";
		
		List<Post> list = new ArrayList<Post>();
										//한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지...
		int startrow = (page - 1) * limit + 1; //읽기 시작할 row 번호(1		11		21...
		int endrow = startrow + limit - 1; 	   //읽을 마지막 row 번호(10	20		30...
		
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(post_list_sql);){
			pstmt.setLong(1, boardId);
			pstmt.setInt(2, endrow);
			pstmt.setInt(3, startrow);
			pstmt.setInt(4, endrow);
			
			try (ResultSet rs= pstmt.executeQuery()){
				//DB에서 가져온 데이터를 BoardBean에 담습니다.
				while(rs.next()) {
					Post post = new Post();
					post.setBoardId(rs.getLong("board_id"));
					post.setPostId(rs.getLong("post_id"));
					post.setPostWriter(rs.getString("post_writer"));
					post.setPostSubject(rs.getString("post_subject"));
					post.setPostContent(rs.getString("post_content"));
					post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
					post.setPostUpdateDate(rs.getTimestamp("post_update_date").toLocalDateTime());
					post.setPostReRef(rs.getLong("post_re_ref"));
					post.setPostReLev(rs.getLong("post_re_lev"));
					post.setPostReSeq(rs.getLong("post_re_seq"));
					post.setPostView(rs.getLong("post_view"));
					post.setPostCommentCnt(rs.getLong("cnt"));
					list.add(post);//값을 담은 객체를 리스트에 저장합니다.
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getPostListByBoardId() 에러:"+ e);
		}
		
		return list;
	}

	//조회수 업데이트 - 글번호에 해당하는 조회수를 1 증가합니다.
	public void setReadCountUpdate(Long postId) {
		String sql = """
				update post 
				set post_view = post_view + 1
				where post_id=?
				""";
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setLong(1, postId);
			pstmt.executeUpdate();
		}catch (Exception e) {
			System.out.println("setReadCountUpdate() 에러"+e);
			e.printStackTrace();
		}
	}

	public List<Object> getDetail(Long postId) {
		String sql = """
				select post.*, nvl(cnt,0) as cnt, img_path, img_original, img_uuid, img_type
				from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
					on post.post_id = pc.post_id
				join emp
					on emp.emp_id = post.post_writer
				where  post.post_id = ?
				""";
		Emp emp  = null;
		Post post = null;
		List<Object> list = new ArrayList<>();
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postId);
			
			List<PostFile> postFileList = getDetailPostFile(con, postId);
			
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					emp = new Emp();
					post = new Post();
					post.setBoardId(rs.getLong("board_id"));
					post.setPostId(rs.getLong("post_id"));
					post.setPostWriter(rs.getString("post_writer"));
					post.setPostSubject(rs.getString("post_subject"));
					post.setPostContent(rs.getString("post_content"));
					post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
					post.setPostUpdateDate(rs.getTimestamp("post_update_date").toLocalDateTime());
					post.setPostReRef(rs.getLong("post_re_ref"));
					post.setPostReLev(rs.getLong("post_re_lev"));
					post.setPostReSeq(rs.getLong("post_re_seq"));
					post.setPostView(rs.getLong("post_view"));
					post.setPostCommentCnt(rs.getLong("cnt"));
					
					emp.setImgPath(rs.getString("img_path"));
					emp.setImgOriginal(rs.getString("img_original"));
					emp.setImgUUID(rs.getString("img_uuid"));
					emp.setImgType(rs.getString("img_type"));
					
					list.add(post);
					list.add(emp);
					list.add(postFileList);
				}
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getDetail() 에러: "+e);
		}
		return null;
	}

	//게시글 첨부파일 다운로드
	public PostFile getPostFile(Long postFileId) {
		String sql = """
				select *
				from post_file
				where post_file_id = ?
				""";
		
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postFileId);
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					PostFile postFile = new PostFile();
					postFile.setPostFileId(rs.getLong("post_file_id"));
					postFile.setPostId(rs.getLong("post_id"));
					postFile.setPostFilePath(rs.getString("post_file_path"));
					postFile.setPostFileOriginal(rs.getString("post_file_original"));
					postFile.setPostFileUUID(rs.getString("post_file_uuid"));
					postFile.setPostFileType(rs.getString("post_file_type"));
					return postFile;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getPostFile() 에러: "+e);
		}
		return null;
	}
	
	//게시글 댓글 조회
	public PostComment getPostCommentByCommentId(Long commentId) {
		String sql = """
				select *
				from post_comment
				where comment_id = ?
				""";
		
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, commentId);
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					PostComment postComment = new PostComment();
					postComment.setCommentId(rs.getLong("comment_id"));
					postComment.setPostId(rs.getLong("post_id"));
					postComment.setPostCommentWriter(rs.getString("post_comment_writer"));
					postComment.setPostCommentContent(rs.getString("post_comment_content"));
					postComment.setPostCommentDate(rs.getTimestamp("post_comment_date").toLocalDateTime());
					postComment.setPostCommentDate(rs.getTimestamp("post_comment_update_date").toLocalDateTime());
					postComment.setPostCommentFilePath(rs.getString("post_comment_file_path"));
					postComment.setPostCommentFileOriginal(rs.getString("post_comment_file_original"));
					postComment.setPostCommentFileUUID(rs.getString("post_comment_file_uuid"));
					postComment.setPostCommentFileType(rs.getString("post_comment_file_type"));
					postComment.setPostCommentReRef(rs.getLong("post_comment_re_ref"));
					postComment.setPostCommentReLev(rs.getLong("post_comment_re_lev"));
					postComment.setPostCommentReSeq(rs.getLong("post_comment_re_seq"));
					return postComment;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getPostCommentByCommentId() 에러: "+e);
		}
		return null;
	}
	
	public List<PostFile> getDetailPostFile(Connection con, Long postId) {
		ArrayList<PostFile> list = new ArrayList<>();
		String sql = """
				select *
				from post_file
				where post_id = ?
				""";
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postId);
			try (ResultSet rs = pstmt.executeQuery()){
				while(rs.next()) {
					PostFile postFile = new PostFile();
					postFile.setPostFileId(rs.getLong("post_file_id"));
					postFile.setPostId(rs.getLong("post_id"));
					postFile.setPostFilePath(rs.getString("post_file_path"));
					postFile.setPostFileOriginal(rs.getString("post_file_original"));
					postFile.setPostFileUUID(rs.getString("post_file_uuid"));
					postFile.setPostFileType(rs.getString("post_file_type"));
					
					list.add(postFile);
				}
				return list;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getDetailPostFile() 에러: "+e);
		}
		return null;
	}

	public Boolean postAndFileModify(Post postData, List<PostFile> postFileList, List<String> deletePostFileUUIDList){
		int result =0;
		try(	Connection con = ds.getConnection();){
			con.setAutoCommit(false);
			try {
				result+=postModify(con, postData);
				result+=postFileModify(con, postFileList, deletePostFileUUIDList);
				
				if(result==2) {
					con.commit();
					return true;
				}else{
					con.rollback();
					return false;
				}
			}catch (Exception e) {
				e.printStackTrace();
				if(con != null) {
					try {
						con.rollback();
					}catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			con.setAutoCommit(true);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postAndFileModify() 에러: "+ e);
		}
		return false;
	}
	
	private int postModify(Connection con, Post postData) {
		String sql = """
				UPDATE POST
				SET BOARD_ID = ?,
					POST_SUBJECT = ?,
					POST_CONTENT = ?,
					POST_UPDATE_DATE = SYSDATE
				WHERE POST_ID = ?
				""";
		try(	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postData.getBoardId());
			pstmt.setString(2, postData.getPostSubject());
			pstmt.setString(3, postData.getPostContent());
			pstmt.setLong(4, postData.getPostId());
			int result = pstmt.executeUpdate();
			if(result==1) {
				System.out.println("업데이트 성공");
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postModify() 에러: "+ e);
		}
		return 0;
	}

	//첨부파일 추가 및 삭제
	private int postFileModify(Connection con, List<PostFile> postFileList, List<String> deletePostFileUUIDList) {
		int insertResult = 1;//첨부파일 추가 안하는 경우를 대비해 1설정
		int deleteResult = 1;//첨부파일 삭제 안하는 경우를 대비해 1설정
		
		if(!postFileList.isEmpty()) {//게시글 첨부파일 추가
			insertResult = postFile_insert(con, postFileList, true);
		}
		if(!deletePostFileUUIDList.isEmpty()) {
			deleteResult = postFile_delete(con, deletePostFileUUIDList);
		}
		
		return (insertResult>0&&deleteResult>0)?1:0;
	}
	
	//게시글 첨부파일 삭제
	private int postFile_delete(Connection con, List<String> deletePostFileUUIDList) {
		int result = 0;
		String sql = """
				DELETE FROM POST_FILE
				WHERE POST_FILE_UUID = ?
				""";
		
		try(	PreparedStatement pstmt = con.prepareStatement(sql);){
			for(String UUID : deletePostFileUUIDList) {
				pstmt.setString(1, UUID);
				result += pstmt.executeUpdate();
			}
			return result;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postModify() 에러: "+ e);
		}
		return 0;
	}

	public boolean postDelete(int postId) {
		String select_sql = """
				select post_re_ref, post_re_lev, post_re_seq 
				from post
				where post_id = ? 
				""";
		
		String post_delete_sql = """
				delete from post
				where post_re_ref = ? 
				and  post_re_lev >= ? 
				and  post_re_seq >= ? 
				and  post_re_seq <=( nvl((select min(post_re_seq)-1 
										from post 
										where post_re_ref = ? 
				 						and post_re_lev = ? 
				 						and post_re_seq > ?), 
				 					   (select max(post_re_seq) 
				 						from post 
				 						where post_re_ref = ?)
				 						)
									)""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(select_sql);){//1
			pstmt.setInt(1, postId);
			try (ResultSet rs = pstmt.executeQuery();){//2
				if(rs.next()) {
					try(PreparedStatement pstmt2 = con.prepareStatement(post_delete_sql);){//3
						pstmt2.setInt(1, rs.getInt("post_re_ref"));
						pstmt2.setInt(2, rs.getInt("post_re_lev"));
						pstmt2.setInt(3, rs.getInt("post_re_seq"));
						pstmt2.setInt(4, rs.getInt("post_re_ref"));
						pstmt2.setInt(5, rs.getInt("post_re_lev"));
						pstmt2.setInt(6, rs.getInt("post_re_seq"));
						pstmt2.setInt(7, rs.getInt("post_re_ref"));
						
						if(pstmt2.executeUpdate() >= 1) {
							return true; // 삭제가 안된 경우에는 false를 반환
						}
					}//try 3
				}//if(rs.next()) {
			}//try2
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("postDelete() 에러"+e);
		}
		
		return false;
	}

	
	public HashMap<Long, String> getIdToboardName2Map() {
        HashMap<Long, String> map = new HashMap<>();
        String sql = """
                    SELECT board_id, board_name2 from board
                """;
        try (Connection conn = ds.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getLong("board_id"), rs.getString("board_name2"));
            }
        } catch (SQLException e) {
            System.out.println("boardMap 불러오기 에러");
            e.printStackTrace();
        }
        return map;
    }

	public Long postAndFileReply(Post postData, List<PostFile> postFiles) {
		
		try(Connection con =ds.getConnection();){
			//트랜직션을 이용하기 위해서 setAutoCommit을 false로 설정합니다.
			con.setAutoCommit(false);
			
			try {
				reply_update(con, postData.getPostReRef(), postData.getPostReSeq());
				Long postId=reply_insert(con, postData, postFiles);
				
				if(postId>0) {
					con.commit();
					return postId;
				}else {
					con.rollback();
				}
			}catch (SQLException e) {
				e.printStackTrace();
				
				if(con != null) {
					try {
						con.rollback(); // rollback합니다.
					}catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			}
			con.setAutoCommit(true);
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("boardReply() 에러"+e);
		}
		return 0L;
	}

	public void reply_update(Connection con, Long post_re_ref, Long post_re_seq) throws SQLException{
		// BOARD_RE_REF, BOARD_RE_SEQ 값을 확인하여 원문 글에 답글이 달려있다면
		// 달린 답글들의 BOARD_RE_SEQ값을 1씩 증가시킵니다.
		// 현재 글을 이미 달린 답글보다 앞에 출력되게 하기 위해서 입니다.
		
		String sql = """
				update post
				set post_re_seq = post_re_seq + 1
				where post_re_ref = ?
				and post_re_seq > ?
				""";
		
		try (PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, post_re_ref);
			pstmt.setLong(2, post_re_seq);
			pstmt.executeUpdate();
		}
	}
	
	//게시글 답글 post, postFile 추가
	public Long reply_insert(Connection con, Post postData, List<PostFile> postFiles) throws SQLException{
		int count = 0;
		Long postId = reply_post_insert(con, postData);
		
		if(!postFiles.isEmpty()) {
			count = postFile_insert(con, postFiles);
		}else {
			count = 1;
		}
		
		if(postId>0 && count>0) {
			return postId;
		}
		return 0L;
	}
	
	//게시글 답글 추가
	public Long reply_post_insert(Connection con, Post postData) throws SQLException{
		String sql = """
				insert into POST(BOARD_ID,POST_WRITER,POST_SUBJECT,POST_CONTENT,
				POST_DATE,POST_UPDATE_DATE,POST_RE_REF,POST_RE_LEV,POST_RE_SEQ)
				values( ?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?, ?)
				""";
		
		try(PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postData.getBoardId());
			pstmt.setString(2, postData.getPostWriter());
			pstmt.setString(3, postData.getPostSubject());
			pstmt.setString(4, postData.getPostContent());
			pstmt.setLong(5, postData.getPostReRef());
			pstmt.setLong(6, postData.getPostReLev() + 1);
			pstmt.setLong(7, postData.getPostReSeq() + 1);
			pstmt.executeUpdate();
		}

		Long postId = 0L;
		String board_max_sql = "SELECT MAX(POST_ID) FROM POST";
		try (PreparedStatement pstmt = con.prepareStatement(board_max_sql);){
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					postId=rs.getLong(1);
				}
			}
		}
		
		return postId;
	}

	public int getSearchListCountByBoardId(Long boardId, String category, String query) {
		String sql="";
		if(category.equals("작성자")) {
			sql = """
					SELECT COUNT(*)
					FROM post
					WHERE post_writer in (
					    SELECT emp_id
					    FROM emp
					    WHERE ename LIKE ?
					)
					AND board_id = ?
					""";
		}else{
			String searchColmn = category.equals("제목")? "post_subject": "post_content";
			
			sql = """
					SELECT COUNT(*) 
					FROM post 
					WHERE %s LIKE ? 
					and board_id = ?
					""".formatted(searchColmn);
		}
		
		int x=0;
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, '%'+query+'%');
			pstmt.setLong(2, boardId);
			try (ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					x = rs.getInt(1);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getSearchListCountByBoardId() 에러: "+e);
		}
		return x;
	}

	public List<Post> getSearchPostListByBoardId(int page, int limit, Long boardId, String category, String query) {
		String searchWhere = "";
		if(category.equals("제목")) {
			searchWhere = "post_subject LIKE ? ";
		}else if(category.equals("내용")) {
			searchWhere = "post_content LIKE ? ";
		}else {
			searchWhere = """
					post_writer in (
					    SELECT emp_id
					    FROM emp
					    WHERE ename LIKE ?
					)
					""";
		}
		String post_list_sql ="""
				select *
				from ( select rownum rnum, j.*
						from (
								select post.*, nvl(cnt,0) as cnt
								from post left outer join (select post_id, count(*) cnt
															from post_comment
															group by post_id) pc
								on post.post_id = pc.post_id
								where board_id = ?
								and %s
								order by post_re_ref desc, post_re_seq asc
						) j
						where rownum <= ? 
				)
				where rnum>=? and rnum<=?
				""".formatted(searchWhere);
		
		List<Post> list = new ArrayList<Post>();
										//한 페이지당 10개씩 목록인 경우 1페이지, 2페이지, 3페이지...
		int startrow = (page - 1) * limit + 1; //읽기 시작할 row 번호(1		11		21...
		int endrow = startrow + limit - 1; 	   //읽을 마지막 row 번호(10		20		30...
		
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(post_list_sql);){
			pstmt.setLong(1, boardId);
			pstmt.setString(2, '%'+query+'%');
			pstmt.setInt(3, endrow);
			pstmt.setInt(4, startrow);
			pstmt.setInt(5, endrow);
			
			try (ResultSet rs= pstmt.executeQuery()){
				//DB에서 가져온 데이터를 BoardBean에 담습니다.
				while(rs.next()) {
					Post post = new Post();
					post.setBoardId(rs.getLong("board_id"));
					post.setPostId(rs.getLong("post_id"));
					post.setPostWriter(rs.getString("post_writer"));
					post.setPostSubject(rs.getString("post_subject"));
					post.setPostContent(rs.getString("post_content"));
					post.setPostDate(rs.getTimestamp("post_date").toLocalDateTime());
					post.setPostUpdateDate(rs.getTimestamp("post_update_date").toLocalDateTime());
					post.setPostReRef(rs.getLong("post_re_ref"));
					post.setPostReLev(rs.getLong("post_re_lev"));
					post.setPostReSeq(rs.getLong("post_re_seq"));
					post.setPostView(rs.getLong("post_view"));
					post.setPostCommentCnt(rs.getLong("cnt"));
					list.add(post);//값을 담은 객체를 리스트에 저장합니다.
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getPostListByBoardId() 에러:"+ e);
		}
		
		return list;
	}
	
	
	
}
