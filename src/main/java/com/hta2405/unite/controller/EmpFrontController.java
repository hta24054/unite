package com.hta2405.unite.controller;

import com.hta2405.unite.action.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/emp/*")
public class EmpFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/login", new EmpLoginAction());
        actionMap.put("/loginProcess", new EmpLoginProcessAction());
        actionMap.put("/logout", new EmpLogoutAction());
        actionMap.put("/pwInquiry", new EmpPwInquiryAction());
        actionMap.put("/pwInquiryProcess", new EmpPwInquiryProcessAction());
        actionMap.put("/emailVerification", new EmpEmailVerificationAction());
        actionMap.put("/emailVerificationProcess", new EmpEmailVerificationProcessAction());
        actionMap.put("/sendAuthenCode", new EmpSendAuthenCodeAction());
        actionMap.put("/changePw", new EmpChangePwAction());
        actionMap.put("/changePwProcess", new EmpChangePwProcessAction());
        actionMap.put("/empTree", new EmpTreeAction());
    }
}
