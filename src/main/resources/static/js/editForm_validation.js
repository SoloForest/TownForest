const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]+$/;
const emailRegex = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
const phoneNumberRegex = /^[0-9]+$/;

$("#password").on("keyup", function () {
    const password = $("#password").val();
    if (password.length === 0) {
        $("#password_check").html("비밀번호를 입력해 주세요.").show();
    } else if (password.length < 8 || password.length > 15 || !passwordRegex.test(password)) {
        $("#password_check").html("8~15자의 영문, 숫자, 특수문자를 사용해 주세요.").show();
    } else {
        $("#password_check").hide();
    }
});

$("#email").on("keyup", function () {
    const email = $("#email").val();
    if (email.length === 0) {
        $("#email_check").html("이메일을 입력해 주세요.").show();
    } else if (!emailRegex.test(email)) {
        $("#email_check").html("이메일 주소가 정확한지 확인해 주세요.").show();
    } else {
        $("#email_check").hide();
    }
});

$("#phoneNumber").on("keyup", function () {
    const phoneNumber = $("#phoneNumber").val();
    if (phoneNumber.length === 0) {
        $("#phoneNumber_check").html("휴대전화번호를 입력해 주세요.").show();
    } else if (phoneNumber.length < 10 || !phoneNumberRegex.test(phoneNumber)) {
        $("#phoneNumber_check").html("휴대전화번호가 정확한지 확인해 주세요.").show();
    } else {
        $("#phoneNumber_check").hide();
    }
});

function EditForm_submit() {
    const form = document.getElementById("editForm");
    const passwordInput = document.getElementById("password");
    const emailNameInput = document.getElementById("email");
    const phoneNumberInput = document.getElementById("phoneNumber");

    passwordInput.value = passwordInput.value.trim();
    emailNameInput.value = emailNameInput.value.trim();
    phoneNumberInput.value = phoneNumberInput.value.trim();

    if (passwordInput.value.length === 0) {
        $("#password_check").html("비밀번호를 입력해 주세요.").show();
        passwordInput.focus();
        return;
    }

    if (passwordInput.value.length < 8 || passwordInput.value.length > 15 || !passwordRegex.test(passwordInput.value)) {
        $("#password_check").html("8~15자의 영문, 숫자, 특수문자를 사용해 주세요.").show();
        passwordInput.focus();
        return;
    }

    if (emailNameInput.value.length === 0) {
        $("#email_check").html("이메일을 입력해 주세요.").show();
        emailNameInput.focus();
        return;
    }

    if (!emailRegex.test(emailNameInput.value)) {
        $("#email_check").html("이메일 주소가 정확한지 확인해 주세요.").show();
        emailNameInput.focus();
        return;
    }

    if (phoneNumberInput.value.length === 0) {
        $("#phoneNumber_check").html("휴대전화번호를 입력해 주세요.").show();
        phoneNumberInput.focus();
        return;
    }

    if (phoneNumberInput.value.length < 10 || !phoneNumberRegex.test(phoneNumberInput.value)) {
        $("#phoneNumber_check").html("휴대전화번호가 정확한지 확인해 주세요.").show();
        phoneNumberInput.focus();
        return;
    }

    form.submit();
}

const header = $("meta[name='_csrf_header']").attr('content');
const token = $("meta[name='_csrf']").attr('content');

$("#toggleClick").on("click", function () {
    $("#confirmPassword").val("");
    $("#confirmPassword_check").html("").hide();
});

$("#confirm").on("click", function () {
    const confirmPassword = $("#confirmPassword").val();
    $.ajax({
        url: '/account/confirmPwd',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        type: 'POST',
        data: {'password': confirmPassword},
        success: function (data) {
            if (data) {
                document.getElementById("modalClick").click();
                document.getElementById("editClick").click();
            } else {
                $("#confirmPassword_check").html("비밀번호가 올바르지 않습니다.").show();
            }
        }, error: function (error) {
            $("#confirmPassword_check").html("비밀번호를 입력해 주세요.").show();
            alert(JSON.stringify(error));
        }
    });
});

function withdraw() {
    if (confirm('정말 탈퇴하시겠습니까?\n이 작업은 되돌릴 수 없습니다.')) {
        document.querySelector('#withdrawForm').submit();
        alert('정상적으로 회원 탈퇴가 처리되었습니다.\n그동안 저희 서비스를 이용해주셔서 감사합니다.');
    }
}