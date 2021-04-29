if (sessionStorage.getItem('authorization') == null || sessionStorage.getItem('account') == null) {
    let account = getCookie("account");
    let auth = getCookie("authorization");
    if ((account != undefined) && (auth != undefined)) {
        sessionStorage.setItem('account', account);
        sessionStorage.setItem('authorization', auth);
    } else {
        window.location.href = "/website/login.html";
    }
}

const userData = JSON.parse(sessionStorage.getItem('account'));
console.log(userData);
document.getElementById("usernameNavSpan").innerHTML = userData.user.username;
updateVoteCount();

document.getElementById("logoutLink").addEventListener("click", function() {
    sessionStorage.removeItem('account');
    sessionStorage.removeItem('authorization');
    document.cookie = "account= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "authorization= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
    window.location.href = "/website/login.html";
});