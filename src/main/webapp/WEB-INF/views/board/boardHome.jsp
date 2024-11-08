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
    margin-left: 220px;
    padding: 20px;
    width: calc(100% - 220px);
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
</style>
<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
<script>

$(function() {
	let pathName = window.location.pathname;
	
    // 현재 페이지 URL을 확인하여 게시판 홈이면 피드형 레이아웃으로 표시
    if (pathName === '/unite/board') {
        loadBoardHome(); // 게시판 홈 데이터를 로드
    }
    
    // 왼쪽 메뉴 클릭 이벤트 - 리스트형 레이아웃으로 게시글 표시
    $('.boardName2').click(function(e) {
        e.preventDefault();
        boardName2 = $(this).text(); // 클릭된 메뉴 항목의 URL

        loadBoardList(boardName2); // 클릭한 메뉴에 맞는 게시판 데이터를 로드
    });

 	// 왼쪽 글쓰기 클릭 이벤트
    $('.writeBtn').click(function(e) {
        e.preventDefault();
        
        loardBoardWrite("boardWrite"); // 글쓰기 메서드 호출
    });
 	
    window.addEventListener('popstate', function(event) {
        if (event.state && event.state.page) {
            var page = event.state.page;
            console.log(page);

            $.ajax({
                url: '${pageContext.request.contextPath}/board/boardWrite',
                type: 'GET',
                data: {"boardId":boardId},
                success: function(response) {
                	$(".boardTitle").text('글쓰기');
                    $('.content').append(response);
                },
                error: function(xhr, status, error) {
                    $('.content').append('<p>글쓰기 폼을 불러오는 데 실패했습니다.</p>');
                }
            });
        }
    });

});

function loardBoardWrite(boardId){
	console.log(boardId)
	$.ajax({
		data:{"boardId":boardId},
        url:  '${pageContext.request.contextPath}/board/boardWrite',
        type: 'GET',
        success: function(response) {
        	$(".boardTitle").text('글쓰기');
            $('.content').append(response);
            history.pushState({ page: 'writeForm' }, null, '${pageContext.request.contextPath}/board/write');
        },
        error: function() {
            $('.content').append('<p>글쓰기 폼을 불러오는 데 실패했습니다.</p>');
            history.pushState({ page: 'writeForm' }, null, '${pageContext.request.contextPath}/board/write');
        }
    });
}


// 게시판 홈(피드형) 데이터 로드 함수
function loadBoardHome() {
	console.log("home")
    $.ajax({
        url: 'boardHome',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
            let html = '<div class="feed-container">';
            if(data==null){
            	html += '등록된 게시글이 없습니다.';
            }else{
	            data.forEach(post => {
	                html += `<div class="post">
	                 	     	<h3>${post.boardName1}</h3>
	                 	     	<p>${post.subject}</p><span>댓글 : ${post.commentCnt}</span>
	                 	     	<div class="info"><span><img src=${post.profile}></img></span><span>${post.name}</span><span>${post.date}</span></div>
	                 	  	 </div>`;
	            });
            }
            html += '</div>';
            $('.content').append(html); // 피드형 레이아웃에 게시글 표시
        },
        error: function() {
            alert('게시판 홈 데이터를 불러오는 데 실패했습니다.');
        }
    });
}

// 특정 게시판 리스트(리스트형) 데이터 로드 함수
function loadBoardList(boardName2) {//매개변수로 boardName2를 가져옴
    $.ajax({
    	data: {"boardName2":boardName2},
        url: 'boardListProcess',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
        	$(".boardTitle").text(boardName2);
            let html = '<ul class="board-list">';
            data.forEach(post => {
                html += `<li>
                            <h3>${post.title}</h3>
                            <p>작성자: ${post.author} | 날짜: ${post.date} | 조회수: ${post.views}</p>
                         </li>`;
            });
            html += '</ul>';
            $('.content').append(html); // 리스트형 레이아웃에 게시글 표시
            
         	// URL을 변경하여 브라우저 기록에 추가
            history.pushState(null, null, '${pageContext.request.contextPath}/board/${data.boardId}'); // '/게시판id'로 링크 이동했다는 표시
        },
        error: function() {
            alert('게시판 데이터를 불러오는 데 실패했습니다.');
            let html = '<ul class="board-list">';
            html += '등록된 게시글이 없습니다.';
            html += '</ul>';
            $('.content').append(html); // 리스트형 레이아웃에 게시글 표시
            
            $(".boardTitle").text(boardName2);//임시
            history.pushState(null, null, '${pageContext.request.contextPath}/board/20'); // 임시
        }
    });
}

</script>
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
	  <h2 class="boardTitle">게시판 홈</h2>
	  
	  
	  <!-- 추가 게시글을 이곳에 추가할 수 있습니다 -->
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
