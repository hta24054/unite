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
    List<ChatRoomDTO> findAllRooms(String empId);

    List<ChatMessage> findRoomById(Long chatRoomId);

    List<Map<String, Object>> getIdToRoomNameMap();

    void createRoom(ChatRoom chatRoom);

    void deleteRoom(Long chatRoomId);

    // ChatMessage 관련
    List<ChatRoomDTO> findLastMessagesByRoomIds(String empId);

    List<ChatMessage> findMessagesByRoomId(Long chatRoomId);

    void saveMessage(ChatMessage chatMessage);

    // ChatRoomMember 관련
    List<String> findMembersByRoomId(Long chatRoomId);

    void addMember(ChatRoomMember member);

    void removeMember(Long chatRoomMemberId);

    int insertRoomMember(List<ChatRoomMember> chatRoomMemberList);
}
