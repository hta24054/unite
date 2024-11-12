$(function() {
	let pathName = window.location.pathname;
	
    // 현재 페이지 URL을 확인하여 게시판 홈이면 피드형 레이아웃으로 표시
    if (pathName === '/unite/board/home') {
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
 	/* 
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
    }); */

});

function loardBoardWrite(boardId){
	console.log(boardId)
	$.ajax({
		/*data:{"boardId":boardId},*/
        url:  'post/postWrite',
        type: 'GET',
        success: function(response) {
        	$(".boardTitle").text('글쓰기');
            $('.boardContent').html(response).css('padding','10px 20px');
        },
        error: function() {
            $('.boardContent').html('<p>글쓰기 폼을 불러오는 데 실패했습니다.</p>');
        }
    });
}


// 게시판 홈(피드형) 데이터 로드 함수
function loadBoardHome() {
	console.log("home")
    $.ajax({
        url: 'homeProcess',
        method: 'GET',
        dataType: 'json',
        success: function(data) {
        	let html = "";
        	console.log(data)
            if(data=={}||data==null){
            	html += '등록된 게시글이 없습니다.';
            }else{
                html += '<div class="feed-container">';
            	const boards = data.boards; // boards 데이터
                const posts = data.posts;   // posts 데이터

                for (let i = 0; i < boards.length && i < posts.length; i++) {
                    const board = boards[i];
                    const post = posts[i];

                    console.log("Board:", board);
                    console.log("Post:", post);

                    html += AddHtmlBoardAndPost(board, post); // 각 board와 해당 post를 렌더링
                }
            	
	            html += '</div>';
            }
            $('.boardContent').html(html); // 피드형 레이아웃에 게시글 표시
        },
        error: function() {
            alert('게시판 홈 데이터를 불러오는 데 실패했습니다.');
        }
    });
}

//board와 post를 가져와 html 출력
function AddHtmlBoardAndPost(board, post) {
	
	let html = `<div class="board-container">
				    <div class="board-header">
				    	${board.boardName1}`;
    
    if (board.boardName2 !== '일반게시판') {
        html += ` > ${board.boardName2}`;
    }

    html += `       </div>
				    
				    <div class="board-title">
				    	<span>${post.postSubject}</span>
				        <img src='/unite/image/comments.png' alt="프로필 이미지" class="icon"/> <span>${post.postCommentCnt}</span>
				    </div>
				    
				    <div class="board-content">${post.postContent}</div>
				    
				    <div class="board-footer">
				        <img src='/unite/image/profile_black.png' alt="프로필 이미지">
				        <div class="username">${post.postWriter}</div>
				        <div class="date">${post.postUpdateDate}</div>
				    </div>
				</div>
				 `;
	 
	 return html;
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
            
        },
        error: function() {
            alert('게시판 데이터를 불러오는 데 실패했습니다.');
            let html = '<ul class="board-list">';
            html += '등록된 게시글이 없습니다.';
            html += '</ul>';
            $('.content').append(html); // 리스트형 레이아웃에 게시글 표시
            
            $(".boardTitle").text(boardName2);//임시
        }
    });
}
