function updateInputState(input) {
    const val = input.value;
    if (!val) {
        input.parentElement.classList.add('empty');
    } else {
        input.parentElement.classList.remove('empty');
    }
}

// 페이지가 완전히 로드된 후에 실행
window.addEventListener('load', () => {
    const allInputs = document.querySelectorAll('input');
    for (const input of allInputs) {
        updateInputState(input);
        input.addEventListener('input', () => updateInputState(input));
    }
});

const usernameRegex = /^[a-z0-9]+$/;
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]+$/;
const fullNameRegex = /^[가-힣a-zA-Z]+$/;
const emailRegex = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
const phoneNumberRegex = /^[0-9]+$/;

$("#username").on("keyup", function () {
    const username = $("#username").val();
    if (username.length === 0) {
        $("#username_check").html("아이디를 입력해 주세요.").show();
    } else if (username.length < 5 || username.length > 20 || !usernameRegex.test(username)) {
        $("#username_check").html("5~20자의 영소문자, 숫자를 사용해 주세요.").show();
    } else {
        $("#username_check").hide();
    }
});

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

$("#fullName").on("keyup", function () {
    const fullName = $("#fullName").val();
    if (fullName.length === 0) {
        $("#fullName_check").html("이름을 입력해 주세요.").show();
    } else if (fullName.length < 2 || fullName.length > 10 || !fullNameRegex.test(fullName)) {
        $("#fullName_check").html("2~10자의 한글,영문을 사용해 주세요.").show();
    } else {
        $("#fullName_check").hide();
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

function JoinForm__submit() {
    const form = document.getElementById("joinForm");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const fullNameInput = document.getElementById("fullName");
    const emailNameInput = document.getElementById("email");
    const phoneNumberInput = document.getElementById("phoneNumber");
    const allCheckboxes = document.querySelectorAll(".toggle:checked");

    usernameInput.value = usernameInput.value.trim();
    passwordInput.value = passwordInput.value.trim();
    fullNameInput.value = fullNameInput.value.trim();
    emailNameInput.value = emailNameInput.value.trim();
    phoneNumberInput.value = phoneNumberInput.value.trim();

    if (usernameInput.value.length === 0) {
        usernameInput.focus();
        $("#username_check").html("아이디를 입력해 주세요.").show();
        return;
    }

    if (usernameInput.value.length < 5 || usernameInput.value.length > 20 || !usernameRegex.test(usernameInput.value)) {
        usernameInput.focus();
        $("#username_check").html("5~20자의 영소문자, 숫자를 사용해 주세요.").show();
        return;
    }

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

    if (fullNameInput.value.length === 0) {
        $("#fullName_check").html("이름을 입력해 주세요.").show();
        fullNameInput.focus();
        return;
    }

    if (fullNameInput.length < 2 || fullNameInput.value.length > 10 || !fullNameRegex.test(fullNameInput.value)) {
        $("#fullName_check").html("2~10자의 한글, 영문을 사용해 주세요.").show();
        fullNameInput.focus();
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

    if (allCheckboxes.length !== 2) {
        toastWarning('모든 약관에 동의해 주세요.');
        return;
    }

    form.submit();
}