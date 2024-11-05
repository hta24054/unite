package com.hta2405.unite.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;


public class EmpEmailVerificationProcessAction implements Action {
	private final String gmailid="unite240504";
	private final String gmailpass="vziy btkv bgjb oiam";
	private final String server = "smtp.gmail.com";
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ActionForward forward = new ActionForward();
		
		/* 브라우저에서 건너온 세션 확인 
		 * 로그인 상태에선 접근 못하게 설정 */
		HttpSession session = req.getSession(true);
		if((String)session.getAttribute("id") != null) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<script>");
			out.println("alert('접근 권한이 없습니다.');");
			out.println("location.href='../emp/home';");
			out.println("</script>");
		}
		
		
		String emailCheck = (String) req.getSession().getAttribute("email");
		String receiverName = req.getParameter("name");
		String receiverEmail = req.getParameter("email");
		
		//dao를 사용해 이름과 이메일이 같은지 비교
		EmpDao dao = new EmpDao();
		Emp emp = dao.getEmpByNameAndEmail(receiverName,receiverEmail);
		
		if(emp == null || !emp.getEmail().equals(emailCheck)) {
			// 회원 정보가 일치하지 않은 경우
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.println("<script>");
			out.println("alert('회원 정보가 존재하지 않습니다.');");
			out.println("history.back(-1);");
			out.println("</script>");
			return null;
		} else {
			System.out.println("일치");
			/* 메일 전송 */
			String authenCode = sendEmail(receiverEmail);
			
			/* 포워딩 처리 */
			session.setAttribute("authenCode", authenCode);
			session.setAttribute("id", emp.getEmpId());

			System.out.println(authenCode);
			forward = new ActionForward();
			forward.setPath("../emp/changePw");
			forward.setRedirect(true);
			return forward;
		}
	}

	//이메일 발송
	public String sendEmail(String receiverEmail){
		String authenCode = null;
		System.out.println("sendEmail");
		try {
			// 인증번호 생성
       	    authenCode = makeAuthenticationCode();
			
			//서버 정보를 Properties 객체에 저장합니다.
			Properties properties = new Properties();
			
			//SMTP 서버 정보 설정
			properties.put("mail.smtp.host",server);
			properties.put("mail.smtp.port",587);	//포트번호
			properties.put("mail.smtp.starttls.enable","true");
			
			//이메일 전송 시 보안을 위해 사용하는 SSL 프로토콜을 설정합니다. 여기서는 TLS 1.2를 사용하도록 지정
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			
			//위에서 설정한 SMTP 서버 정보를 기반으로 세션을 생성합니다.
			Session s = Session.getInstance(properties);
			
			//받는 주소를 설정합니다.
			Address receiverAddress = new InternetAddress(receiverEmail);
			System.out.println(s!=null);
			//메일을 보내기 위한 정보를 입력하기 위해 Message객체를 생성합니다.
			Message message = new MimeMessage(s);
			
			//보내는 메일의 내용이 한글일 경우 깨지지 않도록 content-type을 지정합니다.
			message.setHeader("content-type","text/html;charset=UTF-8");
			
			// 발신자 설정
		    message.setFrom(new InternetAddress(gmailid, "Unite"));
			
			//수신자 추가합니다.
			message.addRecipient(Message.RecipientType.TO, receiverAddress);
			
			//제목 정보를 설정합니다.
			message.setSubject("Unite :: 인증번호 메일입니다.");
			
			//내용의 정보를 설정합니다.
			message.setContent("비밀번호 변경 인증번호는 [ "+authenCode+ " ] 입니다.", "text/html;charset=UTF-8");
			
			//보내는 날짜를 설정합니다.
			message.setSentDate(new java.util.Date());
			
			//SMTP 서버에 연결하기 위한 Transport 객체를 생성합니다.
			Transport transport = s.getTransport("smtp");
			
			//발신자의 이메일 아이디와 비밀번호로 연결합니다.
			transport.connect(server, gmailid, gmailpass);
			
			//설정된 이메일 메시지를 수신자에게 전송합니다.
			transport.sendMessage(message, message.getAllRecipients());
			
			//연결을 종료합니다.
			transport.close();
			
			System.out.println("메일 전송 완료");
			//out.println("메일이 정상적으로 전송되었습니다.");
		}catch (Exception e) {
			System.out.println("메일 전송 오류 발생");
			//out.println("SMTP 서버가 잘목 설정되었거나, 서비스에 문제가 있습니다.");
			e.printStackTrace();
		}
		System.out.println("sendEmail end");
		return authenCode;
	}
	
	private static String makeAuthenticationCode() throws Exception {
		
		int pwdLength = 8;
		final char[] pwdTable = { 
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };
		
		// System.currentTimeMillis(): 중복 방지 처리
		Random ran = new Random(System.currentTimeMillis());
		
		StringBuffer bf = new StringBuffer();
		for(int i=0; i<pwdLength; i++) {
			bf.append(pwdTable[ran.nextInt(pwdTable.length)]);
			
		}
		
		return bf.toString();
	}

}
