package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class EmailVerificationProcessAction implements Action {
	public static final String gmailid="unite240504";
	public static final String gmailpass="vziy btkv bgjb oiam";
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
//		resp.setContentType("text/html;charset=utf-8");
//		PrintWriter out= resp.getWriter();
//		
//		String authenCode = null;
//		String sender = req.getParameter("sender");
//		String receiver = req.getParameter("receiver");
//		String content = req.getParameter("content");
//		
//		String server = "smtp.gmail.com";
//		int port = 587;
//		try {
//			// 인증번호 생성
//       	    authenCode = makeAuthenticationCode();
//			
//			//서버 정보를 Properties 객체에 저장합니다.
//			Properties properties = new Properties();
//			
//			//SMTP 서버 정보 설정
//			//네이버일 경우 smtp.naver.com
//			properties.put("mail.smtp.host",server);
//			properties.put("mail.smtp.port",port);
//			properties.put("mail.smtp.starttls.enable","true");
//			
//			//이메일 전송 시 보안을 위해 사용하는 SSL 프로토콜을 설정합니다. 여기서는 TLS 1.2를 사용하도록 지정
//			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//			
//			//위에서 설정한 SMTP 서버 정보를 기반으로 세션을 생성합니다.
//			Session s = Session.getInstance(properties);
//			
//			//보내는 메일과 받는 주소를 설정합니다.
//			//Address senderAddress = new InternetAddress(sender);
//			Address receiverAddress = new InternetAddress(receiver);
//			
//			//메일을 보내기 위한 정보를 입력하기 위해 Message객체를 생성합니다.
//			Message message = new MimeMessage(s);
//			
//			//보내는 메일의 내용이 한글일 경우 깨지지 않도록 content-type을 지정합니다.
//			message.setHeader("content-type","text/html;charset=UTF-8");
//			
//			//보내는 메일 주소 정보를 설정합니다.
//			//message.setFrom(senderAddress);
//			// 발신자 설정
//		    message.setFrom(new InternetAddress(gmailid, "Unite"));
//			
//			//수신자 추가합니다.
//			message.addRecipient(Message.RecipientType.TO, receiverAddress);
//			
//			//제목 정보를 설정합니다.
//			message.setSubject("Unite :: 인증번호 메일입니다.");
//			
//			//내용의 정보를 설정합니다.
//			message.setContent("비밀번호 변경 인증번호는 [ "+authenCode+ " ] 입니다.", "text/html;charset=UTF-8");
//			
//			//보내는 날짜를 설정합니다.
//			message.setSentDate(new java.util.Date());
//			
//			//SMTP 서버에 연결하기 위한 Transport 객체를 생성합니다.
//			Transport transport = s.getTransport("smtp");
//			
//			//발신자의 이메일 아이디와 비밀번호로 연결합니다.
//			transport.connect(server, gmailid, gmailpass);
//			
//			//설정된 이메일 메시지를 수신자에게 전송합니다.
//			transport.sendMessage(message, message.getAllRecipients());
//			
//			//연결을 종료합니다.
//			transport.close();
//			
//			out.println("<h3>메일이 정상적으로 전송되었습니다.</h3>");
//		}catch (Exception e) {
//			out.println("SMTP 서버가 잘목 설정되었거나, 서비스에 문제가 있습니다.");
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	private String makeAuthenticationCode() {
//		
		return null;
	}

}
