<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Insert title here</title>
	<jsp:include page="../common/header.jsp"/>
	<jsp:include page="project_leftbar.jsp"/>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/css/view.css" type="text/css">
	<script src="${pageContext.request.contextPath }/js/project_view.js"></script> 
	<script>
    	const contextPath = "${pageContext.request.contextPath}";
    	function submitModifyForm(taskNum, memberId) {
    	    document.getElementById('hiddenTaskNum').value = taskNum;
    	    document.getElementById('hiddenMemberId').value = memberId;
    	    document.getElementById('modifyForm').submit();
    	}
	    function submitForm(memberId) {
	        // memberId 값을 hidden input에 설정
	        document.getElementById('memberId').value = memberId;
	        // 폼을 제출
	        document.getElementById('postForm').submit();
	    }
    </script>
</head>
<body>
	<input type="hidden" id="loginid" value="${task.memberId }" name="loginid"><%--view.js에서 사용하기 위해 --%>
	<input type="hidden" name="num" value="${task.taskNum }" id="comment_board_num">
	<div class="container">
	
		<table class="table">
			<tr>
				<th colspan="2"><c:out value="${left}"/></th>
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
				<td><div>첨부파일</div></td>
					<%--파일을 첨부한 경우 --%>
					<c:if test="${!empty task.task_file_original}">
						<td><img src="${pageContext.request.contextPath }/img/down.png" width="10px">
							<a href="down?filename=${task.task_file_original }">${task.task_file_original }</a>
						</td>
					</c:if>
					<%--파일을 첨부하지 않은 경우 --%>
					<c:if test="${empty task.task_file_original }">
						<td></td>
					</c:if>
			</tr>
			<tr style="text-align: right; ">
				<td colspan="2" class="center" style="border-bottom-style: none;">
					<c:if test="${task.memberId == id || id == 'admin' }">
						<a href="javascript:void(0);" onclick="submitModifyForm('${task.taskNum}', '${task.memberId}')">
						    <button class="btn btn-info">수정</button>
						</a>
						<%-- href의 주소를 #으로 설정 --%>
						<a href="#">
							<button class="btn btn-danger" data-toggle="modal" data-target="#myModal">삭제</button>
						</a>
					</c:if>
					<a href="javascript:void(0);" onclick="submitForm('${task.memberId}')">
					    <button class="btn btn-warning">목록</button>
					</a>
				</td>
			</tr>
		</table>	
		<form id="postForm" action="${pageContext.request.contextPath}/projectb/list" method="POST" style="display:none;">
		    <input type="hidden" name="memberId" id="memberId">
		</form>
		<form id="modifyForm" action="modify" method="POST" style="display: none;">
		    <input type="hidden" name="taskNum" id="hiddenTaskNum">
		    <input type="hidden" name="memberId" id="hiddenMemberId">
		</form>
		<div class="modal" id="myModal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
						<form name="deleteForm" action="delete" method="post">
						<%--http://localhost:8088/Board_Ajax/boards/detail?num=22 주소를 보면
						num을 파라미터로 넘기고 있다. 이 값을 가져와서 ${param.num{를 사용 또는 ${boarddata.board_num --%>
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

























