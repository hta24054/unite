package com.hta2405.unite.action.attend;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static com.hta2405.unite.util.EmpUtil.isHrDept;

public class AttendVacationSetCountAction implements Action {
    private final EmpDao empDao = new EmpDao();

    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String empId = (String) req.getSession().getAttribute("id");
        Emp emp = empDao.getEmpById(empId);
        String status = "success";

        if (!isHrDept(emp)) {
            status = "fail";
        }

        int result = empDao.updateAllEmpVacCount();
        if (result == 0) {
            status = "fail";
        }
        System.out.println(4);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        resp.setContentType("application/json;UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(jsonObject);
        System.out.println(5);
        return null;
    }
}
