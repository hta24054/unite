package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.mybatis.mapper.BoardPostMapper;
import com.hta2405.unite.service.EmpService;
import com.hta2405.unite.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeApiController {

    private final BoardPostMapper boardPostMapper;
    private final EmpService empService;
    private final NoticeService noticeService;

    @GetMapping("/board")
    public Map<String, Object> getHomeData(@AuthenticationPrincipal UserDetails user) {
        Map<String, Object> response = new HashMap<>();
        response.put("boardList", boardPostMapper.getBoardListAll(empService.getEmpById(user.getUsername()).getDeptId(), 4));
        response.put("emp", empService.getIdToENameMap());
        return response;
    }

    @GetMapping("/notice")
    public List<Notice> getAliveNotice() {
        return noticeService.getAliveNotice();
    }
}
