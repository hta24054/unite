package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.PostComment;
import com.hta2405.unite.dto.ChatRoomDTO;
import com.hta2405.unite.service.MessengerService;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/messenger")
public class MessengerApiController {
    private final MessengerService messengerService;

    public MessengerApiController(MessengerService messengerService) {
        this.messengerService = messengerService;
    }

    @GetMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> getAllRooms(@AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();
        List<ChatRoomDTO> chatRoomDTOList = messengerService.getAllChatRooms(empId);

        HashMap<String, Object> response = new HashMap<>();
        response.put("chatRoomDTOList", chatRoomDTOList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chatRooms/{userId}")
    public List<Long> getChatRoomsForUser(@PathVariable String userId) {
        return messengerService.getChatRoomsForUser(userId);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<HashMap<String, Object>> getRoomById(@PathVariable Long id) {
        List<ChatMessage> chatMessageList = messengerService.getChatRoomById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("messengerNameMap", messengerService.getIdToRoomNameMap());
        response.put("empMap", messengerService.getIdToENameMap());
        response.put("chatMessageList", chatMessageList);
        response.put("chatRoomId", id);
        System.out.println(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> createRoom(@RequestBody List<String> userIds,
                                                              @AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();

        boolean result = messengerService.createChatRoom(userIds, empId);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", result);
        return ResponseEntity.ok(response); // JSON 형식으로 반환
    }

    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        messengerService.deleteChatRoom(id);
        return ResponseEntity.ok("Chat room deleted successfully!");
    }

    // 메시지 전송 (STOMP 사용)
    @MessageMapping("/chatRoom/{chatRoomId}")
    public void sendMessage(@RequestBody ChatMessage message,
                            @DestinationVariable Long chatRoomId) {

        messengerService.saveMessage(message); // 메시지 저장
    }

    @GetMapping("/chatRoom/{chatRoomId}/unreadCount/{userId}")
    public int getUnreadMessageCount(@PathVariable Long chatRoomId, @PathVariable String userId) {
        return messengerService.getUnreadMessageCount(chatRoomId, userId);
    }

    @PostMapping("/chatRoom/{chatRoomId}/read/{userId}")
    public void updateLastReadMessageId(@PathVariable Long chatRoomId, @PathVariable String userId) {
        messengerService.updateLastReadMessageId(chatRoomId, userId);
    }
}