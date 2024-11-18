package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Board;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.dto.Post;
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
					order by post_re_ref desc, post_re_seq asc
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
				POST_DATE,POST_UPDATE_DATE,POST_RE_REF,POST_RE_LEV,POST_RE_SEQ,EMP_ID)
				VALUES( ?, ?, ?, ?, SYSDATE, SYSDATE, %1$s, 0, 0, ?)
				""".formatted(getPostId_sql);
		
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postData.getBoardId());
			pstmt.setString(2, postData.getPostWriter());
			pstmt.setString(3, postData.getPostSubject());
			pstmt.setString(4, postData.getPostContent());
			pstmt.setString(5, postData.getEmpId());
			return pstmt.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("post_insert() 에러:"+ e);
		}
		return 0;
	}

	private int postFile_insert(Connection con, List<PostFile> postFiles) {
		int num = 0;
		String getPostId_sql = "(SELECT MAX(POST_ID) FROM POST)";
		String sql = """
				INSERT INTO POST_FILE( POST_ID, POST_FILE_PATH, POST_FILE_ORIGINAL, POST_FILE_UUID, POST_FILE_TYPE)
				VALUES( %1$s, ?, ?, ?, ?)
				""".formatted(getPostId_sql);
		
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			for(PostFile postFile : postFiles) {
				pstmt.setString(1, postFile.getPostFilePath());
				pstmt.setString(2, postFile.getPostFileOriginal());
				pstmt.setString(3, postFile.getPostFileUUID());
				pstmt.setString(4, postFile.getPostFileType());
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
					post.setEmpId(rs.getString("emp_id"));
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
	public void setReadCountUpdate(int num) {
		String sql = """
				update post 
				set post_view = post_view + 1
				where post_id=?
				""";
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}catch (Exception e) {
			System.out.println("setReadCountUpdate() 에러"+e);
			e.printStackTrace();
		}
	}

	public List<Object> getDetail(int postId) {
		String sql = """
				select post.*, nvl(cnt,0) as cnt, img_path, img_original, img_uuid, img_type
				from post left outer join (select post_id, count(*) as cnt
											from post_comment
											group by post_id) pc
					on post.post_id = pc.post_id
				join emp
					on emp.emp_id = post.emp_id
				where  post.post_id = ?
				""";
		Emp emp  = null;
		Post post = null;
		List<Object> list = new ArrayList<>();
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setInt(1, postId);
			
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
					post.setEmpId(rs.getString("emp_id"));
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

	private List<PostFile> getDetailPostFile(Connection con, int postId) {
		ArrayList<PostFile> list = new ArrayList<>();
		String sql = """
				select *
				from post_file
				where post_id = ?
				""";
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setInt(1, postId);
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
	
	
}
