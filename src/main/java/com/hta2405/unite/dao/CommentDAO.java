package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.PostComment;
import com.hta2405.unite.util.LocalDateTimeAdapter;

public class CommentDAO {
	private DataSource ds;
	
	public CommentDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch (Exception ex) {
			System.out.println("DB 연결 실패 : "+ex);
		}
	}
	
	public int getListCount(int postId) {
		String sql ="""
				select count(*)
				from post_comment
				where post_id = ?
				""";
		int count =0;
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setInt(1, postId);
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					count= rs.getInt(1);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getListCount() 에러:"+e);
		}
		return count;
	}

	public JsonArray getCommentList(int postId, int state) {
		//asc:등록순, desc:최신순
		//state==1 ? "asc":"desc";
		JsonArray arr = new JsonArray();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    
		String sql = """
				select comm.*, ename, img_path, img_original, img_uuid, img_type
				from post_comment comm join emp
				on comm.post_comment_writer = emp.emp_id
				where post_id = ?
				order by post_comment_re_ref %s, post_comment_re_seq asc
				""".formatted( state==1 ? "asc":"desc");
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setInt(1, postId);
			try(ResultSet rs = pstmt.executeQuery()){
				
				while(rs.next()) {
					JsonObject object = new JsonObject();
					object.addProperty("commentId", rs.getLong(1));
					object.addProperty("postId", rs.getLong(2));
					object.addProperty("postCommentWriter", rs.getString(3)); //emp_id
					object.addProperty("postCommentContent", rs.getString(4));
					object.addProperty("postCommentDate", rs.getTimestamp(5).toLocalDateTime().format(formatter));
					object.addProperty("postCommentUpdateDate", rs.getTimestamp(6).toLocalDateTime().format(formatter));
					object.addProperty("postCommentFilePath", rs.getString(7));
					object.addProperty("postCommentFileOriginal", rs.getString(8));
					object.addProperty("postCommentFileUUID", rs.getString(9));
					object.addProperty("postCommentFileType", rs.getString(10));
					object.addProperty("postCommentReRef", rs.getLong(11));
					object.addProperty("postCommentReLev", rs.getLong(12));
					object.addProperty("postCommentReSeq", rs.getLong(13));
					object.addProperty("ename", rs.getString(14));
					object.addProperty("imgPath", rs.getString(15));
					object.addProperty("imgOriginal", rs.getString(16));
					object.addProperty("imgUUID", rs.getString(17));
					object.addProperty("imgType", rs.getString(18));
					
					arr.add(object);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("getCommentList() 에러:"+e);
		}
		return arr;
	}//getCommentList()메서드 end
/*
	public int commentsInsert(PostComment co) {
		int result=0;
		String sql = """
				insert into comm
				values(com_seq.nextval,?,?,sysdate,?,?,?,com_seq.nextval)
				""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setString(1, co.getId());
			pstmt.setString(2, co.getContent());
			pstmt.setInt(3, co.getComment_board_num());
			pstmt.setInt(4, co.getComment_re_lev());
			pstmt.setInt(5, co.getComment_re_seq());
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 삽입 완료되었습니다.");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsInsert() 에러:"+e);
		}
		return result;
	}//commentsInsert() end

	public int commentsUpdate(Comment co) {
		int result=0;
		String sql = """
				update comm
				set content = ? 
				where num = ?
				""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){

			pstmt.setString(1, co.getContent());
			pstmt.setInt(2, co.getNum());
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 변경 완료되었습니다.");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsUpdate() 에러:"+e);
		}
		return result;
	}//commentsUpdate() 메서드

	public int commentsDelete(int num) {
		int result = 0;
		String sql =  """
				delete comm
				where num=?
				""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			if(result==1) {
				System.out.println("데이터 삭제 되었습니다.");
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsDelete() 에러:"+e);
		}
		return result;
	}//commentsDelete() 메서드

	public int commentsReply(Comment c) {
		int result = 0;
		
		try(Connection con = ds.getConnection();){
			
			con.setAutoCommit(false);
			
			try {
				reply_update(con, c.getComment_re_ref(), c.getComment_re_seq());
				result = reply_insert(con,c);
				con.commit();
			}catch (Exception e) {
				e.printStackTrace(); //오류 확인용
				if(con != null) {
					try {
						con.rollback();//rollback합니다.
					}catch (Exception ex) {
						ex.printStackTrace();
					} 
				}
			}
			con.setAutoCommit(true);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}//commentsReply() end

	private void reply_update(Connection con, int re_ref, int re_seq) 
													throws SQLException{
		String update_sql = """
				update comm
				set comment_re_seq = comment_re_seq + 1
				where comment_re_ref = ?
				and comment_re_seq > ?
				""";
		try (	PreparedStatement pstmt = con.prepareStatement(update_sql);){
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
		}
	}

	private int reply_insert(Connection con, Comment co) throws SQLException{
		int result = 0;
		String sql = """
				insert into comm
				values(com_seq.nextval, ?, ?, sysdate, ?, ?, ?, ?)
				""";
		try (PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, co.getId());
			pstmt.setString(2, co.getContent());
			pstmt.setInt(3, co.getComment_board_num());
			pstmt.setInt(4, co.getComment_re_lev() + 1);
			pstmt.setInt(5, co.getComment_re_seq() + 1);
			pstmt.setInt(6, co.getComment_re_ref());
			result = pstmt.executeUpdate();
		}
		return result;
	}
*/

}
