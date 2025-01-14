package com.hta2405.unite.mybatis.mapper;

import com.hta2405.unite.domain.Contact;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    List<Contact> getAllOuterContactList();

    int insertContact(Contact contact);

    int updateContact(Contact contact);

    int deleteContact(List<Long> list);
}
