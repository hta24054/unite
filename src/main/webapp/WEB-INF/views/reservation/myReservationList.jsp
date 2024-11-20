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
                        <input type="hidden" id="emp_id" name="emp_id" value="${id}">
                        <input type="hidden" name="reservation_id" value="${reservation_id}">
                        
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
                                        <!-- 자원 정보 -->
                                        <td>${item.resource.resourceType}</td>
                                        <td>${item.resource.resourceName}</td>
                                        
                                        <!-- 예약 정보 -->
                                        <td>${item.reservation.reservationStart}</td>
                                        <td>${item.reservation.reservationEnd}</td>
                                        <td>
                                            <div>
                                                <button type="submit" formaction="detailPageServletURL">상세보기</button>
                                                <button type="submit" formaction="cancelReservationServletURL">취소</button>
                                            </div>
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
</body>
</html>