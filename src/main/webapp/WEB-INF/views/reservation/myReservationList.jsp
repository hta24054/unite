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
                                    <!-- <th>상세보기/취소</th> -->
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
                                        	<button type="button" class="btn btn-secondary">예약 취소</button>
                                        </td>
                                        <!-- 
                                        <td class="d-flex">
                                            <button type="button" class="btn btn-info" 
							                    data-toggle="modal" 
							                    data-reservation-id="${item.reservation.reservationId}">
							                    상세 보기
							                </button>
                                            <button type="button" class="btn btn-secondary">예약 취소</button>
                                        </td>
                                        -->
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
	
	<!-- 예약 상세보기 모달 -->
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
	       			    <input type="hidden" id="reservation_id" name="reservation_id" value="${reservation_id}"> 
						<input type="hidden" id="emp_id" name="emp_id" value="${id}">
					<!-- data -->
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 예약 상세보기 모달 -->
	
	<script>
		// 예약 상세보기 버튼 클릭 이벤트
		$(".btn-info").off("click").on("click", function(e) {
			e.preventDefault();
			const reservationId = $(this).data("reservation-id");
			
			 console.log("reservationId", reservationId)
			 console.log("emp_id", $("#emp_id").val() )
	
		    $.ajax({
		    	url: '${pageContext.request.contextPath}/reservation/myReservationDetail',
		        method: 'GET',
		        data: { 
		        	reservation_id: reservationId, 
		        	emp_id: $("#emp_id").val() 
		        },
		        dataType: 'json',
		        success: function (data) {
		        	 console.log("data", data); 
		        	 console.log("Resource Type:", data.resourceType); 
		        	 
		        	 let resourceInfoHtml = data.resourceInfo ? "<li>자원정보: " + data.resourceInfo + "</li>" : '';
		        	
		        	 $('#reservationDetailModal .modal-body').html(
        			    "<ul>" +
        			        "<li>분류명: " + data.resourceType + "</li>" +
        			        "<li>자원명: " + data.resourceName + "</li>" +
        			         resourceInfoHtml +
        			        "<li>시작일시: " + data.reservationStart + "</li>" +
        			        "<li>종료일시: " + data.reservationEnd + "</li>" +
        			        "<li>예약자: " + $("#emp_id").val() + "</li>" +
        			        "<li>사용용도: " + data.reservationInfo + "</li>" +
        			    "</ul>"
        			);

	
		            // 모달 표시
		            $('#reservationDetailModal').modal('show');
		        },
		        error: function (error) {
		            alert('예약 정보 불러오기 실패.');
		        }
		    });
		});
	</script>
</body>
</html>