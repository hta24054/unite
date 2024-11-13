<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>게시판</title>
<style>
  .main {
    font-family: Arial, sans-serif;
    margin-top: -3em;
    display: flex;
  }
  
  .header{
    position: fixed;
    z-index: 1;
    width: 100%;
    
  }
  
  /* 왼쪽 아코디언 메뉴 */
  .sidebar {
    width: 200px;
    border-right: 1px solid #ccc;
    height: 100vh;
    position: fixed;
  }
  
  .sidebar .title{
  	padding: 25px 20px 5px;
  }
  
  .sidebar h2 {
    font-size: 2rem;
    margin-bottom: 10px;
  }

  .accordion button {
    width: 100%;
    background-color: #f9f9f9;
    border: 1px solid #ccc;
    padding: 5px;
    text-align: left;
    cursor: pointer;
    font-size: 16px;
    outline: none;
    transition: background-color 0.3s;
    margin-bottom: 5px;
  }

  .accordion button:hover {
    background-color: #ddd;
  }

  .panel {
    display: none;
    background-color: white;
    overflow: hidden;
    margin-bottom: 10px;
  }

  .panel .boardName2 {
    padding: 5px 15px;
    color: #333;
    font-size: 14px;
  }

  .panel .boardName2:hover {
    background-color: #f0f0f0;
  }

  /* 오른쪽 게시판 영역 */
  .content {
    margin-left: 200px;
    width: calc(100% - 200px);
    height: 100vh;
    overflow-y: auto;
  }

  .post {
    border-bottom: 1px solid #ddd;
    padding: 15px 0;
  }

  .post h3 {
    margin: 0;
    font-size: 16px;
  }

  .post p {
    margin: 5px 0;
    color: #666;
  }

  .post .info {
    font-size: 12px;
    color: #999;
  }
  .boardTitle{
  	font-size: 2rem;
  	font-weight:normal;
  	padding: 20px;
  	border-bottom: 1px solid #ccc;
  }
  
  
  
  .board-container {
      font-family: Arial, sans-serif;
      border-bottom: 1px solid #ccc;
      padding: 15px 25px;
      width: 100%;
  }
  .board-header {
      font-size: 14px;
      color: #666;
      margin-bottom: 8px;
  }
  .board-title {
      font-size: 16px;
      font-weight: bold;
      margin-bottom: 4px;
      display: flex;
      align-items: center;
  }
  .board-title span {
      color: #999;
  }
  .board-content {
      font-size: 14px;
      color: #999;
      margin-bottom: 10px;
  }
  .board-footer {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #666;
  }
  .board-footer img {
      border-radius: 50%;
      width: 24px;
      height: 24px;
      margin-right: 6px;
  }
  .board-footer .username {
      margin-right: 10px;
      font-weight: bold;
  }
  .board-footer .date {
      color: #999;
  }
  .icon {
      display: inline-block;
      width: 25px;
      background-color: #ddd;
      border-radius: 50%;
      text-align: center;
      line-height: 16px;
      font-size: 12px;
      color: #666;
      margin: 0px 3px 0px 10px;
  }
</style>
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
<script src="${pageContext.request.contextPath}/js/boardHome.js"></script>
</head>
<body>
<jsp:include page="../common/header.jsp"/>
<div class="main">
	<div class="sidebar">
	  <section class="title">
	    <h2>게시판</h2>
	  </section>
	  
	  <section class="boardWrite">
	    <button class="writeBtn">글쓰기</button>
	  </section>
	  
	  <section class="companyBoard">
	    <button onclick="togglePanel('all-board')">전사게시판</button>
	    <div id="all-board" class="panel">
	      <div class="boardName2">공지사항</div>
	      <div class="boardName2">주간식단표</div>
	      <div class="boardName2">FAQ</div>
	    </div>
	  </section>
	  
	  <section class="generalBoard">
	    <button onclick="togglePanel('general-board')">일반게시판</button>
	    <div id="general-board" class="panel">
	      <div class="boardName2">일반게시판</div>
	    </div>
	  </section>
	  
	  <section class="departmentBoard">
	    <button onclick="togglePanel('department-board')">부서게시판</button>
	    <div id="department-board" class="panel">
	      <div class="boardName2">솔루션영업팀</div>
	    </div>
	  </section>
	</div>

	<div class="content">
	  <div class="boardTitle">게시판 홈</div>
	  <div class="boardContent"><!-- 추가 게시글을 이곳에 추가할 수 있습니다 -->
	  
	  </div>
	</div>
</div>


<script>
  function togglePanel(panelId) {
    const panel = document.getElementById(panelId);
    panel.style.display = panel.style.display === 'block' ? 'none' : 'block';
  }
</script>


</body>
</html>
