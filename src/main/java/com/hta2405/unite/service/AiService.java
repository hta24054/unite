package com.hta2405.unite.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final ChatClient chatClient;

    public String chat(String message) {
        String template = """
                -- 오늘의 날짜는 {today} 입니다.
                -- 답변은 한글로 합니다.
                아래의 요청에 대한 답변을 작성하되, 가독성을 위해 적절한 위치에서 줄바꿈을 포함해 주세요:
                {message}
                """;
        String content = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("today", LocalDate.now())
                        .param("message", message))
                .call().content();
        log.info("aiResponse = {}", content);
        return content;
    }
}
