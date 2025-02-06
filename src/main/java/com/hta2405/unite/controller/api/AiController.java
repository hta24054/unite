package com.hta2405.unite.controller.api;

import com.hta2405.unite.dto.ai.AiChatSummarizeDTO;
import com.hta2405.unite.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;

    @PostMapping("/chat")
    public String chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return aiService.chat(message);
    }

    @GetMapping("/summarize")
    public AiChatSummarizeDTO summarizeChatRoom(Long chatRoomId) {
        return aiService.summarizeChatRoom(chatRoomId);
    }
}
