<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>캘린더 일정관리</title>
	<style>
		.container {
			max-width: 1900px;
		}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-2">
				<aside>
					<h2>캘린더</h2>
					<button class="btn btn-info" data-toggle="modal"
							data-target="#scheduleModal">일정등록</button>
					<div>
						<a href="#">&middot; 공유 일정 등록</a>
					</div>
				</aside>
			</div>
			<div class="col-sm-10">
				<div>
					캘린더
				</div>
			</div>
		</div>

	</div>
	
	<%-- modal 시작 --%>
	<div class="modal" id="scheduleModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<form name="scheduleAddForm" action="scheduleAdd" method="post">
						<%--
							http://localhost:8088/Board_Ajax2/boards/detail?num=22
							주소를 보면 num을 파라미터로 넘기고 있습니다.
							이 값을 가져와서 ${param.num}을 사용 또는 ${boarddata.board_num}
						--%>
						<input type="hidden" name="num" value="${param.num}" 
						       id="scheduleId">
						<div class="form-group">
							<label for="board_pass">비밀번호</label>
							<input type="password"
								   class="form-control" placeholder="Enter password"
								   name="board_pass" id="board_pass">
						</div>
						<button type="submit" class="btn btn-primary">전송</button>
						<button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
					</form>
				</div>
			</div>
		</div>
	</div>
	<%-- modal end --%>
</body>
</html>