<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>아코디언 메뉴</title>
  <style>
    /* 기본 스타일 설정 */
    .accordion {
      height: calc(100vh - 50px); 
	  border-right: 2px solid rgb(51, 68, 102); 
	  padding: 30px 0px;
	  float: left;
	  margin-top: -50px;
    }
    .accordion-title {
      padding: 15px 100px 15px 80px;
      cursor: pointer;
      font-weight: bold;
      display: flex;
      justify-content: flex-start;
      align-items: center;
      gap:10px;
    }
    .accordion-content {
      display: none;
      background-color: #fff;
    }
    .accordion-a{
      padding: 15px 100px 15px 80px;
    }
    a{
      text-decoration: none;
      color:black;
    }
    .accordion-a:hover {
      background-color: #f0f0f0;
    }
    span{margin-bottom: "3px";
    }
  </style>
</head>
<body>

<div class="accordion">
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      전사게시판
    </div>
    <div class="accordion-content">
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list1">공지사항</a>
    	</div>
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list2">주간시간표</a>
    	</div>
    	<div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list3">FAQ</a>
    	</div>
    </div>
  </div>
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      일반게시판
    </div>
    <div class="accordion-content">
        <div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list4">일반게시판</a>
    	</div>
    </div>
  </div>
  <div class="accordion-item">
    <div class="accordion-title" onclick="toggleAccordion(this)">
      부서게시판
    </div>
    <div class="accordion-content">
        <div class="accordion-a">
    		<a href="${pageContext.request.contextPath}/board/list5">솔루션영업팀</a>
    	</div>
    </div>
  </div>
</div>

<script>
  function toggleAccordion(element) {
    const content = element.nextElementSibling;
    const isOpen = content.style.display === "block";
    

    // 선택한 아코디언 콘텐츠 열기/닫기
    if (!isOpen) {
      content.style.display = "block";
    }else{
      content.style.display = "none";
    }
  }
</script>

</body>
</html>
