package com.hta2405.unite.service;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.enums.ChatMessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiService {
    private final ChatClient chatClient;
    private final MessengerService messengerService;
    private final EmpService empService;

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

    public String summarizeChatRoom(Long chatRoomId) {
        String template = """
                -- 다음 대화 내용보고 대화 주제와, 전체적인 요약문을 200자 이내로 제공하세요
                -- 답변은 한글로 합니다.
                아래의 요청에 대한 답변을 작성하되, 가독성을 위해 적절한 위치에서 줄바꿈을 포함해 주세요:
                {message}
                """;
        List<ChatMessage> messageList = messengerService.getMessagesByRoomId(chatRoomId);
        Map<String, String> nameMap = empService.getIdToENameMap();
        StringBuilder stringBuilder = new StringBuilder();
        messageList.stream()
                .filter(m -> m.getChatMessageType() == ChatMessageType.NORMAL)
                .forEach(m -> stringBuilder.append(
                        String.format("발신자 = [%s], 내용 = [%s], 시간 = [%s]\n",
                                nameMap.get(m.getSenderId()), m.getChatMessageContent(), m.getChatMessageDate()
                        )));

        log.info("chatMessage = {}", stringBuilder);
        String content = chatClient.prompt()
                .user(promptUserSpec -> promptUserSpec.text(template)
                        .param("today", LocalDate.now())
                        .param("message", stringBuilder.toString()))
                .call().content();
        log.info("aiResponse = {}", content);
        return content;
    }
}
