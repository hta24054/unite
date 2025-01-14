package com.hta2405.unite.service;

import com.hta2405.unite.domain.Contact;
import com.hta2405.unite.mybatis.mapper.ContactMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactMapper contactMapper;

    public List<Contact> getAllOuterContact(){
        return contactMapper.getAllOuterContactList();
    }

    public int addContact(Contact contact) {
        return contactMapper.insertContact(contact);
    }

    public int updateContact(Contact contact) {
        return contactMapper.updateContact(contact);
    }

    public int deleteContact(List<Long> list) {
        return contactMapper.deleteContact(list);
    }
}
