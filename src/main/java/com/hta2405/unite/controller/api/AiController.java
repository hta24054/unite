package com.hta2405.unite.controller.api;

import com.hta2405.unite.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    public Map<String, Object> chatSchedule(@RequestBody Map<String, String> request, @AuthenticationPrincipal UserDetails user) {
        String message = request.get("message");
        Map<String, Object> map = new HashMap<>();
        boolean result = aiService.addSchedule(message, user.getUsername());
        map.put("result", result);
        String resultMessage = "일정을 추가했습니다.";

        if (!result) {
            resultMessage = "일정 추가 실패";
        }
        map.put("resultMessage", resultMessage);
        return map;
    }
}
