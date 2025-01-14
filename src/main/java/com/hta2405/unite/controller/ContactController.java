package com.hta2405.unite.controller;

import com.hta2405.unite.domain.Contact;
import com.hta2405.unite.dto.EmpListDTO;
import com.hta2405.unite.service.ContactService;
import com.hta2405.unite.service.DeptService;
import com.hta2405.unite.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contact")
@Slf4j
public class ContactController {

    private final EmpService empService;
    private final DeptService deptService;
    private final ContactService contactService;

    public ContactController(EmpService empService, DeptService deptService, ContactService contactService) {
        this.empService = empService;
        this.deptService = deptService;
        this.contactService = contactService;
    }

    @GetMapping("/inner")
    public ModelAndView showInnerContact(ModelAndView mv) {
        List<EmpListDTO> empList = empService.getEmpListDTO(deptService.getAllDept());
        String message = "사내 주소록입니다.";
        mv.addObject("title", "사내 주소록");
        mv.addObject("message", message);
        mv.addObject("empList", empList);
        mv.setViewName("/contact/innerContact");
        return mv;
    }

    @GetMapping("/outer")
    public String showOuterContact() {
        return "/contact/outerContact";
    }

    @GetMapping("/outer/list")
    @ResponseBody
    public Map<String, Object> getAllResource() {
        List<Contact> contactList = contactService.getAllOuterContact();

        // DataTables 가 요구하는 JSON 구조 생성
        Map<String, Object> response = new HashMap<>();
        response.put("data", contactList);
        return response;
    }
    @PostMapping("/outer")
    @ResponseBody
    public String insertContact(Contact contact){
        int result = contactService.addContact(contact);
        if (result != 1) {
            return "주소록 등록 실패";
        }
        return "주소록 등록 성공";
    }

    @PatchMapping("/outer")
    @ResponseBody
    public String updateContact(Contact contact){
        int result = contactService.updateContact(contact);
        if (result != 1) {
            return "주소록 수정 실패";
        }
        return "주소록 수정 성공";
    }

    @DeleteMapping("/outer")
    @ResponseBody
    public String deleteResource(@RequestParam("selectedIds[]") long[] selectedIds) {
        List<Long> list = Arrays.stream(selectedIds).boxed().toList();
        int result = contactService.deleteContact(list);
        if (result < 1) {
            return "자원 삭제 실패";
        }
        return "자원 삭제 성공";
    }
}
