<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>나의 예약목록</title>
	<style>
		.container {
			max-width: 1900px;
		}
	</style> 
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-2 px-5">
				<aside>
					<h3>자원예약</h3>
					<p>나의 예약 목록</p>
				</aside>
			</div>
			
			<div class="col-sm-10 px-5">
                <h3>나의 예약 목록</h3>
                
                <!-- 예약 목록이 있을 때 -->
                <c:if test="${not empty reservationList}">
                    <form action="${pageContext.request.contextPath}/reservation/myReservationList" method="get">
                    	<input type="hidden" id="emp_id" name="emp_id" value="${id}">
                        <table class="table table-bordered table-striped">
                            <thead>
                                <tr>
                                	<th>예약 번호</th>
                                    <th>자원 유형</th>
                                    <th>자원 이름</th>
                                    <th>예약 시작</th>
                                    <th>예약 종료</th>
                                    <th>사용 용도</th>
                                    <th>자원 정보</th>
                                    <th>예약 취소</th>
                                </tr>
                            </thead> 
                            <tbody>
                                <c:forEach var="item" items="${reservationList}">
                                    <tr>
                                    	<td>${item.reservation.reservationId}</td>
                                        <td>${item.resource.resourceType}</td>
                                        <td>${item.resource.resourceName}</td>
                                        <td>${item.reservation.reservationStart}</td>
                                        <td>${item.reservation.reservationEnd}</td>
                                        <td>${item.reservation.reservationInfo}</td>
                                        <td>${item.resource.resourceInfo}</td>                                        
                                        <td>
										    <button type="button" id="btnCancel" class="btn btn-secondary" data-reservation-id="${item.reservation.reservationId}">
										        예약 취소
										    </button>
										</td>
                                        
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </form>
                </c:if>
                
                <!-- 예약 목록이 없을 때 -->
			    <c:if test="${empty reservationList}">
			        <div class="text-center">
			        	예약 목록이 없습니다.
			        </div>
			    </c:if>
			</div>
		</div>
	</div>
	
	<script>
	 	// 예약 취소 버튼 클릭 시
	    $("#btnCancel").off("click").on("click", function (){
	        const reservationId = $(this).data("reservation-id");
	   
	        if (confirm("정말 취소하시겠습니까?")) {
	            $.ajax({
	            	url: "${pageContext.request.contextPath}/reservation/cancelReservation",
					type: "post",
			        dataType: "json",
	                data: { 
	                    reservation_id: reservationId, 
	                    emp_id: $("#emp_id").val()
	                },
	                success: function (data) {
	                    if (data === 1) {
	                        alert("예약이 취소되었습니다.");
	                        location.reload();
	                    } else {
	                        alert("예약 취소 실패");
	                    }
	                },
	                error: function (error) {
	                    alert("예약 취소 오류");
	                }
	            });
	        }
	    });
	</script>
</body>
</html>