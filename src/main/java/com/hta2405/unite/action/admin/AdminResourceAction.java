package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ResourceDao;
import com.hta2405.unite.dto.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdminResourceAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Resource> resourceList = new ResourceDao().getAllResource();
        req.setAttribute("resourceList", resourceList);
        return new ActionForward(false, "/WEB-INF/views/admin/resource.jsp");
    }
}
