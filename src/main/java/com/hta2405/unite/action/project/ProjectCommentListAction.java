package com.hta2405.unite.action.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.ProjectbDao;
import com.hta2405.unite.util.LocalDateTimeAdapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//프로젝트 상세. main에서 누르고 들어오는 첫 창
public class ProjectCommentListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 세션에서 사용자 ID와 프로젝트 ID 가져오기
        String userid = (String) req.getSession().getAttribute("id");
        int projectid = (Integer) req.getSession().getAttribute("projectId");

        // AJAX로 전송된 파라미터 받기
        String num = req.getParameter("num");
        System.out.println(num);
        
        int taskNum = Integer.parseInt(req.getParameter("num"));
        String memberId = req.getParameter("userid");

        System.out.println("userid: " + userid);
        System.out.println("projectid: " + projectid);
        System.out.println("taskNum: " + taskNum);
        System.out.println("memberId: " + memberId);

        ProjectbDao dao = new ProjectbDao();
        
        // 필요한 데이터를 가져오기
        int commentBoardNum = Integer.parseInt(req.getParameter("comment_board_num"));
        int state = Integer.parseInt(req.getParameter("state"));

        // 댓글 목록의 개수 가져오기
        int listCount = dao.getListCount(commentBoardNum);
        System.out.println("comment_board_num: " + commentBoardNum);
        System.out.println("state: " + state);

        // JSON 객체로 응답 준비
        JsonObject object = new JsonObject();
        object.addProperty("listcount", listCount);

        // 댓글 목록 가져오기
        JsonArray commentList = dao.getCommentList(commentBoardNum, state);
        JsonElement commentListJson = new Gson().toJsonTree(commentList);
        object.add("commentlist", commentListJson);

        // 사용자 ID 포함
        object.addProperty("id", userid);

        // 직원 정보 가져오기 (EmpDao에서 가져온 데이터)
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        JsonElement empUuidMap = gson.toJsonTree(new EmpDao().getIdToENameUUIDMap());
        object.add("emp", empUuidMap);

        System.out.println("emp: " + empUuidMap);

        // 응답 타입과 문자 인코딩 설정
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print(object.toString());
        out.flush();

        return null;
    }



}
