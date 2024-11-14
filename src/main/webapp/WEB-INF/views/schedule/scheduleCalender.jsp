<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>캘린더 - 일정관리</title> 
	<script src='https://cdn.jsdelivr.net/npm/moment@2.27.0/min/moment.min.js'></script>
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@6.1.15/index.global.min.js'></script>
	<script src='https://cdn.jsdelivr.net/npm/@fullcalendar/moment@6.1.15/index.global.min.js'></script>
	<script src="${pageContext.request.contextPath}/js/calendar.js"></script>
	<style>
		.container {
			max-width: 1900px;
		}
		
		h3 {
			margin-bottom: 20px;
			color: rgb(51, 68, 102);
			font-size: 30px;
			font-weight: 600;
		}
		
		#calendar a {
			text-decoration: none;
			color: #000;
		}
		
		.fc-header-toolbar {
		    padding-top: 1em;    
		    padding-left: 1em;    
		    padding-right: 1em;
		}
		
		/* 일요일 날짜 빨간색 */
		.fc-day-sun a {
		  color: red !important;
		}
		
		/* 토요일 날짜 파란색 */
		.fc-day-sat a {
		  color: blue !important;
		}
		
		.color-group {
			display: flex;
		}
		
		.color-group > p {
			margin: 0 10px 0 0;
		}
		
		textarea {
			width: 100%;
			height: 100px;
			margin-bottom: 10px;
		}
		
		.btn_wrap {
			display: flex;
		    align-items: center;
		    justify-content: center;
		}
		
		.btn_wrap button {
			margin: 0 5px;
		}
		
		.modal-header p {
			margin: 0;
		}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-2 px-5">
				<aside>
					<h3>캘린더</h3>
					<button class="btn btn-info" data-toggle="modal" data-target="#scheduleModal">일정 등록</button>
					<div>
						<a href="${pageContext.request.contextPath}/schedule/scheduleShare">&middot; 공유 일정 등록</a>
					</div>
				</aside>
			</div>
			<div class="col-sm-10 px-5">
				<div id="calendar"></div>
			</div>
		</div>
	</div>
	
	<%-- 일정 등록 모달 --%>
	<div class="modal" id="scheduleModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">일정 등록</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
				</div>
				<div class="modal-body">
					<form name="scheduleEvent" method="post">
						<input type="hidden" id="schedule_id" name="schedule_id" value="${schedule_id}">
						<input type="hidden" id="emp_id" name="emp_id" value="${id}">
						<input type="hidden" id="share_emp" name="share_emp" value="${share_emp.join(',')}">
						
						<div class="form-group">
							<label for="schedule_name">일정명</label>
							<input type="text"
								   class="form-control" placeholder="일정명을 입력하세요"
								   name="schedule_name" id="schedule_name">
						</div>
						<div class="form-group custom-control custom-checkbox">
				             <input type="checkbox" name="allDay" id="allDay" class="custom-control-input" value="">
				             <label for="allDay" class="custom-control-label">하루종일</label>
				        </div>
						<div class="form-group">
							<label for="startAt">시작날짜/시간</label>
							<input type="datetime-local" name="startAt" id="startAt" class="form-control">
						</div>
						<div class="form-group">
				            <label for="endAt">종료날짜/시간</label>
				            <input type="datetime-local" name="endAt" id="endAt" class="form-control">
				        </div>
          				<div class="form-group">
          					<div class="color-group">
          						<p>색상</p>
					            <select name="bgColor" id="bgColor">
									<option value="#1e3a8a" style="color: #1e3a8a;">Blue100</option>
						            <option value="#1d4ed8" style="color: #1d4ed8;">Blue200</option>
						            <option value="#22d3ee" style="color: #22d3ee;">Blue300</option>
						            <option value="#16a34a" style="color: #16a34a;">Green100</option>
						            <option value="#84cc16" style="color: #84cc16;">Green200</option>
						            <option value="#dc2626" style="color: #dc2626;">Red100</option>
						            <option value="#f43f5e" style="color: #f43f5e;">Red200</option>
						            <option value="#facc15" style="color: #facc15;">Yellow</option>
								</select>
          					</div>
				        </div>
				        <div>
				        	<p>내용</p>
				        	<textarea rows="10" name="description" id="description"></textarea>
				        </div>
				        
          				<div class="btn_wrap">
          					<button type="reset" class="btn btn-secondary">취소</button>
							<button type="submit" class="btn btn-info" id="btnRegister">등록</button>
          				</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>