<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>나의 예약목록</title> 
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-sm-3 px-5">
				<aside>
					<h3>자원예약</h3>
					<p>나의 예약 목록</p>
				</aside>
			</div>
			
			<div class="col-sm-9 px-5">
                <h3>나의 예약 목록</h3>
                
                <!-- 예약 목록이 있을 때 -->
                <c:if test="${not empty reservationList}">
                    <form action="${pageContext.request.contextPath}/reservation/myReservationList" method="get">
                        <table>
                            <thead>
                                <tr>
                                    <th>자원 유형</th>
                                    <th>자원 이름</th>
                                    <th>예약 시작</th>
                                    <th>예약 종료</th>
                                    <th>상세보기/취소</th>
                                </tr>
                            </thead> 
                            <tbody>
                                <c:forEach var="item" items="${reservationList}">
                                    <tr>
                                        <td>${item.resource.resourceType}</td>
                                        <td>${item.resource.resourceName}</td>
                                        <td>${item.reservation.reservationStart}</td>
                                        <td>${item.reservation.reservationEnd}</td>
                                        <td class="d-flex">
                                        	<button type="button" class="btn btn-info" data-toggle="modal" data-target="#reservationDetailModal">상세 보기</button>
                                            <button type="button" class="btn btn-secondary">예약 취소</button>
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
					
	            </div>
	        </div>
	    </div>
	</div>
	<!-- 예약 상세보기 모달 -->
</body>
</html>