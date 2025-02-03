package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.ChatMessage;
import com.hta2405.unite.domain.ChatRoom;
import com.hta2405.unite.domain.ChatRoomMember;
import com.hta2405.unite.dto.ChatRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessengerMapper {
    // ChatRoom 관련
    List<ChatRoomDTO> findAllRooms(String empId, boolean isHomeMessenger);

    List<ChatMessage> findMessageListById(Long chatRoomId, String userId);

    List<Map<String, Object>> getIdToRoomNameMap(String userId);

    void createRoom(ChatRoom chatRoom);

    void deleteRoom(Long chatRoomId);

    // ChatMessage 관련
    ChatMessage findLastMessageByRoomId(Long chatRoomId);

    List<ChatMessage> findMessagesByRoomId(Long chatRoomId);

    void saveMessage(ChatMessage chatMessage);

    // ChatRoomMember 관련
    List<String> findMembersByRoomId(Long chatRoomId);

    ChatRoom findChatRoomById(Long chatRoomId);

    int removeMember(Long chatRoomId, String userId);

    int insertRoomMember(List<ChatRoomMember> chatRoomMemberList, boolean newInvite);

    int getUnreadMessageCount(Long chatRoomId, String userId);

    void updateRecentMessageDate(Long chatRoomId);

    void updateLastReadMessageId(Long chatRoomId, String userId);

    ChatMessage findMessageById(Long chatMessageId);

    List<ChatRoomMember> getChatRoomsById(String userId);

    void updateChatRoomName(Long chatRoomId, String userId, String chatRoomName);

    void deleteMessageById(Long chatRoomId);

    int deleteMember(Long chatRoomId);
}
