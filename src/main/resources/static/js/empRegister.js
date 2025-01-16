const contextPath = /*[[@{/}]]*/ '';
$(document).ready(function () {
    const contextPath = /*[[@{/}]]*/ '';
    let fileChange = 0;

    // 파일 업로드 처리
    $("#file").change(function () {
        handleFileChange(this);
    });

    // 이미지 삭제 처리
    $(".remove_img").click(function () {
        handleFileDelete();
    });

    // 폼 제출 처리
    $("#registerForm").submit(function (event) {
        event.preventDefault();
        handleFormSubmit();
    });

    // 자격증 및 외국어 추가/삭제 버튼 처리 (이벤트 위임 사용)
    $("#certifications, #languages").on("click", ".add-cert, .add-lang, .remove-cert, .remove-lang", function () {
        handleDynamicField(this);
    });

    // 파일 변경 핸들러
    function handleFileChange(input) {
        fileChange++;
        const maxSizeInBytes = 5 * 1024 * 1024;
        const file = input.files[0];
        if (file.size > maxSizeInBytes) {
            alert("업로드 파일 용량 제한 : 5MB");
            $(input).val('');
            return;
        }
        const fileURL = URL.createObjectURL(file);
        $('#over_view').attr('src', fileURL);
    }

    // 파일 삭제 핸들러
    function handleFileDelete() {
        if (confirm("첨부파일을 삭제하시겠습니까?")) {
            $("#fileName").text('');
            $(".remove_img").css('display', 'none');
            $("#over_view").attr('src', '/image/profile_navy.png');
        }
    }

    // 폼 제출 핸들러
    function handleFormSubmit() {
        if (!confirm("등록하시겠습니까?")) {
            return;
        }

        // 필수 값 검증
        let isValid = validateForm();
        if (!isValid) return;

        const formData = new FormData(); // 파일과 JSON을 포함하는 FormData 생성
        const dto = gatherFormData(); // DTO 생성

        // JSON 데이터를 문자열로 변환하여 FormData에 추가
        formData.append("dto", JSON.stringify(dto));

        // 파일 추가
        const fileInput = $('input[name="file"]')[0];
        if (fileInput && fileInput.files.length > 0) {
            formData.append('file', fileInput.files[0]);
        }

        // AJAX 요청
        $.ajax({
            url: contextPath + "/api/admin/emp-manage",
            method: 'POST',
            processData: false,
            contentType: false,
            data: formData,
            success: function (data) {
                alert(data);
                location.reload();
            },
            error: function () {
                alert("등록 중 오류가 발생했습니다.");
            }
        });
    }

    // 동적 필드 추가/삭제 핸들러
    function handleDynamicField(button) {
        const isAddAction = $(button).hasClass('add-cert') || $(button).hasClass('add-lang');
        const targetId = $(button).closest('[id]').attr('id'); // certifications 또는 languages
        const fieldHtml = generateDynamicFieldHtml(targetId);

        if (isAddAction) {
            $(button).closest(`.${targetId === 'certifications' ? 'cert-add' : 'lang-add'}`).before(fieldHtml);
        } else {
            $(button).closest(`.${targetId === 'certifications' ? 'cert-item' : 'lang-item'}`).remove();
        }
    }

    // 유효성 검증
    function validateForm() {
        let isValid = true;
        $('#registerForm [required]').each(function () {
            if ($(this).val() === '') {
                const errorMessage = $(this).data('name') || $(this).attr('placeholder');
                alert(`${errorMessage || '필수 항목'}을(를) 입력해 주세요.`);
                $(this).focus();
                isValid = false;
                return false;
            }
        });
        return isValid;
    }

    // 폼 데이터 수집
    function gatherFormData() {
        const dto = {};
        $('#registerForm').find('input:not([type="file"]), select, textarea').each(function () {
            const name = $(this).attr('name');
            const value = $(this).val();

            if (!name) return;

            if (name === 'lang' || name === 'cert') {
                if (!dto[name]) dto[name] = [];
                dto[name].push({
                    empId: $("input[name='empId']").val(),
                    [name === 'lang' ? 'langName' : 'certName']: value
                });
            } else if ($(this).is(':checkbox')) {
                dto[name] = $(this).is(':checked');
            } else if ($(this).is(':radio')) {
                if ($(this).is(':checked')) {
                    dto[name] = value;
                }
            } else {
                dto[name] = value;
            }
        });
        return dto;
    }

    // 동적 필드 HTML 생성
    function generateDynamicFieldHtml(targetId) {
        if (targetId === 'certifications') {
            return `
                <div class="cert-item">
                    <input type="text" name="cert" class="form-control cert-input" placeholder="자격증명" required>
                    <button type="button" class="btn btn-sm btn-light remove-cert">-</button>
                </div>`;
        } else if (targetId === 'languages') {
            return `
                <div class="lang-item">
                    <input type="text" name="lang" class="form-control lang-input" placeholder="외국어명" required>
                    <button type="button" class="btn btn-sm btn-light remove-lang">-</button>
                </div>`;
        }
    }
});
