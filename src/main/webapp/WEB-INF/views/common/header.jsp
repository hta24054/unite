<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <%--<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script> --%>
    <script src="${pageContext.request.contextPath }/js/jquery-3.7.1.js"></script> 
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
	<style>
		body > nav.navbar{justify-content: center;} /*중앙 정렬*/
		.dropdown-menu{min-width: 0rem;}
		.navbar{
			background: rgb(51, 68, 102);
			margin-bottom: 3em;
			padding-right: 3em;
		}
		.navbar-dark .navbar-nav .nav-link{color: rgb(255, 255, 255);}
		.a1 {
		    transition: all 0.3s ease; /* 부드러운 전환 효과 */
		}
		
		.a1:hover,
		.a1.active {
		    font-size: 1.2em; /* 글자 크기 증가 */
		    font-weight: bold; /* 글자 굵게 */
		}
				
		textarea{resize: none;}
		.nav-item1{position: absolute; left: 15px; top: 2px;}
		.nav-item2{position: absolute; right: 80px; top: 5px;}
		.nav-item3{position: absolute; right: 10px; top: 0px;}
	</style>
	<%--필터가 없는 경우 필요  --%>
	<%--<c:if test="${empty id }">
		<script>
			location.href = "${pageContext.request.contextPath}/members/login";
		</script>
	</c:if>--%>

	<%--<c:if test="${!empty id }"> --%>
		<nav class="navbar navbar-expand-sm right-block navbar-dark">
			<ul class="navbar-nav">
			<%--
				<li class="nav-ite">
					<a class="nav-link" href="${pageContext.request.contextPath }/#">홈</a>
				</li>
			 --%>
				<li class="nav-item1">
					<a class="nav-link" href="${pageContext.request.contextPath}/emp/home"><img src="${pageContext.request.contextPath}/image/logo_header.png "style="width:80px;"></a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/attend">&nbsp;근태관리&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/empInfo/view">&nbsp;인사정보&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/addressBook/view">&nbsp;주소록&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/schedule/calender">&nbsp;캘린더&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/board">&nbsp;게시판&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="#">&nbsp;전자문서&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="${pageContext.request.contextPath}/project/main">&nbsp;프로젝트&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="#">&nbsp;자원예약&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="#">&nbsp;메신저&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="#">&nbsp;마이페이지&nbsp;</a>
				</li>
				<li class="nav-item">
					<a class="nav-link a1" href="#">&nbsp;관리자&nbsp;</a>
				</li>
				<li class="nav-item2">
					<a class="nav-link" href="#"><img src="${pageContext.request.contextPath}/image/profile_white.png" style="width:30px;"></a>
				</li>
				<li class="nav-item3" style="line-height: 10px;">
					<%--${.name} --%><span class="nav-link">${id}님</span><a class="nav-link" href="${pageContext.request.contextPath}/emp/logout">로그아웃</a>
				</li>
				<%--<c:if test="${id=='admin' }">
				Drop down
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">관리자</a>
						<div class="dropdown-menu">
							<a class="dropdown-item" href="${pageContext.request.contextPath }/members/list">회원정보</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath }/boards/list">게시판</a>
						</div>
					</li>
				</c:if>	 --%>
			</ul>
		</nav>
	<%--</c:if> --%>