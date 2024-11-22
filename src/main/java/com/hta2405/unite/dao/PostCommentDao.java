package com.hta2405.unite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hta2405.unite.dto.PostComment;

public class PostCommentDao {
	private DataSource ds;
	
	public PostCommentDao() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		}catch (Exception ex) {
			System.out.println("DB 연결 실패 : "+ex);
		}
	}
	
	public int getListCount(Long postId) {
		String sql ="""
				select count(*)
				from post_comment
				where post_id = ?
				""";
		int count =0;
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setLong(1, postId);
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

	public JsonArray getCommentList(Long postId, int state) {
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
			
			pstmt.setLong(1, postId);
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

	public int commentsInsert(PostComment postCommentData) {
		String getPostId_sql = "(SELECT NVL(MAX(comment_id),0)+1 FROM post_comment)";
		String sql = """
				insert into post_comment
				(post_id, post_comment_writer, post_comment_content, post_comment_date, post_comment_update_date,
				post_comment_file_path, post_comment_file_original, post_comment_file_uuid, post_comment_file_type,
				post_comment_re_ref, post_comment_re_lev, post_comment_re_seq)
				values(?,?,?,sysdate,sysdate,?,?,?,?,%1$s,?,?)
				""".formatted(getPostId_sql);
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			
			pstmt.setLong(1, postCommentData.getPostId());
			pstmt.setString(2, postCommentData.getPostCommentWriter());
			pstmt.setString(3, postCommentData.getPostCommentContent());
			pstmt.setString(4, postCommentData.getPostCommentFilePath());
			pstmt.setString(5, postCommentData.getPostCommentFileOriginal());
			pstmt.setString(6, postCommentData.getPostCommentFileUUID());
			pstmt.setString(7, postCommentData.getPostCommentFileType());
			pstmt.setLong(8, postCommentData.getPostCommentReLev());
			pstmt.setLong(9, postCommentData.getPostCommentReSeq());
			
			int result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 삽입 완료되었습니다.");
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsInsert() 에러:"+e);
		}
		return 0;
	}

	public int commentsUpdate(PostComment postCommentData) {
		String sql = """
				update post_comment
				set post_comment_content = ?,
					post_comment_update_date = sysdate,
					post_comment_file_path = ?,
					post_comment_file_original = ?,
					post_comment_file_uuid = ?,
					post_comment_file_type = ?
				where comment_id = ?
				""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setString(1, postCommentData.getPostCommentContent());
			pstmt.setString(2, postCommentData.getPostCommentFilePath());
			pstmt.setString(3, postCommentData.getPostCommentFileOriginal());
			pstmt.setString(4, postCommentData.getPostCommentFileUUID());
			pstmt.setString(5, postCommentData.getPostCommentFileType());
			pstmt.setLong(6, postCommentData.getCommentId());
			int result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("데이터 변경 완료되었습니다.");
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsUpdate() 에러:"+e);
		}
		return 0;
	}//commentsUpdate() 메서드

	public int commentsDelete(Long commentId) {
		String sql =  """
				delete post_comment
				where comment_id = ?
				""";
		try(	Connection con = ds.getConnection();
				PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, commentId);
			int result = pstmt.executeUpdate();
			if(result==1) {
				System.out.println("데이터 삭제 되었습니다.");
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("commentsDelete() 에러:"+e);
		}
		return 0;
	}
	
	public int commentsReply(PostComment postCommentData) {
		
		try(Connection con = ds.getConnection();){
			con.setAutoCommit(false);
			try {
				reply_update(con, postCommentData.getPostCommentReRef(), postCommentData.getPostCommentReSeq());
				int result = reply_insert(con,postCommentData);
				if(result > 0) {
					con.commit();
					return result;
				}
				con.rollback();
			}catch (Exception e) {
				e.printStackTrace();
				if(con != null) {
					con.rollback();
				}
			}
			con.setAutoCommit(true);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void reply_update(Connection con, Long postCommentReRef, Long postCommentReSeq) 
													throws SQLException{
		String sql = """
				update post_comment
				set post_comment_re_seq = post_comment_re_seq + 1
				where post_comment_re_ref = ?
				and post_comment_re_seq > ?
				""";
		try (	PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postCommentReRef);
			pstmt.setLong(2, postCommentReSeq);
			pstmt.executeUpdate();
		}
	}

	private int reply_insert(Connection con, PostComment postCommentData) throws SQLException{
		int result = 0;
		String sql = """
				insert into post_comment
				(post_id, post_comment_writer, post_comment_content, post_comment_date, post_comment_update_date,
				post_comment_file_path, post_comment_file_original, post_comment_file_uuid, post_comment_file_type,
				post_comment_re_ref, post_comment_re_lev, post_comment_re_seq)
				values(?,?,?,sysdate,sysdate,?,?,?,?,?,?,?)
				""";
		try (PreparedStatement pstmt = con.prepareStatement(sql);){
			pstmt.setLong(1, postCommentData.getPostId());
			pstmt.setString(2, postCommentData.getPostCommentWriter());
			pstmt.setString(3, postCommentData.getPostCommentContent());
			pstmt.setString(4, postCommentData.getPostCommentFilePath());
			pstmt.setString(5, postCommentData.getPostCommentFileOriginal());
			pstmt.setString(6, postCommentData.getPostCommentFileUUID());
			pstmt.setString(7, postCommentData.getPostCommentFileType());
			pstmt.setLong(8, postCommentData.getPostCommentReRef());
			pstmt.setLong(9, postCommentData.getPostCommentReLev() +1);
			pstmt.setLong(10, postCommentData.getPostCommentReSeq() +1);
			result = pstmt.executeUpdate();
		}
		return result;
	}

}
