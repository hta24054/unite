<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>자원예약</title> 
	<jsp:include page="../common/fullcalendar.jsp" />
	<script src="${pageContext.request.contextPath}/js/reservation.js"></script>
	
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
		
		.btn_wrap {
			display: flex;
		    align-items: center;
		    justify-content: center;
		}
		
		.btn_wrap button {
			margin: 0 5px;
		}
		
		ul, li {
			list-style: none;
			padding: 0;
			margin: 0;
		}
	</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-2 px-5">
				<aside>
					<h3>자원예약</h3>
					<button class="btn btn-info" data-toggle="modal" data-target="#reservationModal">예약 하기</button>
					<div>
						<a href="${pageContext.request.contextPath}/reservation/reservationList">&middot; 나의 예약 목록</a>
					</div>
				</aside>
			</div>
			<div class="col-sm-10 px-5">
				<div id="calendar"></div>
			</div>
		</div>
	</div>
	
	<%-- 자원 예약 모달 --%>
	<div class="modal" id="reservationModal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">예약 하기</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
				</div>
				<div class="modal-body">
					<form name="reservationEvent" method="post">
						<input type="hidden" id="reservation_id" name="reservation_id" value="${reservation_id}"> 
						<input type="hidden" id="emp_id" name="emp_id" value="${id}">						
				        
				        <div class="form-group custom-control custom-checkbox">
				             <input type="checkbox" name="allDay" id="allDay" class="custom-control-input" value="">
				             <label for="allDay" class="custom-control-label">종일</label>
				        </div>
				        
				        <div class="modify_area">
				        	<div class="form-group">
								<label for="startAt">시작날짜/시간</label>
								<input type="datetime-local" name="startAt" id="startAt" class="form-control">
							</div>
							<div class="form-group">
					            <label for="endAt">종료날짜/시간</label>
					            <input type="datetime-local" name="endAt" id="endAt" class="form-control">
					        </div>
					        <div class="form-group">
	          					<p>자원선택</p>
					            <div>
					            	<select name="resourceType" id="resourceType">
					            		<!-- <option value="">분류명을 선택하세요</option> -->
										<option value="">분류명</option>
										<c:forEach var="resource" items="${resourceList}">
	            							<option value="${resource.resourceType}">${resource.resourceType}</option>
										</c:forEach>
									</select>
									<select name="resourceName" id="resourceName">
										<option value="">자원명</option>
										<!-- <option value="">자원명 을 선택하세요</option> -->
								        <c:forEach var="resource" items="${resourceList}">
	            							<option value="${resource.resourceId}">${resource.resourceName}</option>
										</c:forEach>
								    </select>			            
					            </div>
					        </div>
					        <div class="form-group">
					            <label for="reservationInfo">사용용도</label>
					            <input type="text" name="reservationInfo" id="reservationInfo" class="form-control">
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
	<%-- 자원 예약 모달 --%>
</body>
</html>