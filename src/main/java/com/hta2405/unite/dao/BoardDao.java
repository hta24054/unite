package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hta2405.unite.dto.Board;
import com.hta2405.unite.dto.Post;

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
		String sql = """
					select post.*,board.board_name1,board.board_name2,dept_id, nvl(cnt,0) as cnt
					from post left outer join (select post_id , count(*) as cnt
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
					list.add(board);
					list.add(post);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardList() 에러:"+ e);
		}
		
		return list;
	}

	public Board getBoardListByName2(String boardName2) {
		Board board = null;
		String sql = """
				select * from dept
				where boardName2 = ?
				""";
		
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
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
			System.out.println("getBoardList() 에러:"+ e);
		}
		return board;
	}

	public boolean BoardInsert(Board boarddata) {
		String sql = """
				INSERT INTO BOARD(BOARD_NAME1,BOARD_NAME2,DEPT_ID)
				VALUES(?,?,?)
				""";
		
		try (	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, boarddata.getBoardName1());
			pstmt.setString(2, boarddata.getBoardName2());
			pstmt.setLong(3, boarddata.getDeptId());
			int result = pstmt.executeUpdate();
			return result == 1 ? true : false;
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getBoardList() 에러:"+ e);
		}
		return false;
	}
	
	
}
