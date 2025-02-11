$(document).ready(function () {
    $.ajax({
        url: contextPath + "/api/birthday",
        method: "GET",
        dataType: "json",
        success: function (data) {
            console.log("success");
            let output = "";
            let len = data.length;
            let count = 0;
            if (data == null || data.length == 0) {
                output = "<p>오늘은 생일자가 없습니다</p>";
            } else {
                data.forEach(function (birthdayPerson) {
                    var empId = birthdayPerson.empId;
                    var imageUrl = "/api/emp/profile-image?empId=" + empId;

                    output += `
                        <div class="birthday-item">
                            <div class="left">
                                <img src="${imageUrl}" width="40px" style="border-radius: 50%" alt="프로필">
                            </div>
                            <div class="right">
                                <span class="ename">${birthdayPerson.ename}</span><br>
                                <span class="job">${birthdayPerson.jobName} / ${birthdayPerson.deptName}</span>
                            </div>
                        </div>
                    `;
                    count++;
                    if(count < len) output += `<hr style="margin:5px;">`
                });
                if (data.length > 2) $(".birthday-list").css("max-height", "140px").css("overflow-y", "auto");
                else $(".birthday-list").css("max-height", "none").css("overflow-y", "visible");
            }
            $(".birthday-list").html(output);
        },
        error: function () {
            console.error("birthday error");
        }
    });
});
