$(document).ready(function () {
    let index = 0;
	// const targetElement2 = document.querySelector("body > div.container > div.col-md-4.notification");
	// targetElement2.classList.remove("col-md-4");
	// targetElement2.classList.remove("notification");
	// const targetElement = document.querySelector("body > div > div.center > div:nth-child(2) > div");
	// targetElement.classList.remove("container");

    function loadBoardData() {
	    $.ajax({
	        url: "/api/home/board", // 서블릿 URL
	        type: "GET",
	        dataType: "json",
	        success: function (data) {
				let tableBody = $(".styled-table tbody");
				tableBody.empty();
				let boards = data.boardList;
				let emp = data.emp;

				// 게시판 리스트에서 각 게시판의 최근 게시글만 가져와서 표시
				boards.slice(0, 4).forEach(function (board) {
					let formattedDate = new Date(board.postDate).toLocaleString();
					let row = `
                    <tr onclick="window.location.href='/board/post/detail?no=${board.postId}&boardName1=${board.boardName1}&boardName2=${board.boardName2}&page=1'">
                        <td>[${board.boardName2}] ${board.postSubject}</a></td>
						<td><img src="/api/emp/profile-image?empId=${board.postWriter}" style="width:36px; height: 36px; border-radius:50%; border: 1px solid gray;">&nbsp;${emp[board.postWriter]}<br>${formattedDate}</td>
                    </tr>
                `;
						tableBody.append(row);  // 생성된 row를 테이블에 추가
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

});
