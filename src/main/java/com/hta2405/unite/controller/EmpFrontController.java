package com.hta2405.unite.controller;

import com.hta2405.unite.action.emp.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/emp/*")
public class EmpFrontController extends AbstractFrontController {
    @Override
    public void init() throws ServletException {
        actionMap.put("/login", EmpLoginAction::new);
        actionMap.put("/loginProcess", EmpLoginProcessAction::new);
        actionMap.put("/logout", EmpLogoutAction::new);
        actionMap.put("/pwInquiry", EmpPwInquiryAction::new);
        actionMap.put("/pwInquiryProcess", EmpPwInquiryProcessAction::new);
        actionMap.put("/emailVerification", EmpEmailVerificationAction::new);
        actionMap.put("/emailVerificationProcess", EmpEmailVerificationProcessAction::new);
        actionMap.put("/sendAuthenCode", EmpSendAuthenCodeAction::new);
        actionMap.put("/changePw", EmpChangePwAction::new);
        actionMap.put("/changePwProcess", EmpChangePwProcessAction::new);
        actionMap.put("/empTree", EmpTreeAction::new);
        actionMap.put("/profile-image", EmpProfileImageAction::new);
        actionMap.put("/search", EmpSearchAction::new);
    }
}
