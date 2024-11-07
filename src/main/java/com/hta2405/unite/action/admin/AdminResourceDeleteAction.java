package com.hta2405.unite.action.admin;

import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ResourceDao;
import com.hta2405.unite.util.CommonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AdminResourceDeleteAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String resourceIdsParam = req.getParameter("resourceIds");

        if (resourceIdsParam == null) {
            CommonUtil.alertAndGoBack(resp, "삭제할 자원이 선택되지 않았습니다.");
            return null;
        }

        List<Long> resourceIds = Arrays.stream(resourceIdsParam.split(","))
                .map(Long::parseLong)
                .toList();

        int result = new ResourceDao().deleteResources(resourceIds);
        if (result != resourceIds.size()) {
            CommonUtil.alertAndGoBack(resp, "삭제 중 오류가 발생했습니다.");
            return null;
        }

        req.getSession().setAttribute("message", "자원 삭제를 성공하였습니다.");
        return new ActionForward(true, req.getContextPath() + "/admin/resource");
    }
}
