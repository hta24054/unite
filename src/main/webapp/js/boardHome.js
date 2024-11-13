$(function() {
	let pathName = window.location.pathname;
	
    // 현재 페이지 URL을 확인하여 게시판 홈이면 피드형 레이아웃으로 표시
    if (pathName === '/unite/board/home') {
        loadBoardHome(); // 게시판 홈 데이터를 로드
    }
    
    $('#boardHome').click(function(){
		location.href = "home";
	})
    
    // 왼쪽 메뉴 클릭 이벤트 - 리스트형 레이아웃으로 게시글 표시
    $('.boardName2').click(function(e) {
		
		//html 초기화
		$('.boardContent').html('');
		
        e.preventDefault();
        boardName2 = $(this).text(); // 클릭된 메뉴 항목의 URL

        loadBoardList(boardName2); // 클릭한 메뉴에 맞는 게시판 데이터를 로드
    });

 	// 왼쪽 글쓰기 클릭 이벤트
    $('.writeBtn').click(function(e) {
		
		//html 초기화
		$('.boardContent').html('');
		
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
	    url: 'post/postWrite',
	    type: 'GET'
	})
	.done(function(response) {
	    $(".boardTitle").text('글쓰기');
	    $('.boardContent').html(response).css('padding', '20px 60px');
	    // Summernote 스크립트를 동적으로 로드하고 초기화
	    $.getScript('https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-bs4.min.js', function() {
	        $('.summernote').summernote({
	            height: 500,
	            minHeight: 500,
	            maxHeight: null,
	            focus: true
	        });
	    });
	    
	})
	.fail(function() {
	    $('.boardContent').html('<p>글쓰기 폼을 불러오는 데 실패했습니다.</p>');
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
        	var html = "";
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

//board와 post를 가져와 boardHome html 출력
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
	console.log('boardName2 = ',boardName2)
	
    $.ajax({
    	data: {"boardName2":boardName2},
        url: 'boardList',
        method: 'GET',
        success: function(response) {
			$(".boardTitle").text(boardName2);
	    	$('.boardContent').html(response);
        },
        error: function() {
            alert('게시판 데이터를 불러오는 데 실패했습니다.');
            let html = '<ul class="board-list">';
            html += '등록된 게시글이 없습니다.';
            html += '</ul>';
            $('.boardContent').append(html); // 리스트형 레이아웃에 게시글 표시
            
            $(".boardTitle").text(boardName2);//임시
        }
    });
}
