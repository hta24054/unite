$(document).ready(function() {
    // 페이지 로딩 시 진행 중인 프로젝트 데이터 가져오기
    loadOngoingProjects();

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

    // 저장 버튼 클릭 이벤트
    $(document).on('click', '.save-status', function() {
        var projectId = $(this).data('project-id');
        var status = $(this).closest('tr').find('.project-status').val();

        var confirmationMessage = status === 'completed' ? 
            "정말 프로젝트를 종료하시겠습니까?" : 
            "정말 이 프로젝트를 취소하시겠습니까?";

        if (confirm(confirmationMessage)) {
            // AJAX 요청으로 상태 업데이트
            $.ajax({
                url: contextPath + "/project/main", // contextPath를 미리 설정한 변수 사용
                type: "POST",
                data: {
                    projectId: projectId,
                    status: status,
                    action: 'updateStatus' // 추가된 action 파라미터
                },
                success: function(response) {
                    console.log(response); // 서버 응답 확인
                    if (response.success) {
                        alert("상태가 업데이트되었습니다.");
                        // 상태 업데이트 후 진행 중인 프로젝트 목록 다시 불러오기
                        loadOngoingProjects();
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

// 진행 중인 프로젝트 데이터 로드 함수
function loadOngoingProjects() {
    $.ajax({
        url: contextPath + "/project/getOngoingProjects", // contextPath 사용
        type: "GET",
        success: function(data) {
            // 테이블에 데이터 추가
            var tbody = $(".table tbody");
            tbody.empty(); // 기존 데이터 제거
            $.each(data, function(index, project) {
                // 참여자와 열람자 이름 처리
                var participantNames = project.participantNames || [];
                var viewerNames = project.viewers || [];
                
                var participantsDisplay = participantNames.length > 1 
                    ? "<span title='" + participantNames.slice(1).join(', ') + "'>" + participantNames[0] + " 외 " + (participantNames.length - 1) + "명</span>"
                    : (participantNames[0] || '없음');

                var viewersDisplay = viewerNames.length > 1 
                    ? "<span title='" + viewerNames.slice(1).join(', ') + "'>" + viewerNames[0] + " 외 " + (viewerNames.length - 1) + "명</span>"
                    : (viewerNames[0] || '없음');
                
                tbody.append("<tr>" +
                    "<td>" + project.projectId + "</td>" +
                    "<td><a href='" + contextPath + "/project/detail?projectId=" + project.projectId + "'>" + project.projectName + "</a></td>" +
                    "<td>" + project.empName + "</td>" +
                    "<td>" + participantsDisplay + "</td>" +
                    "<td>" + viewersDisplay + "</td>" +
                    "<td>" +
                        "<div class='progress'>" +
                            "<div class='progress-bar' role='progressbar' style='width: " + project.progressRate + "%;'>" +
                                project.progressRate + "%" +
                            "</div>" +
                        "</div>" + 
                    "</td>" +
                    "<td>" + project.endDate + "</td>" +
                    // isManager가 true일 때만 상태 변경 select와 저장 버튼 추가
                    (project.isManager ? 
                        "<td>" +
                            "<select class='form-control project-status' data-project-id='" + project.projectId + "'>" +
                                "<option value='ongoing'>진행 중</option>" +
                                "<option value='completed'>완료</option>" +
                                "<option value='cancelled'>취소</option>" +
                            "</select>" +
                        "</td>" +
                        "<td><button class='btn btn-success save-status' data-project-id='" + project.projectId + "' style='display:none;'>저장</button></td>"
                        :
                        "<td colspan='2'>관리 권한 없음</td>"
                    ) +
                    "</tr>");
            });
        },
        error: function() {
            alert("프로젝트 데이터를 불러오는 데 실패했습니다.");
        }
    });
}
