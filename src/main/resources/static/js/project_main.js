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
$(document).on('click', '#toggle-projects', function () {
	const container = $('#project-container');
	const icon = $('#toggle-icon-p');
	const isVisible = container.is(':visible'); // 현재 열림 상태 확인

	if (isVisible) {
		container.slideUp(); // 닫기
		icon.text('∧');
	} else {
		container.slideDown(); // 열기
		icon.text('∨');
	}
});
$(document).on('click', '#toggle-favorite', function () {
	const container = $('#project-favorite');
	const icon = $('#toggle-icon-f');
	const isVisible = container.is(':visible'); // 현재 열림 상태 확인

	if (isVisible) {
		container.slideUp(); // 닫기
		icon.text('∧');
	} else {
		container.slideDown(); // 열기
		icon.text('∨');
	}
});

function go(page) {
	const limit = $('#viewcount').val();
	const data = {limit:limit, state:"ajax", page: page}
	//ajax(data);
	loadProjects(1);
	loadProjects(0);
}
// function ajax(sdata){
// 	console.log(sdata);
// 	$.ajax({
// 		data: sdata,
// 		url: "/project/getProjects",
// 		dataType: "json",
// 		cache: false,
// 		success: function(data){
// 			console.log(data);
// 			$("#viewcount").val(data.limit);
// 			$("thead").find("span").text("글 개수 : " + data.listcount);
// 			if(data.listcount > 0){
// 				$("tbody").remove();
// 				updateBoardList(data); //게시판 내용 업데이트
// 				generatePagination(data);
// 			}else {
//                 // 데이터가 없을 경우, "등록된 글이 없습니다" 메시지를 표시
//                 $("tbody").html("<tr><td colspan='7' style='text-align:center;'>등록된 글이 없습니다</td></tr>");
//             }
// 		},
// 		error: function(xhr, status, error) { // error 콜백 함수
// 			console.error("에러 발생:", error); // 에러 메시지 출력
// 			console.error("상태 코드:", status); // 상태 코드 출력
// 			console.error("xhr 객체:", xhr); // xhr 객체 출력
// 		}
// 	});
// }

function updateBoardList(data) {
	let ongoingProjectsHtml = '';
	let favoriteProjectsHtml = '';

	$(data.boardlist).each(function(index, project) {
		const projectCard = `
            <div class="project-card" data-project-id="${project.projectId}">
				 <span><img src="../image/gear.png" style="width:30px; height: 30px; float:right;"></span>
                 <h5 style="padding-bottom: 15px">
                    <span style="float: left;">No.${project.projectId}</span>
                    <span>&nbsp;${project.projectName}</span>
                    <span 
						class="favorite-icon ${project.projectFavorite == 1 ? 'favorite' : 'unfavorite'}" 
						style="float: right; cursor: pointer; font-size: 1.5rem;" 
						data-project-id="${project.projectId}" 
						onclick="toggleFavorite(${project.projectId}, this)"
					>
						${project.projectFavorite == 1 ? '★' : '☆'}
					</span>
                </h5>
                <p><strong>책임자:</strong> ${project.managerName}</p>
                <p><strong>참여자:</strong> ${project.participants}</p>
                <p><strong>열람자:</strong> ${project.viewers}</p>
                <div class="progress">
                    <div class="progress-bar" style="width: ${project.avgProgress}%">${project.avgProgress}%</div>
                </div>
                <p><strong>마감일:</strong> ${project.projectEndDate}</p>
            </div>
        `;

		if (project.projectFavorite === 1) {
			favoriteProjectsHtml += projectCard;
			$('#project-favorite').html(favoriteProjectsHtml);
		} else {
			ongoingProjectsHtml += projectCard;
			$('#project-container').html(ongoingProjectsHtml);
		}
	});

}
$(document).on('click', '.project-card img', function() {
	var card = $(this).closest('.project-card');

	// 기존 배경색과 글자색을 가져오기
	var currentBgColor = card.css('background-color');
	var currentTextColor = card.css('color');

	// 모달에서 색상 선택기 값 초기화
	$('#bgColorPicker').val(rgbToHex(currentBgColor));  // RGB를 HEX로 변환
	$('#textColorPicker').val(rgbToHex(currentTextColor));  // RGB를 HEX로 변환

	// 색상 변경 버튼 클릭 이벤트
	$('#changeColorButton').off('click').on('click', function() {
		// 선택된 색상 값을 가져오기
		var selectedBgColor = $('#bgColorPicker').val();
		var selectedTextColor = $('#textColorPicker').val();

		// 프로젝트 카드 배경색과 글자색 변경
		card.css('background-color', selectedBgColor);
		card.css('color', selectedTextColor);

		// 변경된 색상을 서버에 저장
		saveColorSettings(card.data('project-id'), selectedBgColor, selectedTextColor);

		// 모달 닫기
		$('#colorModal').modal('hide');
	});

	// 모달 열기
	$('#colorModal').modal('show');
});

// RGB 값을 HEX로 변환하는 함수
function rgbToHex(rgb) {
	var result = rgb.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,.*)?\)$/);
	if (result) {
		return "#" +
			("0" + parseInt(result[1], 10).toString(16)).slice(-2) +
			("0" + parseInt(result[2], 10).toString(16)).slice(-2) +
			("0" + parseInt(result[3], 10).toString(16)).slice(-2);
	}
	return rgb; // 이미 HEX 형식이면 그대로 반환
}

// 서버에 색상 설정을 저장하는 함수
function saveColorSettings(projectId, bgColor, textColor) {
	$.ajax({
		url: '/project/saveProjectColorSettings',  // 서버에서 색상을 저장할 URL
		method: 'POST',
		data: {
			projectId: projectId,
			bgColor: bgColor,
			textColor: textColor
		},
		success: function(response) {
			console.log('색상 설정이 저장되었습니다.');
		},
		error: function(error) {
			console.log('색상 저장 실패:', error);
		}
	});
}

// 즐겨찾기 토글 함수
function toggleFavorite(projectId, element) {
	$.ajax({
		url: '/project/toggleFavorite',
		type: 'POST',
		data: { projectId: projectId },
		success: function (response) {
			if (response.success) {
				// 즐겨찾기 상태 변경
				const isFavorite = $(element).hasClass('unfavorite');
				$(element).toggleClass('favorite unfavorite').text(isFavorite ? '★' : '☆');
				// 프로젝트 목록 새로고침
				loadProjects(0); // 진행 중 프로젝트
				loadProjects(1); // 즐겨찾기 프로젝트
			} else {
				alert('즐겨찾기 상태를 업데이트할 수 없습니다.');
			}
		},
		error: function () {
			alert('오류가 발생했습니다.');
		}
	});
}

function loadProjects(favorite) {
	$.ajax({
		url: '/project/getProjects',
		data: { page: 1, favorite: favorite },
		type: 'GET',
		dataType: 'json',
		async: false,
		success: function(data) {
		//	프로젝트 데이터를 받아온 후 화면에 업데이트
			if (favorite === 0) {
				updateBoardList(data); // 진행 중 프로젝트
			} else {
				updateFavoriteProjects(data); // 즐겨찾기 프로젝트
			}
		},
		error: function(xhr, status, error) {
			console.error("Error loading projects:", error);
		}
	});
}

function updateFavoriteProjects(data) {
	let favoriteProjectsHtml = '';
	$(data.boardlist).each(function(index, project) {
		const projectCard = `
            <div class="project-card" data-project-id="${project.projectId}">
                 <span><img src="../image/gear.png" style="width:30px; height: 30px; float:right;"></span>
                 <h5 style="padding-bottom: 15px">
                    <span style="float: left;">No.${project.projectId}</span>
                    <span>&nbsp;${project.projectName}</span>
                    <span 
						class="favorite-icon ${project.projectFavorite == 1 ? 'favorite' : 'unfavorite'}" 
						style="float: right; cursor: pointer; font-size: 1.5rem;" 
						data-project-id="${project.projectId}" 
						onclick="toggleFavorite(${project.projectId}, this)"
					>
						${project.projectFavorite == 1 ? '★' : '☆'}
					</span>
                </h5>
                <p><strong>책임자:</strong> ${project.managerName}</p>
                <p><strong>참여자:</strong> ${project.participants}</p>
                <p><strong>열람자:</strong> ${project.viewers}</p>
                <div class="progress">
                    <div class="progress-bar" style="width: ${project.avgProgress}%">${project.avgProgress}%</div>
                </div>
                <p><strong>마감일:</strong> ${project.projectEndDate}</p>
            </div>
        `;
		favoriteProjectsHtml += projectCard;
	});
	$('#project-favorite').html(favoriteProjectsHtml); // 즐겨찾기 목록 업데이트
	updateProjectColors(data.boardlist);
}

function updateProjectColors(projects) {
	projects.forEach(function(project) {
		var projectCard = $('.project-card[data-project-id="' + project.projectId + '"]');

		// 서버에서 가져온 색상 정보를 적용
		if (project.bgColor) {
			projectCard.css('background-color', project.bgColor);
		}
		if (project.textColor) {
			projectCard.css('color', project.textColor);
		}
	});
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

