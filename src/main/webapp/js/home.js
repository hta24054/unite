$(document).ready(function () {
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
	                            <td><a href="javascript:void(0);" onclick="loadBoardList('${board.boardName2}', 1)">[${board.boardName2}]</a>&nbsp;${post.postSubject}</td> 
	                            <td><img src="${contextPath}/image/profile_navy.png" class="user_img" alt="프로필" style="width:20px; height: 20px;">&nbsp;${post.postWriter}<br>${formattedDate}</td>
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
	
	loadBoardData();  // 페이지 로드 시 데이터 로드
	setInterval(loadBoardData, 3000);  // 3초마다 데이터 갱신
});

	
		//게시판 데이터를 갱신하는 함수
		/*function loadBoardData() {
		        $.ajax({
		            url: "home", // 서블릿 URL
		            type: "GET",
		            dataType: "json",
		            success: function (data) {
		                let tableBody = $(".styled-table tbody");
		                tableBody.empty(); // 기존 데이터 초기화
		
		                // "boards"와 "posts" 데이터를 가져오기
		                let boards = data.boards; // Gson에서 추가한 "boards"
		                let posts = data.posts;   // Gson에서 추가한 "posts"
		
		                // 데이터를 조합하여 테이블에 추가
		                for (let i = 0; i < boards.length; i++) {
		                    let board = boards[i];
		                    let post = posts[i];
		
		                    let row = `
		                        <tr>
		                            <td>${board.boardName1}</td>
		                            <td>${post.postSubject} (${new Date(post.postUpdateDate).toLocaleString()})</td>
		                            <td>${post.postWriter}</td>
		                        </tr>`;
		                    tableBody.append(row);
		                }
		            },
		            error: function (xhr, status, error) {
		                console.error("게시판 데이터를 불러오는 데 실패했습니다: " + error);
		            }
		        });
		    }
		
		// 페이지 로드 후 3초마다 게시판 갱신
		$(document).ready(function() {
		    loadBoardData();  // 초기 데이터 로드
		    //setInterval(loadBoardData, 3000);  // 3초마다 게시판 갱신
		});
	*/
