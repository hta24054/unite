<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Insert title here</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/css/view.css" type="text/css">
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
</head>
<body>
	<input type="hidden" id="loginid" value="${task.memberId }" name="loginid"><%--view.js에서 사용하기 위해 --%>
	<div class="container">
		<table class="table">
			<tr>
				<th colspan="2">MVC 게시판 - view 페이지</th>
			</tr>
			<tr>
				<td><div>글쓴이</div></td>
				<td><div>${task.memberName}</div></td>
			</tr>
			<tr>
				<td><div>제목</div></td>
				<td><c:out value="${task.projectTitle}"/></td>
			</tr>
			<tr>
				<td><div>내용</div></td>
				<td style="padding-right: 0px"><textarea class="form-control" rows="5" readOnly>${task.projectContent}</textarea></td>
			</tr>
			
			<tr>
				<td colspan="2" class="center">
					<c:if test="${boarddata.board_name == id || id == 'admin' }">
						<a href="modify?num=${boarddata.board_num }">
							<button class="btn btn-info">수정</button>
						</a>
						<%-- href의 주소를 #으로 설정 --%>
						<a href="#">
							<button class="btn btn-danger" data-toggle="modal" data-target="#myModal">삭제</button>
						</a>
					</c:if>
					<a href="list">
						<button class="btn btn-warning">목록</button>
					</a>
					<a href="reply?num=${boarddata.board_num }">
						<button class="btn btn-success">답변</button>
					</a>
				</td>
			</tr>
		</table>	
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<form name="deleteForm" action="delete" method="post">
						<%--http://localhost:8088/Board_Ajax/boards/detail?num=22 주소를 보면
						num을 파라미터로 넘기고 있다. 이 값을 가져와서 ${param.num{를 사용 또는 ${boarddata.board_num --%>
							<input type="hidden" name="num" value="${param.num }" id="comment_board_num">
							<div class="form-group">
								<label for="board_pass">비밀번호</label>
								<input type="password" class="form-control" placeholder="Enter password" name="board_pass" id="board_pass">
							</div>
							<button type="submit" class="btn btn-primary">전송</button>
							<button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="comment-area">
			<div class="comment-head">
				<h3 class="comment-count">
					댓글 <sup id="count"></sup> <%--윗첨자(superscript) --%>
				</h3>
				<div class="comment-order">
					<ul class="comment-order-list">
					</ul>
				</div>
			</div>
			<ul class="comment-list">
			</ul>
			<div class="comment-write">
				<div class="comment-write-area">
					<b class="comment-write-area-name">${id }</b> 
					<span class="comment-write-area-count">0/200</span>
					<textarea placeholder="댓글을 남겨보세요" rows="1" class="comment-write-area-text" maxlength="200"></textarea>
						
				</div>
				<div class="register-box">
					<div class="button btn-cancel">취소</div> <%--댓글의 취소는 display:none, 등록만 보이게 --%>
					<div class="button btn-register">등록</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
























