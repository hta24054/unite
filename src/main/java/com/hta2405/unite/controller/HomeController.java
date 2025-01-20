package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.dto.BoardPostEmpDTO;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import com.hta2405.unite.mybatis.mapper.JobMapper;
import com.hta2405.unite.service.DocService;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class HomeController {
    private final NoticeService noticeService;
    private final EmpService empService;
    private final DocService docService;
    private final JobMapper jobMapper;
    private final BoardPostMapper boardPostMapper;

    public HomeController(NoticeService noticeService, EmpService empService, JobMapper jobMapper, BoardPostMapper boardPostMapper, DocService docService) {
        this.noticeService = noticeService;
        this.empService = empService;
        this.jobMapper = jobMapper;
        this.boardPostMapper = boardPostMapper;
        this.docService = docService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal UserDetails user) {
        model.addAttribute("name", empService.getEmpById(user.getUsername()).getEname());
        model.addAttribute("email", empService.getEmpById(user.getUsername()).getEmail());
        model.addAttribute("job", jobMapper.getJobByEmpId(user.getUsername()).getJobName());
        model.addAttribute("waiting", docService.getWaitingDocs(user.getUsername()).size());
        model.addAttribute("inProgress", docService.getInProgressDTO(user.getUsername()).size());
        return "home2";
    }

    @GetMapping("/home/board")
    @ResponseBody  // JSON 형식으로 응답
    public Map<String, Object> getHomeData(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        response.put("boardList", boardPostMapper.getBoardListAll(empService.getEmpById(user.getUsername()).getDeptId()));
        response.put("emp", empService.getIdToENameMap());
        return response;
    }

    @GetMapping("/home/notice")
    @ResponseBody
    public List<Notice> getAliveNotice() {
        return noticeService.getAliveNotice();
    }


}
