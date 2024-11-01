$(document).ready(function(){	
	const events = [];
	
	// calendar element 취득
    const calendarEl = $('#calendar')[0];
	
	const calendar = new FullCalendar.Calendar(calendarEl, {
	    height: '700px', // calendar 높이 설정
        expandRows: true, // 화면에 맞게 높이 재설정
        slotMinTime: '08:00', // Day 캘린더에서 시작 시간
        slotMaxTime: '24:00', // Day 캘린더에서 종료 시간
        defaultAllDay: true, // 종일 이벤트
        // 해더에 표시할 툴바
        headerToolbar: {
          left: 'prev,next',
          center: 'title',
          right: 'today'
        },
        initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
        navLinks: true, // 날짜를 선택하면 Day 캘린더나 Week 캘린더로 링크
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
			const title = prompt('Event Title:');
			if (title) {
		        calendar.addEvent({
		          title: title,
		          start: arg.start,
		          end: arg.end,
		          allDay: arg.allDay,
		          contents: arg.contents
		        })
			}
			calendar.unselect()
		},
	    events: function(info, successCallback, failureCallback) {
			successCallback(events);
	    },
		eventClick: function(info) {  
			
		}
	  });
	
	function showCalendar() {
	  calendar.render();
	}
		
	showCalendar();
	
	//모달창 이벤트
	$("#btnRegister").on("click", function () {
		  const eventData = {
		    title: $("#title").val(),
		    start: $("#startAt").val(),
		    end: $("#endAt").val(),
		    allday: $("#allDay").val(),
		    bgColor: $("#bgColor").val(),
		    contents: $("#contents").val()
		  };
		  //빈값입력시 오류
		  if (
			    eventData.title == "" ||
			    eventData.start == "" ||
			    eventData.end == "" ||
			    eventData.contents
		  ) {
		    	alert("입력하지 않은 값이 있습니다.");
		  } else if ($("#startAt").val() > $("#endAt").val()) { //끝나는 날짜가 시작하는 날짜보다 값이 클 경우
		   		alert("시간을 잘못입력 하셨습니다.");
		  } else {
			    // 이벤트 추가
			    calendar.addEvent(eventData);
			    $("#scheduleModal").modal("hide");
			    $("#title").val("");
			    $("#startAt").val("");
			    $("#endAt").val("");
			    $("#allDay").val("");
			    $("#bgColor").val("");
			    $("#contents").val("");
		  }
	});
});



