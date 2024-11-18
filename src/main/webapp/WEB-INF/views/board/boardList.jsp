<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style>
 select.form-control{
 	width: auto;
 	margin-bottom: 2em;
 	display: inline-block;
 }
 
 .rows{
 	text-align: right;
 }
 
 .gray{
 	color: gray;
 }
 
 .tr-post:hover{
 	background-color: rgba(0, 0, 0, .05);
 	cursor: pointer;
 }
 
 .lineImg{
 	width: 13px;
    margin-bottom: 2px;
 }
 
 body > div > table > thead > tr:nth-child(2) > th:nth-child(1){width:8%}
 body > div > table > thead > tr:nth-child(2) > th:nth-child(2){width:50%}
 body > div > table > thead > tr:nth-child(2) > th:nth-child(3){width:14%}
 body > div > table > thead > tr:nth-child(2) > th:nth-child(4){width:17%}
 body > div > table > thead > tr:nth-child(2) > th:nth-child(5){width:11%}
</style>
<script src="${pageContext.request.contextPath}/js/boardList.js"></script>
</head>
<body>
	<input type="hidden" id="boardName2" value="${boardName2}"><%--view.js에서 사용하기위해 추가합니다. --%>
	<div class="container">
		<%--게시글이 있는 경우 --%>
		  <c:if test="${listCount > 0}">
		    <div class="rows">
		    	<span>줄보기</span>
		    	<select class="form-control" id="viewCount">
		    		<option value="1">1</option>
		    		<option value="3">3</option>
		    		<option value="5">5</option>
		    		<option value="7">7</option>
		    		<option value="10" selected>10</option>
		    	</select>
		    </div>
		    <table class="table">
		    	<thead>
		    		<tr>
		    			<th colspan="4"></th>
		    			<th colspan="1">
		    				<span>글 개수 : ${listCount}</span>
		    			</th>
		    		</tr>
		    		<tr>
		    			<th><div>번호</div></th>
		    			<th><div>제목</div></th>
		    			<th><div>작성자</div></th>
		    			<th><div>날짜</div></th>
		    			<th><div>조회수</div></th>
		    		</tr>
		    	</thead>
		    	<tbody>
		    	<c:set var="num" value="${listCount-(page-1)*limit}"/>
		    	<c:forEach var="p" items="${postList}">
		    		<tr class="tr-post"
		    			data-page="${p.postId}">
		    			<td><%--번호 --%>
		    				<c:out value="${num}"/><%-- num 출력 --%>
		    				<c:set var="num" value="${num-1}"/><%-- num=num-1; 의미 --%>
		    			</td>
		    			<td><%--제목 --%>
		    				<div>
		    					<%-- 답변글 제목앞에 여백 처리 부분 --%>
		    					<c:if test="${p.postReLev>0}"> <%-- 답글인 경우 --%>
			    					<c:forEach var="a" begin="0" end="${p.postReLev*2}" step="1">
			    					&nbsp;
			    					</c:forEach>
			    					<img class="lineImg" src='/unite/image/postLine.png'>
		    					</c:if>
		    					
		    					<input type="hidden" id="${p.postId}" value="${p.postId}"><%--view.js에서 사용하기위해 추가합니다. --%>

	    						<c:if test="${p.postSubject.length()>=20}">
	    							<c:out value="${p.postSubject.substring(0,20)}..."/>
	    						</c:if>
	    						<c:if test="${p.postSubject.length()<20}">
	    							<c:out value="${p.postSubject}"/>
	    						</c:if>
		    					[${p.postCommentCnt}]
		    				</div>
		    			</td>
		    			<td><div>${empMap[p.postWriter]}</div></td>
		    			<td><div>${p.getFormattedPostDate()}</div></td>
		    			<td><div>${p.postView}</div></td>
		    		</tr>
		    	</c:forEach>
		    	</tbody>
		    </table>
		    
		    <div class="center-block">
		    	<ul class="pagination justify-content-center">
		    		<li class="page-item">
		    			<a class="page-link ${page <= 1 ? 'gray' : ''}"
		    				data-page="${page > 1 ? 1 : ''}">
		    			&lt;&lt;
		    			</a>
		    		</li>
		    		<li class="page-item">
		    			<a class="page-link ${page <= 1 ? 'gray' : ''}"
		    				data-page="${page > 1 ? page - 1 : ''}">
		    			&lt;
		    			</a>
		    		</li>
		    		<c:forEach var="a" begin="${startPage}" end="${endPage}">
		    			<li class="page-item ${a == page ? 'active' : ''}">
			    			<a class="page-link"
			    				data-page="${a == page ? '' : a}">${a}</a>
		    			</li>
		    		</c:forEach>
		    		<li class="page-item">
		    			<a class="page-link ${page >= maxPage ? 'gray' : ''}" 
						   	data-page="${page < maxPage ? page + 1 : ''}">
						   	&gt;
						</a>
		    		</li>
		    		<li class="page-item">
		    			<a class="page-link ${page < maxPage ? '' : 'gray'}" 
						   	data-page="${page < maxPage ? maxPage : ''}">
						   	&gt;&gt;
						</a>
		    		</li>
		    	</ul>
		    </div>
		    
		  </c:if><%-- <c:if test="${listcount > 0}"> end --%>
		  
		  <%-- 게시글이 없는 경우 --%>
		  <c:if test="${listCount==0}">
		  	<h3 style = "text-align: center">등록된 글이 없습니다.</h3>
		  </c:if>
		  
	</div> <%-- <div class="container"> end --%>
</body>
</html>