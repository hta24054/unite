package com.hta2405.unite.controller.api;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.dto.ChatRoomDTO;
import com.hta2405.unite.service.MessengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
        System.out.println("chatRoomDTOList: " + chatRoomDTOList);
        response.put("chatRoomDTOList", chatRoomDTOList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chatRooms/{userId}")
    public List<Long> getChatRoomsForUser(@PathVariable String userId) {
        return messengerService.getChatRoomsForUser(userId);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<HashMap<String, Object>> getRoomById(@PathVariable Long id,
                                                               @AuthenticationPrincipal UserDetails user) {
        List<ChatMessage> chatMessageList = messengerService.getChatMessageById(id);
        List<String> userIds = messengerService.getMembersByRoomId(id);
        ChatRoom chatRoom = messengerService.getChatRoomById(id);

        HashMap<String, Object> response = new HashMap<>();
        response.put("messengerNameMap", messengerService.getIdToRoomNameMap(user.getUsername()));
        response.put("empMap", messengerService.getIdToENameMap());
        response.put("chatMessageList", chatMessageList);
        response.put("chatRoom", chatRoom);
        response.put("userIds", userIds);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rooms")
    public ResponseEntity<HashMap<String, Object>> createRoom(@RequestBody List<String> userIds,
                                                              @AuthenticationPrincipal UserDetails user) {
        String empId = user.getUsername();

        HashMap<String, Object> map = messengerService.createChatRoom(userIds, empId);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", map.get("status"));
        response.put("chatRoomId", map.get("chatRoomId"));
        return ResponseEntity.ok(response); // JSON 형식으로 반환
    }

//    @DeleteMapping("/rooms/{id}")
//    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
//        messengerService.deleteChatRoom(id);
//        return ResponseEntity.ok("Chat room deleted successfully!");
//    }

    // 메시지 전송 (STOMP 사용)
    @MessageMapping("/chatRoom/{chatRoomId}")
    public void sendMessage(@RequestBody ChatMessage message,
                            @DestinationVariable Long chatRoomId) {

        System.out.println("asdasd message = " + message + "chatRoomId = " + chatRoomId);

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

    @PostMapping("/chatRoom/{chatRoomId}/rename")
    public ResponseEntity<HashMap<String, Object>> renameChatRoomName(@PathVariable Long chatRoomId,
                                                                      @RequestBody String chatRoomName,
                                                                      @AuthenticationPrincipal UserDetails user) {
        System.out.println(chatRoomName);
        Boolean check = messengerService.updateChatRoomName(chatRoomId, user.getUsername(), chatRoomName);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", check);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chatRoom/{chatRoomId}/roomOut")
    public ResponseEntity<HashMap<String, Object>> chatRoomOut(@PathVariable Long chatRoomId,
                                                               @AuthenticationPrincipal UserDetails user) {

        Boolean check = messengerService.removeMember(chatRoomId, user.getUsername());

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", check);
        return ResponseEntity.ok(response); // JSON 형식으로 반환
    }
}