package com.hta2405.unite.action.emp;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.JsonObject;
import com.hta2405.unite.action.Action;
import com.hta2405.unite.action.ActionForward;
import com.hta2405.unite.dao.EmpDao;
import com.hta2405.unite.dto.Emp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


public class EmpSendAuthenCodeAction implements Action {
	private final String gmailid="unite240504";
	private final String gmailpass="vziy btkv bgjb oiam";
	private final String server = "smtp.gmail.com";
	
	@Override
	public ActionForward execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		String checkId = (String) session.getAttribute("checkId");
		session.setMaxInactiveInterval(5*60);//세션 유효시간 5분 갱신
		session.removeAttribute("email");
		
		String receiverName = req.getParameter("name");
		String receiverEmail = req.getParameter("email");
		
		//dao를 사용해 이름과 이메일이 같은지 비교
		EmpDao dao = new EmpDao();
		Emp emp = dao.getEmpById(checkId);
		
		JsonObject object = new JsonObject();
		String authenCode = "empty";
		
		if(emp == null || !emp.getEmail().equals(receiverEmail) || !emp.getEname().equals(receiverName)) {
			System.out.println("불일치");
			// 회원 정보가 일치하지 않은 경우
			object.addProperty("message", "회원 정보가 존재하지 않습니다.\n이름과 이메일을 다시 한번 확인해 주세요");
			
		} else {
			System.out.println("일치");
			/* 메일 전송 */
			authenCode = sendEmail(receiverEmail);
			System.out.println(authenCode);
			
			//위에서 request로 담았던 것을 JsonObject에 담습니다.
			object.addProperty("authenCode", authenCode);//{"page": 변수 page의 값} 형식으로 저장
			object.addProperty("message", "인증번호를 발송했습니다.\n인증번호가 오지 않으면 입력하신 정보를 확인해 주세요");
			
		}
		resp.setContentType("application/json;charset=utf-8");
		resp.getWriter().print(object);
		System.out.println(object.toString());
		return null;
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
	
	//인증번호 만들기
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
