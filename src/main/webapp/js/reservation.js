$(document).ready(function(){
	let calendar;
	let events = []; 
	let isAllDayChk, startDate, endDate;
	const $resourceType = $("#resourceType");
	const $resourceName = $("#resourceName");
	
	GetResourceList();
	
	// 자원 목록 불러오기
	function GetResourceList() {
		$.ajax({
            url: "getResourceList",  
            type: "get",
	        dataType: "json",
            success: function (data) {
	            $resourceType.empty();
	            $resourceType.append('<option value="">분류명</option>');
				$resourceName.hide();
	
	            // 중복 제거 Set
	            const uniqueResourceType = new Set();
	            data.forEach(function(resource) {
	                if (!uniqueResourceType.has(resource.resourceType)) {
	                    uniqueResourceType.add(resource.resourceType);
	                    $resourceType.append('<option value="' + resource.resourceType + '">' + resource.resourceType + '</option>');
	                }
	            });
	        },
            error: function () {
                alert("자원 목록 불러오기 실패");
            }
        });
	}
	
	// 자원 선택 시 해당 자원명 불러오기
	$resourceType.on("change", function() {
		const $selectedVal = $(this).val();
		
		$.ajax({
			url: "resourceSelectChange",
			type: "get",
			dataType: "json",
			data : {
				resourceType : $selectedVal,
				resourceName: $("resourceName").val()
			},
			success: function(data) {
	            $resourceName.empty();
	            $resourceName.append('<option value="">자원명</option>');
	
	            data.forEach(function(resource) {
	                $resourceName.append('<option value="' + resource.resourceName + '">' + resource.resourceName + '</option>');
	            });
	
	            $resourceName.show();
	        },
            error: function () {
                alert("자원명 불러오기 실패");
            }
		});
	});
	
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
    
	
	// 캘린더 생성
	function initCalendar(){
		const calendarEl = document.getElementById('calendar'); 
		if (!calendarEl) return;
		
		calendar = new FullCalendar.Calendar(calendarEl, {
	        expandRows: true, // 화면에 맞게 높이 재설정
	        slotMinTime: '00:00', // Day 캘린더에서 시작 시간
	        slotMaxTime: '24:00', // Day 캘린더에서 종료 시간
			slotLabelFormat: 'HH:mm',
	        defaultAllDay: true, // 종일 이벤트
	        timeZone: 'local',
	        headerToolbar: {
	          left: 'prev,next',
	          center: 'title',
	          right: 'today'
	        },        
	        buttonText: {
		        today: '오늘' 
		    },
	        initialView: 'timeGridWeek', // 초기 로드 될때 보이는 캘린더 화면
	        editable: true, // 수정 가능
	        selectable: true, // 달력 일자 드래그 설정
	        nowIndicator: true, // 현재 시간 마크
	        dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
	        locale: 'ko', // 한국어 설정
			events: events, // 전역 이벤트 배열 사용
		    dateClick: function(info) {
			    
			},
            eventClick: function(info) {
                console.log("eventClick info", info.event);
               
            },
	        eventChange: function(info) { // 이벤트가 수정되면 발생하는 이벤트
	         	
	        },
	        select: function(info) {
			    console.log("select:", info);
			},
            eventDataTransform: function(event) { // 이벤트 데이터 변환 함수
               
            },
            eventDidMount: function(info) {
				 
			},
			eventDrop: function(info) {
	            
	        },
		});
		
		//선택 상태 해제
        calendar.unselect();
        calendar.render();
	}

	initCalendar();
});