const allInputs = document.querySelectorAll('input');
for (const input of allInputs) {
    input.addEventListener('input', () => {
        const val = input.value
        if (!val) {
            input.parentElement.classList.add('empty')
        } else {
            input.parentElement.classList.remove('empty')
        }
    })
}

$("#username").on("keyup", function () {
    const username = $("#username").val();
    if (username.length === 0) {
        $("#username_check").html("아이디를 입력해 주세요.").show();
    } else {
        $("#username_check").hide();
    }
});

$("#password").on("keyup", function () {
    const password = $("#password").val();
    if (password.length === 0) {
        $("#password_check").html("비밀번호를 입력해 주세요.").show();
    } else {
        $("#password_check").hide();
    }
});

function LoginForm__submit() {
    const form = document.getElementById("loginForm");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    usernameInput.value = usernameInput.value.trim();

    if (usernameInput.value.length === 0) {
        usernameInput.focus();
        $("#username_check").html("아이디를 입력해 주세요.").show();
        return;
    }

    passwordInput.value = passwordInput.value.trim();

    if (passwordInput.value.length === 0) {
        $("#password_check").html("비밀번호를 입력해 주세요.").show();
        passwordInput.focus();
        return;
    }

    form.submit();
}