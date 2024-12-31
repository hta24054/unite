package com.hta2405.unite.service;

import com.hta2405.unite.domain.Notice;
import com.hta2405.unite.mybatis.mapper.NoticeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper) {
        this.noticeMapper = noticeMapper;
    }

    public int addNotice(Notice notice) {
        return noticeMapper.insertNotice(notice);
    }

    public List<Notice> getAllNotice() {
        return noticeMapper.getAllNotice();
    }

    public int updateNotice(Notice notice) {
        return noticeMapper.updateNotice(notice);
    }

    public int deleteNotice(Long noticeId) {
        return noticeMapper.deleteNoticeById(noticeId);
    }
}
