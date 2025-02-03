package com.hta2405.unite.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest request, Model model) {
        // 에러 정보 가져오기
        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        // 상태 코드와 메시지 가져오기
        Integer statusCode = (Integer) errorDetails.get("status");
        String errorMessage = (String) errorDetails.get("message");

        // 모델에 에러 정보 추가
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);

        // 커스텀 에러 페이지로 이동
        return "error/error";
    }
}
