import './boardCommon.js';

let posts = [];
let empMap = null;
let currentIndex = 0; // 현재 출력된 데이터의 시작 인덱스
const itemsPerPage = 10; // 한 번에 출력할 데이터 개수
let isLoading = false; // 중복 호출 방지 플래그
let isFirstLoad = true; // 첫 로드 여부 확인

$(function () {
    let pathName = window.location.pathname;
    const urlParams = new URLSearchParams(window.location.search);
    const key = urlParams.get('name'); // "home에서 key = boardName2값"

    // 현재 페이지 URL을 확인하여 게시판 홈이면 피드형 레이아웃으로 표시
    if (pathName === '/board/home') {
        loadBoardHome(); // 게시판 홈 데이터를 로드
    }

    $('#boardHome').click(function () {
        location.href = "home";
    })
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


// 게시판 홈(피드형) 데이터 로드 함수
function loadBoardHome() {
    $.ajax({
        url: 'homeProcess',
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var html = "";
            if (data === null) {
                html += '<div style="margin-top: 80px; font-size:23px; font-weight:bold; margin-left: 20px;">등록된 게시글이 없습니다.</div>';
                $('.boardContent').html(html); // 피드형 레이아웃에 게시글 표시
            } else {
                empMap = data.empMap;

                for (let i = 0; i < data.list.length; i++) {
                    posts.push(data.list[i]);
                }

                loadMoreData(); // 첫 20개 출력
                window.addEventListener("scroll", handleScroll); // 스크롤 이벤트 추가
            }
            //updateDividerHeight();
        },
        error: function () {
            alert('게시판 홈 데이터를 불러오는 데 실패했습니다.');
        }
    });
}

// 로딩 오버레이 제어 함수
function showLoadingOverlay() {
    document.getElementById("loading-overlay").style.display = "block";
}

function hideLoadingOverlay() {
    document.getElementById("loading-overlay").style.display = "none";
}

// 데이터 렌더링 함수
function renderData(items) {

    items.forEach((data) => {
        let html = `<div class="board-container tr-post" data-no=${data.postId} data-name1='${data.boardName1}' data-name2='${data.boardName2}' data-page='1'>
                    <div class="board-header">
                        ${data.boardName1} > ${data.boardName2}`;

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
                        <div class="username">${empMap[data.postWriter]}</div>
                        <div class="date">${formattedDate}</div>
                    </div>
                </div>
                 `;

        const div = document.createElement("div");
        div.innerHTML = html;
        $('.boardContent').append(div);
    });
}

// 데이터 로드 함수
function loadMoreData() {
    if (isLoading) return; // 이미 로드 중이면 중지
    isLoading = true;

    // 다음 10개 데이터를 가져옴
    const nextItems = posts.slice(currentIndex, currentIndex + itemsPerPage);
    currentIndex += itemsPerPage; // 다음 데이터 인덱스로 이동

    if (isFirstLoad) {
        // 첫 로드에서는 바로 출력
        renderData(nextItems);
        isFirstLoad = false;
        isLoading = false;
    } else {
        showLoadingOverlay(); // 로딩 오버레이 표시
        setTimeout(() => {
            renderData(nextItems);
            hideLoadingOverlay(); // 로딩 오버레이 중지
            isLoading = false;
        }, 1000); // 1초 딜레이
    }

    // 데이터가 더 이상 없으면 이벤트 중지
    if (currentIndex >= posts.length) {
        window.removeEventListener("scroll", handleScroll);
    }
}

// 스크롤 이벤트 핸들러
function handleScroll() {
    const scrollTop = window.scrollY; // 현재 스크롤 위치
    const scrollHeight = document.body.scrollHeight; // 전체 문서 높이
    const clientHeight = window.innerHeight; // 화면 높이

    if (scrollTop + clientHeight >= scrollHeight - 50) { // 하단에 근접했을 때
        loadMoreData();
    }
}