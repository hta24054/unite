package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Emp;
import com.hta2405.unite.domain.ProjectComment;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.ProjectBoardService;
import com.hta2405.unite.service.ProjectService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        mv.addObject("list",projectBoardService.getTaskList(projectId, memberId).get(0).getTaskContent());
        mv.addObject("comm", projectBoardService.commentCount(projectId, memberId));
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
    public ResponseEntity<Resource> filedown(String fileuuid, String originalFilename){
        return projectBoardService.fileDown(fileuuid, originalFilename);
    }


    @PostMapping("/modifyProcess")
    public String modifyProcess(ModelAndView mv, int projectId, String memberId, int taskId, MultipartFile board_file, String board_subject, String board_content) {
        projectBoardService.modifyProcess(projectId, memberId, taskId, board_file, board_subject, board_content);
        log.info("boardFile={}",board_file);
        return "redirect:/projectBoard/comm?projectId=" + projectId + "&memberId=" + memberId + "&taskId=" + taskId;
    }

    @Builder
    @PostMapping("/commentadd")
    public ResponseEntity<Integer> commentAdd(String content,
                                              int taskId,
                                              int comment_re_lev,
                                              int comment_re_seq,
                                              @AuthenticationPrincipal UserDetails user){
        String userid = user.getUsername();

        ProjectComment.ProjectCommentBuilder projectCommentBuilder = ProjectComment.builder()
                .taskCommentWriter(userid)
                .taskId(taskId)
                .taskCommentContent(content)
                .taskCommentReLev(comment_re_lev)
                .taskCommentReSeq(comment_re_seq);

        int result = projectBoardService.commentAdd(projectCommentBuilder);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/commentlist")
    public ResponseEntity<Map<String, Object>> getComments(
            @AuthenticationPrincipal UserDetails user, int taskId, int state) {

        // 사용자 정보와 요청 파라미터 로그
        String userid = user.getUsername();
        log.info("userid: " + userid);
        log.info("taskNum: " + taskId);
        log.info("state: " + state);

        int listCount = projectBoardService.getListCount(taskId);
        log.info("listCount={}", listCount);

        List<ProjectComment> commentList = projectBoardService.getCommentList(taskId, state);

        // 응답 데이터를 하나의 Map으로 묶기
        Map<String, Object> response = new HashMap<>();
        response.put("listcount", listCount);
        response.put("commentlist", commentList);
        //response.put("emp", empService.getEmpById(userid));

        if (commentList != null && !commentList.isEmpty()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reply")
    public ResponseEntity<Integer> replyAdd(String content,
                                              int taskId,
                                              int comment_re_lev,
                                              int comment_re_seq,
                                              int comment_re_ref,
                                              @AuthenticationPrincipal UserDetails user){
        String userid = user.getUsername();

        ProjectComment.ProjectCommentBuilder projectCommentBuilder = ProjectComment.builder()
                .taskCommentWriter(userid)
                .taskId(taskId)
                .taskCommentContent(content)
                .taskCommentReLev(comment_re_lev + 1)
                .taskCommentReSeq(comment_re_seq + 1)
                .taskCommentReRef(comment_re_ref);

        int result = projectBoardService.replyAdd(projectCommentBuilder);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/update")
    public ResponseEntity<Integer> update(String content, int taskId){
        ProjectComment.ProjectCommentBuilder projectCommentBuilder = ProjectComment.builder()
                .taskId(taskId)
                .taskCommentContent(content);
        log.info("taskId: " + taskId);
        log.info("content: " + content);
        int result = projectBoardService.updateComment(projectCommentBuilder);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/delete")
    public ResponseEntity<Integer> delete(int taskId){
        int result = projectBoardService.deleteComment(taskId);
        return ResponseEntity.ok(result);
    }
}
