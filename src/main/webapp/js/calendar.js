$(document).ready(function(){	
	const $scheduleModal = $("#scheduleModal");
	const $scheduleName = $("#schedule_name");
	const $start = $("#startAt"); 
	const $end = $("#endAt");
	const $allDay = $("#allDay");
	const $bgColor = $("#bgColor");
	const $description = $("#description");
	
	const events = [];
    const calendarEl = $('#calendar')[0]; // calendar element 취득
	
	const calendar = new FullCalendar.Calendar(calendarEl, {
	    //height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        slotMinTime: '00:00', // Day 캘린더에서 시작 시간
        slotMaxTime: '24:00', // Day 캘린더에서 종료 시간
        defaultAllDay: true, // 종일 이벤트
        timeZone: 'UTC',
        
        // 해더에 표시할 툴바
        headerToolbar: {
          left: 'prev,next',
          center: 'title',
          right: 'today'
        },
        
        initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
        //events:JSON.parse(data),    // 서버에서 받은 데이터(스케쥴 내용)
        
        editable: true, // 수정 가능
        selectable: true, // 달력 일자 드래그 설정가능
        nowIndicator: true, // 현재 시간 마크
        dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
        locale: 'ko', // 한국어 설정
        
        
		eventAdd: function(obj) { // 이벤트가 추가되면 발생하는 이벤트
         	 console.log(obj);
        },
        
        eventChange: function(obj) { // 이벤트가 수정되면 발생하는 이벤트
         	console.log(obj);
        },
        
        eventRemove: function(obj){ // 이벤트가 삭제되면 발생하는 이벤트
         	console.log(obj);
        },
        select: function(arg) { // 캘린더에서 드래그로 이벤트를 생성할 수 있다.
			const title = prompt('일정 내용을 입력하세요 :');
			if (title) {
		        calendar.addEvent({
		          title: title,
		          start: arg.start,
		          end: arg.end,
		          allDay: arg.allDay,
		        })
			}
			calendar.unselect();
		},
		
	    events: function(info, successCallback, failureCallback) {
			successCallback(events);
	    },
	    
	    dateClick: function(info) { // 일자셀 클릭 함수
                console.log("dateClick", info)
        },
	    
	    
		eventClick: function(info) {  
			console.log("info", info)
			if (info) {
				
				$scheduleName.val(info.event.title);
				$bgColor.val(info.event.extendedProps.bgColor);
				$description.val(info.event.extendedProps.contents);
				$scheduleModal.modal('show');
			}
		}
	  });
	
	function showCalendar() {
	  calendar.render();
	}
		
	showCalendar();
	
	//모달창 이벤트
	$("#btnRegister").on("click", function () {
  		if($scheduleName.val().trim() == "") {
	  		alert("일정명을 입력하세요");
	  		$scheduleName.focus();
	  		return false;
  		}
  
	    if($start.val().trim() == "") {
			alert("시작날짜/시간을 선택하세요");
			$start.focus();
			return false;
	    }
  
		if($end.val().trim() == "") {
			alert("종료날짜/시간을 선택하세요");
			$end.focus();
			return false;
		}
  
		if($description.val().trim() == "") {
			 alert("내용을 입력하세요");
			 $description.focus();
			 return false;
		}
  
  	    // 입력값 객체 생성
		const eventData = {
		  	title: $scheduleName.val(),
		    start: $start.val(),
		    end: $end.val(),
		    allDay: $allDay.is(":checked"), // 체크박스인 경우 true/false
		    bgColor: $bgColor.val(),
		    description: $description.val()
		};

		// 끝나는 날짜가 시작하는 날짜보다 이전인지 검증
		if (eventData.start > eventData.end) {
			alert("시간을 잘못입력 하셨습니다.");
			return false;
		}
		
		// 이벤트 추가
		calendar.addEvent(eventData);
		
		// 모달 닫기 및 입력 필드 초기화
		$scheduleModal.modal("hide");
		$scheduleName.val("");
		$start.val("");
		$end.val("");
		//$allDay.prop("checked", false);
		$bgColor.val("");
		$description.val("");
	});
	
	
	$bgColor.on("change", function(){
		$(this).css("backgound-color", $(this).val());
	});
});



