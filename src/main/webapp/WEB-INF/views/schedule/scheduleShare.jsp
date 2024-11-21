<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../common/header.jsp" />
	<title>캘린더 - 공유 일정 등록</title> 
	<jsp:include page="../common/fullcalendar.jsp" />
	<jsp:include page="scheduleShare_leftbar.jsp"/>
	<script src="${pageContext.request.contextPath}/js/calendar.js"></script>
	<style>	
		@media (max-width: 1600px) {
		    .container-xl > div {
			    padding-left: 200px;
			}
		}
			
		textarea {
			width: 100%;
			height: 100px;
			margin-bottom: 10px;
		}
		
		.btn_wrap {
			display: flex;
		    align-items: center;
		    justify-content: center;
		}
		
		.btn_wrap button {
			margin: 0 5px;
		}
		
		#scheduleShareBtn {
			font-weight: 600;
		}
		
		a:hover {
		    text-decoration: none;
		}
	</style>
</head>
<body>
	<div class="container-xl">
		<div>
			<h3 class="mb-5">공유 일정 등록</h3>
			<div>
				<input type="hidden" id="schedule_id" name="schedule_id" value="${schedule_id}">
				<input type="hidden" id="emp_id" name="emp_id" value="${id}">
				<input type="hidden" id="share_emp" name="share_emp" value="${share_emp.join(',')}"> 	
				
				<div class="form-group row">
					<label for="schedule_name" class="col-sm-2 col-form-label">일정명</label>
					<div class="col-sm-8">
	                    <input type="text" class="form-control" placeholder="일정명을 입력하세요" name="schedule_name" id="schedule_name">
	                </div>
				</div>
				
				<div class="form-group row custom-control custom-checkbox">
					<div class="col-sm-7">
						<input type="checkbox" name="allDay" id="allDay" class="custom-control-input" value="">
	             		<label for="allDay" class="custom-control-label">하루종일</label>
					</div>
				</div>
		        
				<div class="form-group row">
					<label for="startAt" class="col-sm-2 col-form-label">시작날짜/시간</label>
					<div class="col-sm-8">
						<input type="datetime-local" name="startAt" id="startAt" class="form-control">
					</div>
				</div>
			
				<div class="form-group row">
		            <label for="endAt" class="col-sm-2 col-form-label">종료날짜/시간</label>
		            <div class="col-sm-8">
						<input type="datetime-local" name="endAt" id="endAt" class="form-control">
					</div>
		        </div>
	        
       			<div class="form-group row">
       				<p class="col-sm-2">색상</p>
       				<div class="col-sm-8">
						<select name="bgColor" id="bgColor">
							<option value="#1e3a8a" style="color: #1e3a8a;">Blue100</option>
				            <option value="#1d4ed8" style="color: #1d4ed8;">Blue200</option>
				            <option value="#22d3ee" style="color: #22d3ee;">Blue300</option>
				            <option value="#16a34a" style="color: #16a34a;">Green100</option>
				            <option value="#84cc16" style="color: #84cc16;">Green200</option>
				            <option value="#dc2626" style="color: #dc2626;">Red100</option>
				            <option value="#f43f5e" style="color: #f43f5e;">Red200</option>
				            <option value="#facc15" style="color: #facc15;">Yellow</option>
						</select>
					</div>
		        </div>
	        
		        <div class="form-group row">
		        	<p class="col-sm-2">공유자</p>
		        	<div class="col-sm-8">
		        		<div id="scheduleShareEmp"></div>
		        		<a href="javascript:void(0);" data-target="scheduleShareEmp" id="scheduleShareBtn">+ 공유자 선택</a>
		        	</div>
		        </div>
	        
		        <div class="form-group row">
		        	<p class="col-sm-2">내용</p>
		        	<div class="col-sm-8">
		        		<textarea rows="10" name="description" id="description"></textarea>
		        	</div>
		        </div>
	        
       			<div class="btn_wrap">
       				<button type="reset" class="btn btn-secondary">취소</button>
					<button type="button" class="btn btn-info" id="btnShareRegister">등록</button>
       			</div>
			</div>
		</div>
	</div>
	
	<%-- 공유자 선택 모달 --%>
	<div class="modal fade" id="scheduleShareModal" tabindex="-1" role="dialog" aria-labelledby="scheduleShareModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="scheduleShareModalLabel">조직도</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
				</div>
				<div class="modal-body">
					 <jsp:include page="../common/empTree.jsp"/>
				</div>
				<div class="modal-footer">
					<div class="btn_wrap">
						<button type="submit" class="btn btn-info" id="btnSave">저장</button>
         			</div>
				</div>
			</div>
		</div>
	</div>

    <script>
	    $("#scheduleShareBtn").on("click", function(e) {
			e.preventDefault();
			
	        const targetId = $(this).data('target');
	        localStorage.setItem('selectedEmpId', targetId);
	        
	        $('#scheduleShareModal').modal('show');
	    });
		
	    // "선택" 열 추가
	    $('#employeeTableContainer thead tr').prepend('<th>선택</th>');
	
	    // 직원 목록 행에 체크박스를 추가하는 함수
	    function addCheckboxes() {
	        $('#employeeTableBody tr').each(function () {
	            const empId = $(this).find('td:eq(0)').text().trim();
	            $(this).prepend('<td><input type="checkbox" name="selectedEmp" value="' + empId + '"></td>');
	            $(this).find('td:eq(1)').hide();  // empId 열 숨기기
	        });
	    }
	
	    // 최초 로딩 시 체크박스 추가
	    addCheckboxes();
	
	    // AJAX 후 체크박스를 다시 추가
	    $(document).on('ajaxSuccess', function () {
	        addCheckboxes();
	    });
	
	    // 저장 버튼 클릭 시 직원 정보 등록
	    $('#btnSave').on('click', function() {
	        insertEmp();  // 직원 등록 함수 호출
	    });
	     
	    $('#btnShareRegister').on('click', function(e) {
	        e.preventDefault();
	        
	        if (!validateForm()) {
	            return;
	        }
	        
	        const shareData = {
	            emp_id: $('#emp_id').val(),
	            share_emp: $('#share_emp').val(),
	            schedule_name: $('#schedule_name').val(),
	            allDay: $('#allDay').prop('checked') ? 1 : 0,
	            startAt: moment($("#startAt").val()).format('YYYY-MM-DD HH:mm'),
				endAt: moment($("#endAt").val()).format('YYYY-MM-DD HH:mm'), 
	            bgColor: $('#bgColor').val(),
	            description: $('#description').val(),
	            isShared : true,
	        }
	
	        $.ajax({
	            url: '${pageContext.request.contextPath}/schedule/scheduleShareAdd',
	            method: 'post',
	            dataType: 'json',
	            data: shareData,
	            success: function(data) {
	            	console.log("공유 일정 등록 성공", data); 
	            	
	            	alert('공유 일정이 등록되었습니다.');
	                addEventToCalendar(shareData);  
	                
	                const events = []; 
	                if (data != null && data.length > 0) {
				        for (let i = 0; i < data.length; i++) {
				        	
							const isAllDay = data[i].schedule_allDay === 1;
							events.push({
				                title: data[i].schedule_name, 
				                start: data[i].schedule_start, 
				                end: data[i].schedule_end,
				                backgroundColor: data[i].schedule_color, 
				                description: data[i].schedule_content,
				                allDay: isAllDay,
				                extendedProps: {
							        isShared: true, // 공유 일정
							    },
				            });
				        }
				    }

	                events.forEach(function(event) {
	                    $('#calendar').fullCalendar('addEvent', event); 
	                });
	                
	                window.location.href = '${pageContext.request.contextPath}/schedule/calender';
	            },
	            error: function() {
	                alert('공유 일정 등록 오류');
	            }
	        });
	    });
	    
	    function addEventToCalendar(eventData) {
	        const event = {
	            title: eventData.schedule_name,
	            start: eventData.startAt,
	            end: eventData.endAt,
	            backgroundColor: eventData.bgColor,
	            description: eventData.description,
	            allDay: eventData.allDay === 1
	        };
	    }
	    
	    function validateForm() {
	    	const $scheduleName = $("#schedule_name");
			const $start = $("#startAt"); 
			const $end = $("#endAt");
			const $shareEmp = $('#share_emp');
	
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
	        
	        if (!$shareEmp.val()) {
	            alert('공유자를 선택해주세요.');
	            return false;
	        }
	     
	        return true;
	    }
	    
	    $('#scheduleShareModal').on('hidden.bs.modal', function() {
	        $('input[name="selectedEmp"]').prop('checked', false); 
	    });
        
        function insertEmp() {
            const targetEmpId = localStorage.getItem('selectedEmpId');
            if (!targetEmpId) {
                alert('유효한 입력 필드가 선택되지 않았습니다.');
                return;
            }

            // 선택된 직원의 ID, 이름 가져오기
            const selectedEmpData = $('input[name="selectedEmp"]:checked').map(function() {
                return {
                    id: $(this).val(),
                    name: $(this).closest('tr').find('td:eq(2)').text().trim()
                };
            }).get();

            if (selectedEmpData.length === 0) {
                alert('직원 정보를 선택해 주세요.');
                return;
            }

            $('#share_emp').val(selectedEmpData.map(emp => emp.id).join(','));

            // 선택된 직원 이름
            const targetEmp = $('#' + targetEmpId);
            targetEmp.empty(); // 이전 선택 직원들을 제거
            selectedEmpData.forEach(emp => {
                const empEl = $('<span class="selected_emp mr-2">').text(emp.name);
                targetEmp.append(empEl);
            });

            alert('선택된 직원 : ' + selectedEmpData.map(emp => emp.name).join(', '));
            $('#scheduleShareModal').modal('hide');
        }
    </script>
</body>
</html>