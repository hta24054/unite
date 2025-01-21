$(document).ready(function () {
    color();
})

function color(){
    const projectId = $("#project-id").val();
    const memberId = $("#user-id").val();
    $.ajax({
        url: "/api/project/color",
        type: "GET",
        data: {
            projectId: projectId,
            memberId: memberId
        },
        success: function(data) {
            console.log("투두 리스트 데이터:");
            if(data.bgColor == 'rgb(255,255,255)' || data.bgColor == '#ffffff') updateColor('rgb(248,249,250)', data.textColor);
            else updateColor(data.bgColor, data.textColor);
        },
        error: function(xhr, status, error) {
            console.log("ssdfsdfsdf");
            console.error("AJAX 오류 발생:", status, error);
            alert("오류가 발생했습니다. 다시 시도해 주세요.");
        }
    });
}
function updateColor(bgColor, textColor) {
    $(".background").css({
        'background-color' : bgColor,
        'border-radius' : '50px',
        'padding' : '10px'
    });
    $(".text").css('color', textColor);
}