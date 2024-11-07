/**
 * 
 */
$(document).ready(function() {
    // projectName 변수 사용
    if (projectName) {
        $("#currentProjectName").text(projectName).show();
    }

    let currentProjectId, memberId;

    // 진행률 클릭 시
    $(".progress-rate.clickable").click(function() {
        currentProjectId = $(this).data("id"); // 프로젝트 ID
        memberId = $(this).data("memberid"); // 로그인한 사용자 ID
        const currentRate = $(this).data("rate"); // 현재 진행률
        console.log("currentProjectId: " + currentProjectId);
        console.log("memberId: " + memberId);
        console.log("currentRate: " + currentRate);

        // 모달에 진행률 설정
        $("#progressInput").val(currentRate);
        $("#progressModal").modal("show");
    });

    // 진행률 저장 버튼 클릭 시
    $("#saveProgressBtn").click(function() {
        const newProgressRate = $("#progressInput").val(); // 새로 입력된 진행률
        console.log("New progress rate: " + newProgressRate);

        if (!newProgressRate) {
            alert("진행률을 입력해 주세요.");
            return;
        }

        $.ajax({
            url: contextPath + "/project/updateprogress", // 서버 요청 URL
            type: "POST",
            data: { 
                projectId: currentProjectId,  // 프로젝트 ID
                memberId: memberId,  // 로그인한 사용자 ID
                memberProgressRate: newProgressRate // 새로운 진행률
            },
            success: function(response) {
                console.log("Response from server: ", response);

                if (response.success) {
                    // 서버에서 성공적으로 응답 받은 경우, 해당 로그인한 사용자의 진행률만 업데이트
                    const progressRateElement = $(`.progress-rate[data-id='${currentProjectId}'][data-memberid='${memberId}']`);

                    if (progressRateElement.length > 0) {
                        progressRateElement.text(newProgressRate + "%");  // 진행률 텍스트 업데이트
                        progressRateElement.data("rate", newProgressRate);  // data 속성도 업데이트
                    } else {
                        console.log("진행률 요소를 찾을 수 없습니다.");
                    }
                } else {
                    console.log("업데이트에 실패했습니다.");
                }
                $("#progressModal").modal("hide");  // 진행률 모달 창 숨기기
            },
            error: function() {
                console.log("오류가 발생했습니다.");
                $("#progressModal").modal("hide");
            }
        });
    });
});
