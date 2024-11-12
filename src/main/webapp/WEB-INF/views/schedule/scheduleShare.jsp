<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>캘린더 - 공유 일정 등록</title> 
	<script src="${pageContext.request.contextPath}/js/scheduleShare.js"></script>
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
		
		.modal-header p {
			margin: 0;
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
		
		.modal-dialog {
			max-width: 700px;
		}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-2 px-5">
				<aside>
					<h3>캘린더</h3>
					<div>
						<p>&middot; 공유 일정 등록</p>
					</div>
				</aside>
			</div>
			<div class="col-sm-8 px-5">
				<h3>공유 일정 등록</h3>
				<div>
					<form name="scheduleShareEvent" method="post">
						<input type="hidden" id="schedule_id" name="schedule_id" value="${schedule_id}">
						<input type="hidden" id="emp_id" name="emp_id" value="${id}">
						
						<div class="form-group row">
							<label for="schedule_name" class="col-sm-2 col-form-label">일정명</label>
							<div class="col-sm-8">
			                    <input type="text" class="form-control" placeholder="일정명을 입력하세요" name="schedule_name" id="schedule_name">
			                </div>
						</div>
						
						<div class="form-group row custom-control custom-checkbox">
							<div class="col-sm-2">&nbsp;&nbsp;</div>
							<div class="col-sm-7">
								<input type="checkbox" name="allDay" id="allDay" class="custom-control-input" value="">
			             		<label for="allDay" class="custom-control-label">하루종일</label>
							</div>
						</div>
				        
						<div class="form-group row">
							<label for="startAt" class="col-sm-2 col-form-label">시작날짜/시간</label>
							<div class="col-sm-8">
								<input type="datetime-local" name="startAt" id="startAt" class="form-control">
							</div>
						</div>
						
						<div class="form-group row">
				            <label for="endAt" class="col-sm-2 col-form-label">종료날짜/시간</label>
				            <div class="col-sm-8">
								<input type="datetime-local" name="endAt" id="endAt" class="form-control">
							</div>
				        </div>
				        
          				<div class="form-group row">
          					<p class="col-sm-2">색상</p>
          					<div class="col-sm-8">
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
				        
				        <div class="form-group row">
				        	<p class="col-sm-2">참석자</p>
				        	<div class="col-sm-8">
				        		<button type="button" class="btn btn-info" data-toggle="modal" data-target="#scheduleShareModal">+ 참석자 선택</button>
				        	</div>
				        </div>
				        <div class="form-group row">
				        	<p class="col-sm-2">내용</p>
				        	<div class="col-sm-8">
				        		<textarea rows="10" name="description" id="description"></textarea>
				        	</div>
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
	
	<%-- modal 시작 --%>
	<div class="modal" id="scheduleShareModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header" style="align-items: center;">
					<p>조직도</p>
					<button type="button" class="btn btn-danger" data-dismiss="modal">X</button>
				</div>
				<div class="modal-body">
					<div>
						<jsp:include page="scheduleShareTree.jsp" />
					</div>
				</div>
				<div class="modal-footer">
					<div class="btn_wrap">
         				<button type="reset" class="btn btn-secondary">취소</button>
						<button type="submit" class="btn btn-info" id="btnSave">저장</button>
         			</div>
				</div>
			</div>
		</div>
	</div>
	<%-- modal end --%>
</body>
</html>