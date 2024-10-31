package com.hta2405.unite.action;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface Action {
    ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException;
}
