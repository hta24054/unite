$(document).ready(function () {
    let index = 0;
    // 쿠키 확인 함수
    function getCookie(name) {
        const cookies = document.cookie.split('; ');
        for (let i = 0; i < cookies.length; i++) {
            const [key, value] = cookies[i].split('=');
            if (key === name) return value;
        }
        return null;
    }
    // 쿠키 설정 함수
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000); // 하루 후 만료
        document.cookie = `${name}=${value}; expires=${date.toUTCString()}; path=/`;
    }

    // 오늘 하루 보지 않기 버튼 클릭 이벤트
    $('#popup-dont-show').on('click', function () {
        setCookie('dontShowPopup', 'true', 1); // 하루 동안 유지
        $('#popup-container').fadeOut();
    });

    function showPopup() {
        if (getCookie('dontShowPopup') === 'true') {
            console.log('오늘 하루 보지 않기 활성화됨');
            return; // 팝업 표시 안 함
        }

        if (index < notices.length) {
            const notice = notices[index];
            $('#popup-content').html(notice.content); // 공지사항 내용을 렌더링
            $('#popup-container').fadeIn(200);
            index++;
        } else {
            $('#popup-container').fadeOut(200); // 모든 공지사항이 끝나면 팝업 숨기기
        }
    }


    $('#popup-close').on('click', function () {
        $('#popup-container').fadeOut(200, function () {
            showPopup(); // 다음 공지사항 표시
        });
    });

    // 첫 팝업 표시
    showPopup();
    //include css 제거
    const targetElement = document.querySelector("body > div > div.center > div:nth-child(2) > div");
    targetElement.classList.remove("container");
    function loadBoardData() {
	    $.ajax({
	        url: contextPath + "/home/", // 서블릿 URL
	        type: "GET",
	        dataType: "json",
	        success: function (data) {
	            let tableBody = $(".styled-table tbody");
	            tableBody.empty();
	
	            let boards = data.boards;  // boards는 배열
	            let posts = data.posts;    // posts는 배열
	
	            // 게시글 데이터를 최근 5개만 가져오기 (최신순 정렬 후)
	            posts.sort(function (a, b) {
	                return new Date(b.postUpdateDate) - new Date(a.postUpdateDate); // 최신순 정렬
	            });
	
	            // 최대 4개 데이터만 처리
	            posts.slice(0, 4).forEach(function (post) {
	                // post.boardId에 맞는 board 찾기
	                let board = boards.find(board => board.boardId === post.boardId);
	                if (board) {
	                    // JavaScript에서 Date 객체로 변환하여 포맷
	                    let formattedDate = new Date(post.postUpdateDate).toLocaleString();
						console.log(board);
						console.log(posts);
	                    // 행을 생성하여 추가
	                    let row = `
	                        <tr>
	                            <td><a href="board/home?name=${board.boardName2}">[${board.boardName2}]</a>&nbsp;${post.postSubject}</td> 
	                            <td><img src="${contextPath}/emp/profile-image?UUID=${data.emp[post.postWriter]}"style="width:36px; height: 36px; border-radius:50%; border: 1px solid gray;">&nbsp;${data.name[post.postWriter]}<br>${formattedDate}</td>
	                        </tr>`;
	                    tableBody.append(row);  // 생성된 row를 테이블에 추가
	                } else {
	                    console.warn("해당 post에 연결된 board를 찾을 수 없습니다:", post);
	                }
	            });
	        },
	        error: function (xhr, status, error) {
	            console.error("게시판 데이터를 불러오는 데 실패했습니다: " + error);
	        }
	    });
	}
    // 기존 waiting_box.jsp의 데이터가 로드된 이후 실행
    function modifyWaitingDocsForHome() {
        const $tbody = $('#waiting_table tbody'); // 테이블 본문 선택
        const rows = $tbody.find('tr'); // 모든 행 선택
        const maxVisibleRows = 4; // 최대 표시할 행 수

        // 현재 행 개수 확인
        const totalRows = rows.length;
		
        if (totalRows > maxVisibleRows) {
            rows.slice(maxVisibleRows).hide(); // 5번째 행부터 숨기기
            const extraCount = totalRows - maxVisibleRows; // 추가 문서 수 계산
            $('h3:contains("결재 대기 문서")').append(`<span class="plus"> +${extraCount}건</span>`);
        }
        
    }
    modifyWaitingDocsForHome();
    // waiting_box.jsp가 로드된 후 실행
    setTimeout(modifyWaitingDocsForHome, 100); 
	
	loadBoardData();  // 페이지 로드 시 데이터 로드
    //setInterval(loadBoardData, 3000);  // 3초마다 데이터 갱신
    
    const targetElement2 = document.querySelector("body > div.container > div.col-md-4.notification");
    targetElement2.classList.remove("col-md-4");
    targetElement2.classList.remove("notification");
    notice();
	function notice() {
	    $.ajax({
	        url: contextPath + "/project/notice",
	        method: 'GET',
	        success: function (notifications) {
	            console.log("응답 데이터:", notifications);
	            
	            // 알림 데이터를 담을 컨테이너 요소
	            const postListContainer = $(".notification-content tbody");
	            
	            // 기존 내용을 지우고 새로운 알림을 추가
	            postListContainer.empty();
	
	            if (notifications && notifications.length > 0) {
	                const topNotifications = notifications.slice(0, 4);
	
	                topNotifications.forEach(notice => {
	                    const action = notice.taskUpdateDate 
	                        ? "수정(변경)하였습니다" 
	                        : "등록하였습니다";
	                    
	                    const actionDate = notice.taskUpdateDate 
	                        ? notice.taskUpdateDate 
	                        : notice.taskDate;
	
	                    const postRow = `
	                        <tr>
	                        	<td><img src="${contextPath}/emp/profile-image?UUID=${notice.task_file_uuid}"style="width:36px; height: 36px; border-radius:50%; border: 1px solid gray;"></td>
	                            <td>${notice.taskWriter} ${notice.Jobname}님이<br>${notice.ProjectName} - ${notice.taskTitle}을(를)<br>${action}<br><br><small>${actionDate}</small></td>
	                        </tr>
	                    `;
	                    postListContainer.append(postRow);
	                });
	            } else {
	                // 알림이 없는 경우
	                postListContainer.append(`
	                    <tr>
	                        <td colspan="2">게시글이 없습니다</td>
	                    </tr>
	                `);
	            }
	        },
	        error: function (error) {
	            console.error("알림 데이터를 가져오는 데 실패했습니다:", error);
	        }
	    });
	}
});
