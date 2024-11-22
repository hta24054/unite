$(document).ready(function(){
	let calendar;
	let events = []; 
	let isAllDayChk, startDate, endDate;
	let calendarInitialized = false; // 캘린더 초기화 여부 확인
	
	// 공휴일 불러오기
	function fetchHolidayData(data) {
	    const startMonth = data.startStr.substring(0, 7);

	    $.ajax({
	        url: "holiday",  
	        type: "get",
	        data: {
	            year: startMonth.substring(0, 4), 
	            month: startMonth.substring(5, 7) 
	        },
	        dataType: "json",
	        success: function (response) {
	            console.log("공휴일 불러오기 성공", response);
	
	            const holidayData = response.map(holiday => ({
	                title: holiday.holidayName,  
	                start: holiday.holidayDate,  
	                allDay: true,                
	                color: 'red'                 
	            }));
	
	            // 중복 공휴일 체크 후 추가
	            holidayData.forEach(holiday => {
	                if (!events.some(event => event.title === holiday.title && event.start === holiday.start)) {
	                    events.push(holiday);
	                }
	            });
		
				// 캘린더 초기화 한 번만
	            if (!calendarInitialized) {
	                initCalendar();
	                calendarInitialized = true;
	            }
	        },
	        error: function (error) {
	            console.log('공휴일 불러오기 오류', error);
	        }
	    });
	}

	// 개인/공유 일정, 공휴일 불러오기
	function fetchAllData() {
	    events = []; // 배열 초기화
	    Promise.all([
			fetchListData(), 
			fetchSharedListData()
		]).then(() => {
	        fetchHolidayData({
	            startStr: moment().startOf('month').format('YYYY-MM-DD'),
	            endStr: moment().endOf('month').format('YYYY-MM-DD')
	        });
	    });
	}
	
	// 일정 리스트 불러오기
	function fetchListData(){
		$.ajax({
	        url: "scheduleList",
	        type: "get",
	        dataType: "json",
	        data: {
				emp_id: $("#emp_id").val(),
			},
	        success: function(data) {
	            console.log("개인 일정 리스트 불러오기 성공", data);
	            
	            //events = []; 
	            if (data != null) {
			        for (let i = 0; i < data.length; i++) {
			            
			            // 중복 체크: schedule_id로 중복 여부 확인
	                    if (!events.some(event => event.id === data[i].schedule_id)) {
	                        events.push({
	                            title: data[i].schedule_name,
	                            start: data[i].schedule_start,
	                            end: data[i].schedule_end,
	                            backgroundColor: data[i].schedule_color,
	                            description: data[i].schedule_content,
	                            allDay: data[i].schedule_allDay,
	                            id: data[i].schedule_id,
	                            extendedProps: {
							        isShared: false, // 공유 일정
							    },
	                        });
	                    }
			        }
			    }
     	        // 캘린더 초기화
			    initCalendar();
			    
			    //calendar.unselect();
       			//calendar.render();
	        },
	        error: function(error) {
	            console.log('개인 일정 리스트 불러오기 오류', error);
	        }
	    });
	}
	
	// 공유 일정 리스트 불러오기
	function fetchSharedListData() {
	    $.ajax({
	        url: "sharedScheduleList", 
	        type: "get",
	        dataType: "json",
	        data: {
	            emp_id: $("#emp_id").val(), 
	        },
	        success: function(data) {
	            console.log("공유 일정 리스트 불러오기 성공", data);
	
				//events = []; 
	            if (data != null) {
	                // 공유 일정 데이터를 기존 이벤트 배열에 추가
	                for (let i = 0; i < data.length; i++) {
						
	                    // 중복 체크: schedule_id로 중복 여부 확인
	                    if (!events.some(event => event.id === data[i].schedule_id)) {
	                        events.push({
	                            title: data[i].schedule_name,
	                            start: data[i].schedule_start,
	                            end: data[i].schedule_end,
	                            backgroundColor: data[i].schedule_color,
	                            description: data[i].schedule_content,
	                            allDay: data[i].schedule_allDay,
	                            id: data[i].schedule_id,
	                            //isShared: true
	                            extendedProps: {
							        isShared: true, // 공유 일정
							    },
	                        });
	                    }
	                }
	            }
	            // 캘린더 초기화
	            initCalendar();
	            
	            //calendar.unselect();
       			//calendar.render();
	        },
	        error: function(error) {
	            console.log("공유 일정 리스트 불러오기 오류", error);
	        }
	    });
	}
	
	// 일정 등록
    function addEvent(eventData) {
        $.ajax({
            url: "scheduleAdd", 
            type: "post",
            dataType: "json",
            data: {
                emp_id: $("#emp_id").val(),
                schedule_name: eventData.schedule_name,
                startAt: moment(eventData.startAt).format('YYYY-MM-DD HH:mm'),
                endAt: moment(eventData.endAt).format('YYYY-MM-DD HH:mm'), 
                bgColor: eventData.bgColor,
                description: eventData.description,
                allDay: eventData.allDay ? 1 : 0 // true이면 1, false이면 0
            },
            success: function (data) {
                console.log("일정 추가 성공", data);

				//events = []; 
                if (data != null && data.length > 0) {
			        for (let i = 0; i < data.length; i++) {
						const isAllDay = data[i].schedule_allDay === 1;
						events.push({
							id: data[i].schedule_id,
			                title: data[i].schedule_name, 
			                start: data[i].schedule_start, 
			                end: data[i].schedule_end,
			                backgroundColor: data[i].schedule_color, 
			                description: data[i].schedule_content,
			                allDay: isAllDay 
			            });
			        }
			    }
			    
			    fetchAllData();
                $("#scheduleModal").modal("hide"); 
            },
            error: function() {
                console.log("일정 등록 오류");
            }
        });
    }
    
    // 일정 수정
	function updateEvent(eventData) {
		console.log("수정 데이터:", eventData);
	    $.ajax({
	        url: "scheduleUpdate",
	        type: "post",
	        dataType: "json",
	        data: {
				emp_id: $("#emp_id").val(),
	            schedule_id: eventData.schedule_id,  // schedule_id 값 전달
	            schedule_name: eventData.schedule_name,
	            startAt: moment($("#startAt").val()).format('YYYY-MM-DD HH:mm'),
    			endAt: moment($("#endAt").val()).format('YYYY-MM-DD HH:mm'), 
	            bgColor: eventData.bgColor,
	            description: eventData.description,
	            allDay: eventData.allDay ? 1 : 0 
	        },
	        success: function(data) {
	            console.log("일정 수정 성공", data);
	            fetchAllData();
	            $("#scheduleModal").modal("hide");
	            
	        },
	        error: function(error) {
	            console.log("일정 수정 오류", error);
	        }
	    });
	}
	
	// 일정 수정 - 드래그 이벤트 (시작/종료 날짜 수정)
	function updateDragEvent(info) {
	    console.log("eventChange: ", info.event.start, info.event.end);
	    
	    let endAt = info.event.end ? moment(info.event.end).format('YYYY-MM-DD HH:mm') : null;
	    const startAt = moment(info.event.start).format('YYYY-MM-DD HH:mm');
	
	    if (info.event.allDay && endAt === null) {
	        endAt = startAt;  // allDay 일정의 경우 시작 날짜와 종료 날짜를 동일하게 설정
	    }
	    
	    $.ajax({
	        url: "scheduleDragUpdate",
	        type: "post",
	        dataType: "json",
	        data: {
	            emp_id: $("#emp_id").val(),
	            schedule_id: info.event.id,
	            startAt: startAt,
	            endAt: endAt,
	            allDay: info.event.allDay ? 1 : 0
	        },
	        success: function(data) {
	            console.log("drag 일정 업데이트 성공:", data);
	            fetchAllData();
	        },
	        error: function(error) {
	            console.log("drag 일정 업데이트 오류:", error);
	            alert("drag 일정 업데이트 중 오류가 발생했습니다.");
	        }
	    });
	}
	
	// 일정 삭제
	function deleteEvent(eventData) {
		if(confirm("정말 삭제하시겠습니까?")) {
			$.ajax({
				url: "scheduleDelete",
				type: "post",
		        dataType: "json",
		        data: {
		            schedule_id: eventData.schedule_id,  // schedule_id 값 전달
		        },
		        success: function(data) {
		            console.log("일정 삭제 성공:", data);
		            
		            fetchAllData();
		            $("#scheduleModal").modal("hide");
		        },
		        error: function(error) {
		            console.log("일정 삭제 오류:", error);
		            alert("일정 삭제 중 오류가 발생했습니다.");
		        }
			});
		}	
	}
	
	// 상세 일정 팝업 
	function openDetailModal(event) {
		$("#schedule_id").val(event.id);
	    $("#schedule_name").val(event.title);
	    
	    $("#startAt").val(moment(event.start).format("YYYY-MM-DD HH:mm"));
	    $("#endAt").val(moment(event.end).format("YYYY-MM-DD HH:mm"));
	    
	    const description = event.extendedProps && event.extendedProps.description ? event.extendedProps.description : '';
	    $("#description").val(description);
	    
	    $("#bgColor").val(event.backgroundColor);
	    
	    // allDay 체크 여부
	    if (event.allDay) {
	        $("#allDay").prop("checked", true);
	        $("#startAt, #endAt").prop("type", "date");
	    } else {
	        $("#allDay").prop("checked", false);
	        $("#startAt, #endAt").prop("type", "datetime-local");
	    }
	    
	    if (event.extendedProps.isShared === true) {//공유 일정
		    $(".modal-header").find("h5").text("공유 일정");
		    
		    $("form[name='scheduleEvent'] input, form[name='scheduleEvent'] select, form[name='scheduleEvent'] textarea").prop("disabled", true);
		    
		    $(".modal-body").find(".btn_wrap").html(`
		        <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
		    `);
		} else {
		    $(".modal-body").find(".btn_wrap").html(`
		        <button type="reset" class="btn btn-secondary">취소</button>
		        <button type="button" id="btnUpdate" class="btn btn-primary">수정</button>
		        <button type="button" id="btnDelete" class="btn btn-danger">삭제</button>
		    `);
		}
	    
	    
	    // 일정 수정
	    $("#btnUpdate").off("click").on("click", function() {
	        const eventData = {
	            schedule_id: $("#schedule_id").val(),
	            schedule_name: $("#schedule_name").val(),
	            startAt: moment($("#startAt").val()).format('YYYY-MM-DD HH:mm'),
    			endAt: moment($("#endAt").val()).format('YYYY-MM-DD HH:mm'), 
	            bgColor: $("#bgColor").val(),
	            description: $("#description").val(),
	            allDay: $("#allDay").prop("checked")
	        };
	            
	        updateEvent(eventData);
	    });
	    
	    // 일정 삭제
	    $("#btnDelete").off("click").on("click", function() {
	        console.log("data Delete");
	        
	        const eventData = {
	            schedule_id: $("#schedule_id").val() 
	        };
	        
	        deleteEvent(eventData);
	    });
	
	    $("#scheduleModal").modal('show');
	}
    
    // 일정 등록 버튼 클릭 시 모달 초기화
	$(".btn.btn-info[data-target='#scheduleModal']").on("click", function() {
	    $(".modal-header").find("h5").text("일정 등록"); 
	    $(".modal-body").find(".btn_wrap").html(`
	        <button type="reset" class="btn btn-secondary">취소</button>
	        <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
	    `);
	
	    $("#schedule_name").val("");
	    $("#startAt").val("");
	    $("#endAt").val("");
	    $("#description").val("");
	    $("#bgColor").val("#1e3a8a");
	    $("#allDay").prop("checked", false);
	
	    // 등록 버튼에 이벤트 바인딩
	    $("#btnRegister").off("click").on("click", function(e) {
	        e.preventDefault();
	        const eventData = {
	            schedule_name: $("#schedule_name").val(),
	            startAt: $("#startAt").val(),
	            endAt: $("#endAt").val(),
	            bgColor: $("#bgColor").val(),
	            description: $("#description").val(),
	            allDay: $("#allDay").prop("checked")
	        };
	        if (validateForm()) {
	            addEvent(eventData);
	        }
	    });
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
	

	// 캘린더 생성
	function initCalendar(){
		const calendarEl = document.getElementById('calendar'); 
		if (!calendarEl) return;
		
		const startStr = moment().startOf('month').format('YYYY-MM-DD');
    	const endStr = moment().endOf('month').format('YYYY-MM-DD');
    	
    	fetchHolidayData({ startStr, endStr });
		
		calendar = new FullCalendar.Calendar(calendarEl, {
	        expandRows: true, // 화면에 맞게 높이 재설정
	        slotMinTime: '08:00', // Day 캘린더에서 시작 시간
	        slotMaxTime: '22:00', // Day 캘린더에서 종료 시간
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
	        initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
	        editable: true, // 수정 가능
	        selectable: false, // 달력 일자 드래그 설정
	        nowIndicator: true, // 현재 시간 마크
	        dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
	        locale: 'ko', // 한국어 설정
			events: events, // 전역 이벤트 배열 사용
		    dateClick: function(info) {
			    if (!info.event) { // 클릭한 날짜에 일정이 없는 경우
			        $(".modal-header").find("h5").text("일정 등록"); 
			        $(".modal-body").find(".btn_wrap").html(`
			            <button type="reset" class="btn btn-secondary">취소</button>
			            <button type="submit" class="btn btn-info" id="btnRegister">등록</button>
			        `);
			
			        $("#schedule_name").val("");
			        $("#startAt").val(moment(info.date).format("YYYY-MM-DD"));
			      
			        $("#endAt").val("");
			        $("#description").val("");
			        $("#bgColor").val("#1e3a8a");
			        $("#allDay").prop("checked", false);
			
			        $("#scheduleModal").modal("show");
			        
			        // 등록 버튼에 이벤트 바인딩
				    $("#btnRegister").off("click").on("click", function(e) {
				        e.preventDefault();
				        const eventData = {
				            schedule_name: $("#schedule_name").val(),
				            startAt: $("#startAt").val(),
				            endAt: $("#endAt").val(),
				            bgColor: $("#bgColor").val(),
				            description: $("#description").val(),
				            allDay: $("#allDay").prop("checked")
				        };
				        if (validateForm()) {
				            addEvent(eventData);
				        }
				    });
			        
			    } else {
			        openDetailModal(info.event);
			    }
			},
            eventClick: function(info) {
                console.log("eventClick info", info.event);
                openDetailModal(info.event);
            },
	        eventChange: function(info) { // 이벤트가 수정되면 발생하는 이벤트
	         	updateDragEvent(info);
	        },
	        select: function(info) {
			    console.log("select:", info);
			},
            eventDataTransform: function(event) { // 이벤트 데이터 변환 함수
                // allDay 이벤트일 경우 종료 날짜를 하루 더 추가
                if (event.allDay) {
                    event.end = moment(event.end).add(1, 'days'); // 종료 날짜를 하루 더함
                }
                return event; // 수정된 이벤트 반환
            },
            eventDidMount: function(info) {
				 //.log("info.event.extendedProps.isShared", info.event.extendedProps.isShared);
				 //console.log("info.event.extendedProps", info.event.extendedProps);
			},
			eventDrop: function(info) {
	            updateDragEvent(info); // 드래그 후, 일정 이동했을 때 서버로 변경사항 전달
	        },
			/*
			eventRender: function(info) {
	            if (info.event.extendedProps.isShared) {
	                info.el.style.pointerEvents = 'none'; // 클릭 및 드래그 불가능
	            } else {
	                info.el.style.pointerEvents = 'auto';
	            }
	        }
	        */
		});
		
		//선택 상태 해제
        calendar.unselect();
        calendar.render();
	}
	
	fetchAllData();
});