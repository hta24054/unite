package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {
    List<Notice> getAllNotice();

    List<Notice> getAliveNotice();

    int insertNotice(Notice notice);

    int updateNotice(Notice notice);

    int deleteNoticeById(Long noticeId);
}
