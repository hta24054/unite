import './boardCommon.js';

$(function () {
    let pathName = window.location.pathname;
    var boardName2;
    const urlParams = new URLSearchParams(window.location.search);
    const key = urlParams.get('name'); // "home에서 key = boardName2값"

    // 현재 페이지 URL을 확인하여 게시판 홈이면 피드형 레이아웃으로 표시
    if (pathName === '/board/home') {
        loadBoardHome(); // 게시판 홈 데이터를 로드
    }

    $('#boardHome').click(function () {
        location.href = "home";
    })

    // 왼쪽 메뉴의 boardNamw1 클릭 이벤트
    // $('.boardName1').click(function(e) {
    // 	e.preventDefault();
    // 	updateDividerHeight();
    // });

    // 왼쪽 메뉴 클릭 이벤트 - 리스트형 레이아웃으로 게시글 표시
    // $('.boardName2').click(function(e) {
    //
    // 	$('.boardName2,.left a').removeClass('menuActive');
    // 	$(this).addClass('menuActive');
    // 	$(this).parent().parent().prev('a').addClass('menuActive');
    //
    // 	//html 초기화
    // 	$('.boardContent').html('');
    //
    //     e.preventDefault();
    //     boardName2 = $(this).text(); // 클릭된 메뉴 항목의 URL
    //
    //     loadBoardList(boardName2, 1); // 클릭한 메뉴에 맞는 게시판 데이터를 로드
    //
    // 	updateDividerHeight();
    // });
    //
    // $('.boardName2').each(function(){ //홈에서 클릭한 boardName2 실행
    // 	if($(this).text() == key){
    // 		$(this).trigger('click'); //트리거로 click이벤트 발생
    // 	}
    // })

    // 왼쪽 글쓰기 클릭 이벤트 or 게시글 수정 버튼 클릭 이벤트
    // $(document).on('click', '.writeBtn, .postModify-link', function (e) {
    //
    // 	//html 초기화
    // 	$('.boardContent').html('');
    //
    //     e.preventDefault();
    //     var postPage = 'post/postWrite';//글쓰기 url
    //     var title = '글쓰기';
    //     var boardName2 = $('.boardTitle').text();
    //
    //     if($(this).data('page') != null){//수정 버튼 누를시 data-page를 보냄
    // 		postPage = $(this).data('page');
    // 		title = '글쓰기 수정';
    // 	}
    //     loardBoardWrite(postPage,title,boardName2); // 글쓰기 메서드 호출
    //
    //     updateDividerHeight();
    // });

    // 왼쪽 글쓰기 클릭 이벤트 or 게시글 수정 버튼 클릭 이벤트
    // $(document).on('click', '.postReply-link', function (e) {
    //
    // 	//html 초기화
    // 	$('.boardContent').html('');
    //
    //     e.preventDefault();
    //     var postPage = $(this).data('page');
    //     var title = '게시글 답글';
    //     var boardName2 = $('.boardTitle').text();
    //
    //
    //     loardBoardWrite(postPage,title,boardName2); // 글쓰기 메서드 호출
    //     updateDividerHeight();
    // });


    // $(document).on('click', '.page-link', function(event) {
    // 	var nextPage = $(this).data('page');  // data-page에서 페이지 번호 가져오기
    //
    // 	if(boardName2 == null){
    // 		boardName2 = $(this).data('name');
    // 	}
    //
    // 	if(nextPage != null ){
    // 	    event.preventDefault();  // 기본 동작 방지 (링크 이동 방지)
    //
    // 		if(nextPage != ''){
    // 			loadBoardList(boardName2, nextPage);
    // 		}
    // 	}
    // });

    // $(document).on('click', '.tr-post', function(event) {
    //     event.preventDefault();  // 기본 동작 방지 (링크 이동 방지)
    //
    //     var postPage = $(this).data('page');  // data-page에서 페이지 번호 가져오기
    // 	var boardName2 = $(this).data('name');
    //
    // 	console.log("HboardName2=",boardName2)
    //
    // 	loadBoardPost(boardName2, postPage);
    // });

    // $(document).on('click', '.writeBtn, .boardName2, #boardHome, #registerBtn', function(event){
    // 	event.preventDefault();  // 기본 동작 방지 (링크 이동 방지)
    //
    // 	$('.boardContent').css('padding','0');
    // })

});

//sidebar와 content 사이의 구분선 길이 업데이트
// function updateDividerHeight() {
//     const sidebar = $('.sidebar')[0];
//     const content = $('.content')[0];
//     const sidebarHeight = sidebar.offsetHeight;
// 	const contentHeight = content.offsetHeight;
//
// 	var scrollT = $(document).scrollTop();	//스크롤바의 상단위치        
// 	var scrollH = $(document).height(); 	//스크롤바를 갖는 div의 높이       
// 	if(scrollT + scrollH > sidebarHeight) { //스크롤바가 아래 쪽에 위치할 때 
// 		sidebar.style.minHeight = contentHeight + "px";  
// 	}else{
//     	content.style.minHeight = sidebarHeight + "px";
// 	}
// }

// function loardBoardWrite(url, title, boardName2){
// 	console.log('boardName2=',boardName2)
// 	$.ajax({
// 		data:{"boardName2":boardName2},
// 	    url: url,
// 	    type: 'GET'
// 	})
// 	.done(function(response) {
// 	    $(".boardTitle").text(title);
// 	    $('.boardContent').html(response).css('padding', '20px 60px');
// 	})
// 	.fail(function() {
// 	    alert('글쓰기 폼을 불러오는 데 실패했습니다.');
// 	});
// }

// 게시판 홈(피드형) 데이터 로드 함수
function loadBoardHome() {
    $.ajax({
        url: 'homeProcess',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var html = "";
            if (data.length === 0) {
                html += '<div style="margin-top: 80px; font-size:23px; font-weight:bold; margin-left: 20px;">등록된 게시글이 없습니다.</div>';
            } else {
                html += '<div class="feed-container">';
                for (let i = 0; i < data.length; i++) {
                    html += AddHtmlBoardAndPost(data[i]);
                }
                html += '</div>';
            }
            $('.boardContent').html(html); // 피드형 레이아웃에 게시글 표시
            //updateDividerHeight();
        },
        error: function () {
            alert('게시판 홈 데이터를 불러오는 데 실패했습니다.');
        }
    });
}

//board와 post를 가져와 boardHome html 출력
function AddHtmlBoardAndPost(data) {

    let html = `<div class="board-container tr-post" data-no=${data.postId} data-name1='${data.boardName1}' data-name2='${data.boardName2}' data-page='1'>
				    <div class="board-header">
				    	${data.boardName1}`;

    if (data.boardName2 !== '일반게시판') {
        html += ` > ${data.boardName2}`;
    }

    const postContent = data.postContent;
    const truncatedContent = postContent.length > 500 ? postContent.substring(0, 500) + '...' : postContent;

    const postSubject = data.postSubject;
    const truncatedSubject = postSubject.length > 50 ? postSubject.substring(0, 50) + '...' : postSubject;

    // JavaScript에서 Date 객체로 변환하여 포맷
    let formattedDate = new Date(data.postDate).toLocaleString();


    html += `       </div>
				    
				    <div class="board-title">
				    	<span>` + truncatedSubject + `</span>
				        <img src='/image/comments.png' alt="댓글 이미지" class="icon"/> <span>${data.postCommentCnt}</span>
				    </div>
				    
				    <div class="board-content" style="word-break: break-all;">` + truncatedContent + `</div>
				    
				    <div class="board-footer">
				        <img src="/emp/profile-image?empId=${data.postWriter}" alt="Profile Image">
				        <div class="username">${data.postWriter}</div>
				        <div class="date">${formattedDate}</div>
				    </div>
				</div>
				 `;

    return html;
}

// 특정 게시판 리스트(리스트형) 데이터 로드 함수
function loadBoardList(boardName2, page) {//매개변수로 boardName2를 가져옴
    console.log('boardName2 = ', boardName2)
    console.log(page)
    //page가 1번이거나 게시판을 눌러서 불러올때는 boarList
    //page를 눌러서 불러온 경우는 boardList?page=${nextPage}
    const url = 'boardList' + (page == 1 ? '' : '?page=' + page);
    $.ajax({
        data: {"boardName2": boardName2},
        url: url,
        method: 'GET',
        success: function (response) {
            $(".boardTitle").text(boardName2);
            $('.boardContent').html(response);

            window.scrollTo(0, 0);// 스크롤 상단으로 이동
        },
        error: function () {
            alert('게시판 데이터를 불러오는 데 실패했습니다.');
        }
    });
}

//특정 게시판의 게시글 데이터 로드 함수
function loadBoardPost(boardName2, postPage) {
    console.log('boardName2 = ', boardName2)
    console.log(postPage)


    $('.boardName2').each(function () {//left메뉴 이벤트 주기
        if ($(this).text() === boardName2) {
            $(this).addClass('menuActive');
            $(this).parent().parent().prev('a').addClass('menuActive');
        }
    });

    const url = 'post/detail?num=' + postPage;
    $.ajax({
        data: {"boardName2": boardName2},
        url: url,
        method: 'GET',
        success: function (response) {
            $(".boardTitle").text(boardName2);
            $('.boardContent').html(response);

            window.scrollTo(0, 0);// 스크롤 상단으로 이동
            updateDividerHeight();
        },
        error: function () {
            alert('게시판 데이터를 불러오는 데 실패했습니다.');
        }
    });
}
