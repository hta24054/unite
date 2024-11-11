package com.hta2405.unite.action.post;

import java.io.IOException;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.BoardDao;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dto.Board;
import com.hta2405.unite.dto.Dept;
import com.hta2405.unite.dto.Post;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class PostAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		BoardDao boardDao = new BoardDao();
		DeptDao deptDao = new DeptDao();
		
		Board boarddata = new Board();
		Post postdata = new Post();
		
		ActionForward forward = new ActionForward();
		
		String realFolder = "";
		
		//webapp아래에 꼭 폴더 생성하세요
		String saveFolder = "boardupload";
		
		int fileSize = 5 * 1024 * 1024; // 업로드할 파일의 최대 사이즈 입니다. 5MB
		
		// 실제 저장 경로를 지정합니다.
		ServletContext sc = req.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		System.out.println("realFolder = "+ realFolder);
		
		HttpSession session = req.getSession();
		
		try {
			MultipartRequest multi =
					new MultipartRequest(req, realFolder, fileSize, "utf-8",
					new DefaultFileRenamePolicy());
			
			//deptId 가져오기
			Long deptId = deptDao.getDeptIdByDeptName(multi.getParameter("boardName2"));
			//deptId가 null(전사게시판, 일반게시판)인 경우, deptId 9999 지정
			if(deptId == null) {
				deptId = (long) 9999;
			}
			
			//board 객체에 저장
			boarddata.setBoardName1(multi.getParameter("boardName1"));
			boarddata.setBoardName2(multi.getParameter("boardName2"));
			boarddata.setDeptId(deptId);
			Boolean boardCheck = boardDao.BoardInsert(boarddata);
			
			if(!boardCheck) {
				System.out.println("board 삽입 실패");
			}else {
				System.out.println("board 삽입 성공");
				postdata.setPostId(Long.valueOf((String) session.getAttribute("id")));
				postdata.setPostWriter((String) session.getAttribute("ename"));
				postdata.setPostSubject(multi.getParameter("board_subject"));
				postdata.setPostContent(multi.getParameter("board_content"));
				

				//시스템 상에 업로드된 실제 파일명을 얻어 옵니다.
				String filename = multi.getFilesystemName("board_file");
				System.out.println(filename);
				
				//boarddata.setBoard_file(filename);
				
				//글 등록 처리를 위해 DAO의 boardInsert()메서드를 호출합니다.
				//글 등록 폼에서 입력한 정보가 저장되어 있는 boarddata객체를 전달합니다.
				//boolean result = boarddao.boardInsert(boarddata);
				
				
				//글 등록에 실패할 경우 false를 반환합니다.
//				if(!result) {
//					System.out.println("게시판 등록 실패");
//					forward.setPath("/WEB-INF/views/error/error.jsp");
//					request.setAttribute("message", "게시판 등록 실패입니다.");
//					forward.setRedirect(false);
//				}else {
//					System.out.println("게시판 등록 완료");
//					
//					//글 등록이 완료되면 글 목록을 보여주기 위해 "boards/list"로 이동합니다.
//					//Redirect여부를 true로 설정합니다.
//					forward.setRedirect(true);
//					forward.setPath("list");//이동할 경로를 지정합니다.
//				}
			}
			return forward;
		}catch (IOException e) {
			e.printStackTrace();
			forward.setPath("/WEB-INF/views/error/error.jsp");
			req.setAttribute("message", "게시판 업로드 실패입니다.");
			forward.setRedirect(false);
			return forward;
		}//catch end
	}//excute end

}
