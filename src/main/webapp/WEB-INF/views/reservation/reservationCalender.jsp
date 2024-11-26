<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<jsp:include page="reservation_leftbar.jsp"/>
	<title>자원예약</title> 
	<jsp:include page="../common/fullcalendar.jsp" />
	<script src="${pageContext.request.contextPath}/js/reservation.js"></script>
	<style>
		.container-xxl {
		    display: flex;
		    flex-wrap: wrap;
		    margin: 0 auto;
		}
		
		.container-xxl > div {
		    flex: 1; 
		    max-width: 100%;
		    padding: 0 50px; 
		}
		
		#calendar {
		  max-width: 100%;
		  margin: 0 auto;
		}
		
		.btn_wrap {
			display: flex;
		    align-items: center;
		    justify-content: center;
		}
		
		.btn_wrap button {
			margin: 0 5px;
		}
		
		.deatail_list {
			list-style: none;
			padding: 0;
			margin: 0;
		}
		
		.deatail_list li + li {
			margin-top: 10px;
		}
	</style>
</head>
<body>
	<div class="container-xxl">
		<div>
			<div id="calendar"></div>
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
					            <label for="reservationInfo">
					            	사용용도 <sub id="charCount">0/100</sub>
					            </label>
					            <input type="text" name="reservationInfo" id="reservationInfo" class="form-control" maxlength="100">
					            
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
	
	<!-- 예약 상세정보 모달 -->
	<div class="modal" id="reservationDetailModal">
	    <div class="modal-dialog">
	        <div class="modal-content">
	            <div class="modal-header">
	                <h5 class="modal-title">예약 정보</h5>
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span aria-hidden="true">&times;</span>
	                </button>
	            </div>
	            <div class="modal-body">
					<!-- data 영역 -->
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 예약 상세정보 모달 -->
</body>
</html>