package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ResourceDao;
import com.hta2405.unite.dto.Resource;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminResourceAddAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resource newResource = new Resource(null,
                req.getParameter("resourceType"),
                req.getParameter("resourceName"),
                req.getParameter("resourceInfo"),
                req.getParameter("resourceUsable").equals("가능"));
        int result = new ResourceDao().insertResource(newResource);
        if (result != 1) {
            CommonUtil.alertAndGoBack(resp, "자원 등록을 실패하였습니다.");
        }
        req.getSession().setAttribute("message", "자원 등록을 성공하였습니다.");
        return new ActionForward(true, req.getContextPath() + "/admin/resource");
    }
}
