package com.hta2405.unite.util;

import com.hta2405.unite.action.ActionForward;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class CommonUtil {
    public static ActionForward alertAndGoBack(HttpServletResponse resp, String message) throws IOException {
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.print("<script>");
        out.print("alert('" + message + "');");
        out.print("location.href = history.back();");
        out.print("</script>");
        out.close();
        return null;
    }
}
