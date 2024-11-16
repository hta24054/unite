package com.hta2405.unite.action.contact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ContactDao;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dao.DeptDao;
import com.hta2405.unite.dao.JobDao;
import com.hta2405.unite.dto.Emp;
import com.hta2405.unite.util.LocalDateAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class ContactViewAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long deptId = Long.parseLong(req.getParameter("deptId"));

            JsonObject jsonObject = new JsonObject();

            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

           
            List<Emp> empList = new ContactDao().getContactEmpByDeptId(deptId);
            JsonElement listToJson = gson.toJsonTree(empList);
            jsonObject.add("empList", listToJson);

            HashMap<Long, String> jobNameMap = new JobDao().getIdToJobNameMap();
            JsonElement mapToJson = gson.toJsonTree(jobNameMap);
            jsonObject.add("jobName", mapToJson);

            HashMap<Long, String> deptNameMap = new DeptDao().getIdToDeptNameMap();
            JsonElement mapToJsonDeptName = gson.toJsonTree(deptNameMap);
            jsonObject.add("deptName", mapToJsonDeptName);

            resp.setContentType("application/json;charset=utf-8");
            resp.getWriter().print(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error processing request");
        }
        return null;
    }
}
