$(document).ready(function(){
	let isAllDayChk, startDate, endDate;
	let selectedArr = [];
	
	// 등록 버튼 클릭 시
    $("#btnRegister").on("click", function(e) {
        e.preventDefault();
        
        if (validateForm()) {
            // 폼 제출이 유효할 때 폼을 제출
            $("form[name='scheduleShareEvent']").submit();
        }
    });
    
	// form 유효성 검사
    function validateForm() {
		const $scheduleName = $("#schedule_name");
		const $start = $("#startAt"); 
		const $end = $("#endAt");

        if ($scheduleName.val().trim() === "") {
            alert("일정명을 입력하세요");
            $scheduleName.focus();
            return false;
        }

        if ($start.val().trim() === "") {
            alert("시작 날짜/시간을 선택하세요");
            $start.focus();
            return false;
        }

        if (new Date($start.val()) > new Date($end.val())) {
            alert("끝나는 날짜/시간이 시작 날짜/시간보다 이전입니다. 다시 확인해 주세요.");
            return false;
        }

        if ($end.val().trim() === "") {
            alert("종료 날짜/시간을 선택하세요");
            $end.focus();
            return false;
        }
        
        // 참석자 선택 확인
        if (selectedArr.length === 0) {
            alert("참석자를 선택하세요");
            return false;
        }
     
        return true;
    }
    
    // 종일 체크박스 상태 변경 시 
    $("#allDay").on("change", function() {
        isAllDayChk = $(this).prop("checked");
        startDate = $("#startAt").val();
        
        if (isAllDayChk) {
            $("#startAt, #endAt").prop("type", "date");
        } else {
            $("#startAt, #endAt").prop("type", "datetime-local");
        }
        
        if (isAllDayChk && startDate) {
            $("#endAt").val(startDate);
        }
    });
    
    // 시작 날짜 변경 시
    $("#startAt").on("change", function() {
        startDate = $(this).val();
        isAllDayChk = $("#allDay").prop("checked");

        if (isAllDayChk && startDate) {
            $("#endAt").val(startDate);
        }
    });
    
    // 종료 날짜 변경 시
    $("#endAt").on("change", function() {
        startDate = $("#startAt").val();
        endDate = $(this).val();
        isAllDayChk = $("#allDay").prop("checked");

        if (isAllDayChk) {
            if (startDate !== endDate) {
                alert("시작 날짜와 종료 날짜는 같아야 합니다.");
                $(this).focus();
                $("#btnRegister").prop("disabled", true); 
            } else {
                $("#btnRegister").prop("disabled", false); 
            }
        }
    });
    
    $('#bgColor').on("change", function () {
	    $(this).css('color', $(this).val());
	});
	
});