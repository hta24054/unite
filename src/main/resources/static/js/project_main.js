$(document).ready(function() {
    // 페이지 로딩 시 진행 중인 프로젝트 데이터 가져오기
	go(1);
	$("#viewcount").change(function(){
		go(1); //보여줄 페이지를 1페이지로 설정
	});
    // 상태 변경 시 저장 버튼 표시
    $(document).on('change', '.project-status', function() {
        var selectedValue = $(this).val();
        var saveButton = $(this).closest('tr').find('.save-status');
        if (selectedValue === 'completed' || selectedValue === 'cancelled') {
            saveButton.show(); // 완료 또는 취소 시 저장 버튼 보이기
        } else {
            saveButton.hide(); // 진행 중일 경우 저장 버튼 숨기기
        }
    });
    
	// tr 클릭 이벤트를 td 1~7번째만 적용
	$(document).on('click', 'tr.project_main td', function() {
	    // 클릭한 td의 인덱스 가져오기
	    var tdIndex = $(this).index(); 
	
	    // td 1~7번째(0-based index는 0~6)인 경우에만 이벤트 발생
	    if (tdIndex >= 0 && tdIndex <= 6) {
	        var projectId = $(this).closest('tr').data('project-id');
	        goToDetail(projectId);
	    }
	});

    // 저장 버튼 클릭 이벤트
    $(document).on('click', '.save-status', function() {
        var projectId = $(this).data('project-id');
        var status = $(this).closest('tr').find('.project-status').val();
		$('#fileModal').data('project-id', projectId).data('status', status).modal('show');
    });
    
    $('#confirmFileUpload').on('click', function() {
	    var projectId = $('#fileModal').data('project-id');
	    var status = $('#fileModal').data('status');
	    var fileData = new FormData();
	
	    // 파일이 하나만 선택된 경우 첫 번째 파일을 추가
	    var file = $('#fileInput')[0].files[0];  // 첫 번째 파일만 선택
	    if (file) {
	        fileData.append('file', file);  // 파일 추가
	    }
	
	    fileData.append('projectId', projectId);
	    fileData.append('status', status);
	    fileData.append('action', 'updateStatus');
	
	    var confirmationMessage = status === 'completed' ? 
	        "정말 프로젝트를 종료하시겠습니까?" : 
	        "정말 이 프로젝트를 취소하시겠습니까?";
	
	    if (confirm(confirmationMessage)) {
	        // AJAX 요청으로 상태 및 파일 업데이트
	        $.ajax({
	            url: contextPath + "/project/main",
	            type: "POST",
	            data: fileData,
	            processData: false,
	            contentType: false,
	            success: function(response) {
	                console.log(response); // 서버 응답 확인
	                if (response.success) {
	                    alert("상태가 업데이트되었습니다.");
	                    // 상태 업데이트 후 진행 중인 프로젝트 목록 다시 불러오기
	                    go(1);
	                    $('#fileModal').modal('hide'); // 모달 닫기
	                } else {
	                    alert("상태 업데이트에 실패했습니다.");
	                }
	            },
	            error: function() {
	                alert("서버 오류 발생.");
	            }
	        });
	    }
	});

});

function go(page){
	const limit = $('#viewcount').val();
	const data = {limit:limit, state:"ajax", page: page}
	ajax(data);
}
function ajax(sdata){
	console.log(sdata);
	$.ajax({
		data: sdata,
		url:  contextPath + "/project/getOngoingProjects",
		dataType: "json",
		cache: false,
		success: function(data){
			$("#viewcount").val(data.limit);
			$("thead").find("span").text("글 개수 : " + data.listcount);
			if(data.listcount > 0){
				$("tbody").remove();
				updateBoardList(data); //게시판 내용 업데이트
				generatePagination(data);
			}else {
                // 데이터가 없을 경우, "등록된 글이 없습니다" 메시지를 표시
                $("tbody").html("<tr><td colspan='7' style='text-align:center;'>등록된 글이 없습니다</td></tr>");
            }
		},
		error: function(){
			console.log('에러');
		}
	});
}

// 진행 중인 프로젝트 데이터 로드 함수
function updateBoardList(data) {
    let output = "<tbody>";
    
    $(data.boardlist).each(function(index, item) {
        var participantNames = item.participantNames || [];
        var viewerNames = item.viewers || [];
        var participants = participantNames.length > 1 
            ? "<span title='" + participantNames.slice(1).join(', ') + "'>" + participantNames[0] + " 외 " + (participantNames.length - 1) + "명</span>"
            : (participantNames[0] || '없음');
        var viewers = viewerNames.length > 1 
            ? "<span title='" + viewerNames.slice(1).join(', ') + "'>" + viewerNames[0] + " 외 " + (viewerNames.length - 1) + "명</span>"
            : (viewerNames[0] || '없음');
        
        output += `
            <tr style="height: 80px;" class="project_main" data-project-id="${item.projectId}">
                <td>#&nbsp;${item.projectId}</td>
                <td>${item.projectName}</td>
                <td>${item.empName}</td>
                <td>${participants}</td>
                <td>${viewers}</td>
                <td>
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" style="width: ${item.progressRate}%; color: white;">
                            ${item.progressRate}%
                        </div>
                    </div>
                </td>
                <td>${item.endDate}</td>
                ${item.isManager ? `
                    <td>
                        <select class="form-control project-status" style="margin:0" data-project-id="${item.projectId}" onclick="event.stopPropagation();">
                            <option value="ongoing">진행 중</option>
                            <option value="completed">완료</option>
                            <option value="cancelled">취소</option>
                        </select>
                        <button class="btn btn-success save-status" data-project-id="${item.projectId}" style="display:none;">저장</button>
                    </td>
                ` : `
                    <td colspan="2">관리 권한 없음</td>
                `}
            </tr>
        `;
    });
    
    output += "</tbody>";
    $('table').append(output);
}


// tr 클릭 시 상세 페이지로 이동
function goToDetail(projectId) {
    window.location.href = `${contextPath}/project/detail?projectId=${projectId}`;
}


function setPaging(href, digit, isActive = false) {
    const gray = (href === "" && isNaN(digit)) ? "gray" : "";
    const active = isActive ? "active" : "";
    const anchor = `<a class="page-link ${gray}" ${href}>${digit}</a>`;
    return `<li class="page-item ${active}">${anchor}</li>`;
}

function generatePagination(data) {
    let output = "";

    // 맨 처음 버튼
    let firstHref = data.page > 1 ? `href=javascript:go(1)` : "";
    output += setPaging(firstHref, '<<');

    // 이전 버튼
    let prevHref = data.page > 1 ? `href=javascript:go(${data.page - 1})` : "";
    output += setPaging(prevHref, '<');

    // 페이지 번호
    for (let i = data.startpage; i <= data.endpage; i++) {
        const isActive = (i === data.page);
        let pageHref = !isActive ? `href=javascript:go(${i})` : "";
        output += setPaging(pageHref, i, isActive);
    }

    // 다음 버튼
    let nextHref = (data.page < data.maxpage) ? `href=javascript:go(${data.page + 1})` : "";
    output += setPaging(nextHref, '>');

    // 맨 마지막 버튼
    let lastHref = data.page < data.maxpage ? `href=javascript:go(${data.maxpage})` : "";
    output += setPaging(lastHref, '>>');

    $('.pagination').empty().append(output);
}
