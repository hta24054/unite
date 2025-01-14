package com.hta2405.unite.controller;

import com.google.gson.*;
import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.dto.ProjectTaskDTO;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projectBoard")
@Slf4j
public class ProjectBoardController {
    private final ProjectService projectService;
    private final ProjectBoardService projectBoardService;
    private final EmpService empService;

    public ProjectBoardController(ProjectBoardService projectBoardService, ProjectService projectService, EmpService empService) {
        this.projectBoardService = projectBoardService;
        this.projectService = projectService;
        this.empService = empService;
    }

    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> write(int projectId, String title, String content, MultipartFile file, String category, @AuthenticationPrincipal UserDetails user) {
        String userId = user.getUsername();

        if (file == null || file.isEmpty()) {
            System.out.println("첨부 파일이 없습니다.");
        }

        projectBoardService.insertOrUpdate(title, content, userId, projectId, file, category);

        List<ProjectTaskDTO> recentPosts = projectBoardService.getRecentPosts(projectId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("posts", recentPosts);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/lists")
//    @ResponseBody
//    public Map<String, Object> list(@RequestParam int projectId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userid = authentication.getName();
//
//        List<ProjectTask> taskList = projectBoardService.getTaskList(projectId, userid);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("boardlist", taskList);
//        response.put("projectName", projectService.getProjectName(projectId));
//
//        return response; // JSON 형식으로 반환
//    }
    public ModelAndView prepareModelAndView(int projectId, String memberId, Integer taskId, String viewName) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("projectName", projectService.getProjectName(projectId));
        mv.addObject("projectId", projectId);
        mv.addObject("memberName", empService.getEmpById(memberId).getEname());
        mv.addObject("memberId", memberId);
        mv.addObject("taskId", taskId);
        if (taskId != null) {
            mv.addObject("task", projectBoardService.getTask(projectId, memberId, taskId).get(0));
        }

        mv.setViewName(viewName);
        return mv;
    }

    @GetMapping("/list")
    public ModelAndView tasklist(int projectId, String memberId) {
        ModelAndView mv = prepareModelAndView(projectId, memberId, null, "project/project_task_list");
        mv.addObject("boardlist", projectBoardService.getTaskList(projectId, memberId));
        mv.addObject("message", "진행 과정 게시판");
        return mv;
    }

    @GetMapping("/comm")
    public ModelAndView comm(int projectId, String memberId, int taskId) {
        return prepareModelAndView(projectId, memberId, taskId, "project/project_comm");
    }

    @GetMapping("/modify")
    public ModelAndView modify(int projectId, String memberId, int taskId) {
        return prepareModelAndView(projectId, memberId, taskId, "project/project_modify");
    }

    @PostMapping("/delete")
    public String delete(int projectId, String memberId, int taskId, String board_pass, RedirectAttributes redirectAttributes) {
        Emp emp = empService.getEmpById(memberId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(board_pass, emp.getPassword())) {
            redirectAttributes.addFlashAttribute("delete_error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/projectBoard/list?projectId=" + projectId + "&memberId=" + memberId;
        }

        boolean isDeleted = projectBoardService.deleteTask(projectId, memberId, taskId);

        if (isDeleted) redirectAttributes.addFlashAttribute("delete_message", "삭제가 성공적으로 완료되었습니다.");
        else redirectAttributes.addFlashAttribute("delete_error", "삭제에 실패했습니다. 다시 시도해주세요.");

        return "redirect:/projectBoard/list?projectId=" + projectId + "&memberId=" + memberId;
    }

    @GetMapping("/down")
    public void filedown(String fileuuid, String originalFilename, HttpServletResponse response) throws Exception{
        projectBoardService.fileDown(fileuuid, originalFilename, response);
    }


    @PostMapping("/modifyProcess")
    public String modifyProcess(ModelAndView mv, int projectId, String memberId, int taskId, MultipartFile board_file, String board_subject, String board_content) {
        projectBoardService.modifyProcess(projectId, memberId, taskId, board_file, board_subject, board_content);
        return "redirect:/projectBoard/comm?projectId=" + projectId + "&memberId=" + memberId + "&taskId=" + taskId;
    }

    @PostMapping("/commentadd")
    public ResponseEntity<Integer> commentAdd(String content,
                                              int projectId,
                                              int taskId,
                                              @RequestParam(required = false, defaultValue = "0") int parentCommentId,
                                              @AuthenticationPrincipal UserDetails user){
        String userid = user.getUsername();
        log.info("content={}", content);
        log.info("projectId={}", projectId);
        log.info("taskId={}", taskId);
        int result = projectBoardService.commentAdd(userid, projectId, taskId, content, parentCommentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/commentlist")
    public ResponseEntity<JsonObject> getComments(
            @AuthenticationPrincipal UserDetails user, int projectId, int taskNum,
            @RequestParam("comment_board_num") int commentBoardNum, int state) {
        String userid = user.getUsername();
        log.info("userid: " + userid);
        log.info("projectid: " + projectId);
        log.info("taskNum: " + taskNum);
        log.info("comment_board_num: " + commentBoardNum);
        log.info("state: " + state);

        // 댓글 목록의 개수 가져오기
//        int listCount = projectBoardService.getListCount(commentBoardNum);
//        log.info("listcount: " + listCount);

        // 댓글 목록 가져오기
//        JsonArray commentList = projectBoardService.getCommentList(commentBoardNum, state);

        // JSON 응답 객체 준비
        JsonObject responseObject = new JsonObject();
//        responseObject.addProperty("listcount", listCount);
//        responseObject.add("commentlist", new Gson().toJsonTree(commentList));
        responseObject.addProperty("id", userid);

        // 직원 정보 가져오기
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                .create();
//        JsonElement empUuidMap = gson.toJsonTree(empService.getIdToENameUUIDMap());
//        responseObject.add("emp", empUuidMap);
//
//        System.out.println("emp: " + empUuidMap);

        return ResponseEntity.ok(responseObject);
    }
}
