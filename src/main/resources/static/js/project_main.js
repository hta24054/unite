$(document).ready(function() {
	// 페이지 로딩 시 진행 중인 프로젝트 데이터 가져오기
	go(1);
	$("#viewcount").change(function(){
		go(1); //보여줄 페이지를 1페이지로 설정
	});
});
$(document).on('cli' +
	'ck', '#toggle-projects', function () {
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

function updateBoardList(data) {
	let ongoingProjectsHtml = '';
	let favoriteProjectsHtml = '';

	$(data.boardlist).each(function(index, project) {
		// 참여자 처리
		let participantsText = '없음';
		if (project.participants) {
			const participantsArray = project.participants.split(','); // ','로 구분된 참여자 이름 목록
			if (participantsArray.length === 1) {
				participantsText = participantsArray[0];
			} else {
				participantsText = `${participantsArray[0]} 외 ${participantsArray.length - 1}명`;
			}
		}

		// 열람자 처리
		let viewersText = '없음';
		if (project.viewers) {
			const viewersArray = project.viewers.split(','); // ','로 구분된 열람자 이름 목록
			if (viewersArray.length === 1) {
				viewersText = viewersArray[0];
			} else {
				viewersText = `${viewersArray[0]} 외 ${viewersArray.length - 1}명`;
			}
		}

		// 프로젝트 카드 생성
		const projectCard = `
            <div class="project-card" data-project-id="${project.projectId}" data-manager-id="${project.managerId}">
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
                <p><strong>참여자:</strong> ${participantsText}</p>
                <p><strong>열람자:</strong> ${viewersText}</p>
                <p><strong>마감일:</strong> ${project.projectEndDate}</p>
                <div class="progress">
                    <div class="progress-bar" style="width: ${project.avgProgress}%">${project.avgProgress}%</div>
                </div>
            </div>
        `;

		// 즐겨찾기와 진행 중 프로젝트 분리
		if (project.projectFavorite === 1) {
			favoriteProjectsHtml += projectCard;
			$('#project-favorite').html(favoriteProjectsHtml);
		} else {
			ongoingProjectsHtml += projectCard;
			$('#project-container').html(ongoingProjectsHtml);
		}
	});
	updateProjectColor(data.boardlist);
}
function updateProjectColor(projects) {
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
$(document).on('click', '.project-card img', function() {
	var card = $(this).closest('.project-card');
	var projectId = card.data('project-id');
	var managerId = card.data('manager-id');
	var loggedInUserId = $('#loggedInUserId').val();

	console.log("managerId", managerId);
	console.log("loggedInUserId", loggedInUserId);
	console.log("projectId", projectId);

	// 기존 배경색과 글자색을 가져오기
	var currentBgColor = card.css('background-color');
	var currentTextColor = card.css('color');

	// 모달에서 색상 선택기 값 초기화
	$('#bgColorPicker').val(rgbToHex(currentBgColor));  // RGB를 HEX로 변환
	$('#textColorPicker').val(rgbToHex(currentTextColor));  // RGB를 HEX로 변환

	// 모달 열기
	$('#colorModal').modal('show');

	// 초기 상태 설정 (배경 및 글자 색 설정 화면 표시)
	$('#settingType').val('color');  // 'color'를 기본 선택하도록 설정
	$('#colorSettings').show();  // 배경 및 글자 색 변경 화면 표시
	$('#projectSettings').hide();  // 프로젝트 상태 변경 화면 숨김
	$('#changeColorButton').show();  // 배경 및 글자 색 변경 버튼 보이기
	$('#saveProjectStatus').hide();  // 프로젝트 상태 저장 버튼 숨기기


	// 설정 유형에 따라 화면 전환
	$('#settingType').off('change').on('change', function() {
		var settingType = $(this).val();

		if (settingType === 'color') {
			// 배경 및 글자 색 변경 화면
			$('#colorSettings').show();
			$('#projectSettings').hide();
			$('#changeColorButton').show();
			$('#saveProjectStatus').hide();
		} else if (settingType === 'project') {
			// 프로젝트 상태 변경 화면
			$('#colorSettings').hide();
			$('#projectSettings').show();
			$('#changeColorButton').hide();

			// 로그인한 사용자가 책임자일 경우에만 프로젝트 상태 변경 기능 활성화
			if (managerId == loggedInUserId) {
				$('#projectStatusMessage').text('이 프로젝트를 완료하거나 취소할 수 있습니다.');
				$('#projectStatusSelect').show();
				$('#saveProjectStatus').show();
			} else {
				$('#projectStatusMessage').text('책임자만 이 프로젝트를 취소하거나 완료할 수 있습니다.');
				$('#projectStatusSelect').hide();
				$('#saveProjectStatus').hide();
			}
		}
	});

	// 배경색 변경 버튼 클릭 이벤트
	$('#changeColorButton').off('click').on('click', function() {
		var selectedBgColor = $('#bgColorPicker').val();
		var selectedTextColor = $('#textColorPicker').val();

		// 프로젝트 카드 배경색과 글자색 변경
		card.css('background-color', selectedBgColor);
		card.css('color', selectedTextColor);

		// 변경된 색상을 서버에 저장
		saveColorSettings(projectId, selectedBgColor, selectedTextColor);

		// 모달 닫기
		$('#colorModal').modal('hide');
	});

	$('#saveProjectStatus').off('click').on('click', function() {
		var status = $('#projectStatusSelect').val();  // 선택된 상태 가져오기
		var confirmationMessage;

		if (status === 'completed') {
			confirmationMessage = "정말 프로젝트를 종료하시겠습니까?";
		} else if (status === 'canceled') {
			confirmationMessage = "정말 이 프로젝트를 취소하시겠습니까?";
		} else {
			alert("알 수 없는 상태입니다.");
			return;
		}

		// 확인 메시지 후 상태 업데이트 진행
		if (confirm(confirmationMessage)) {
			updateProjectStatus(projectId, status);  // 상태 업데이트 함수 호출
		}
	});
});


function updateProjectStatus(projectId, status) {
	$.ajax({
		url: '/project/updateStatus',
		type: 'POST',
		data: { projectId: projectId, status: status },
		success: function(response) {
			if (response.success) {
				alert('프로젝트 상태가 업데이트되었습니다.');
				loadProjects(0);  // 진행 중 프로젝트 갱신
				loadProjects(1);  // 즐겨찾기 프로젝트 갱신
				$('#colorModal').modal('hide');
			} else {
				alert('프로젝트 상태 업데이트 실패.');
			}
		},
		error: function() {
			alert('서버 오류 발생');
		}
	});
}

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
		url: '/project/saveProjectColor',
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
		// 참여자 처리
		let participantsText = '없음';
		if (project.participants) {
			const participantsArray = project.participants.split(','); // ','로 구분된 참여자 이름 목록
			if (participantsArray.length === 1) {
				participantsText = participantsArray[0];
			} else {
				participantsText = `${participantsArray[0]} 외 ${participantsArray.length - 1}명`;
			}
		}

		// 열람자 처리
		let viewersText = '없음';
		if (project.viewers) {
			const viewersArray = project.viewers.split(','); // ','로 구분된 열람자 이름 목록
			if (viewersArray.length === 1) {
				viewersText = viewersArray[0];
			} else {
				viewersText = `${viewersArray[0]} 외 ${viewersArray.length - 1}명`;
			}
		}

		// 프로젝트 카드 생성
		const projectCard = `
            <div class="project-card" data-project-id="${project.projectId}" data-manager-id="${project.managerId}">
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
                <span class="project-detail-link" onclick="goToProjectDetail(${project.projectId})">
					<p><strong>책임자:</strong> ${project.managerName}</p>
					<p><strong>참여자:</strong> ${participantsText}</p>
					<p><strong>열람자:</strong> ${viewersText}</p>
					<p><strong>마감일:</strong> ${project.projectEndDate}</p>
					<div class="progress">
						<div class="progress-bar" style="width: ${project.avgProgress}%">${project.avgProgress}%</div>
					</div>
				</span>
            </div>
        `;
		favoriteProjectsHtml += projectCard;
	});
	$('#project-favorite').html(favoriteProjectsHtml); // 즐겨찾기 목록 업데이트
	updateProjectColors(data.boardlist);
}
function goToProjectDetail(projectId) {
	window.location.href = `/project/detail?projectId=${projectId}`;
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

