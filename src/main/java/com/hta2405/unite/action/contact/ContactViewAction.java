package com.hta2405.unite.action.contact;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.ContactDao;
import com.hta2405.unite.dto.EmpDetails;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ContactViewAction implements Action {
	private ContactDao contactDao = new ContactDao();

	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String orderBy = req.getParameter("orderBy");

		if (orderBy == null || orderBy.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write("orderBy 파라미터가 누락되었습니다.");
			return null;
		}

		List<EmpDetails> contactList = contactDao.getAllContacts(orderBy);

		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
		String json = gson.toJson(contactList);
		resp.setContentType("application/json; charset=utf-8");
		resp.getWriter().write(json);

		return null;
	}
}
