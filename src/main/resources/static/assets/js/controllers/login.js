getCookiesAuth();

ready(() => {
    document.querySelector("#LoginSubmit").addEventListener("click", (e) => {
        let form = document.querySelector('#loginForm');
        if (!form.checkValidity()) {
            console.log("form not valid")
            e.preventDefault()
            e.stopPropagation()
        } else {
            let formData = new FormData(form);
            // Convert to a json object
            let content = serialize(formData);
            console.log(content)

            const URL = "https://user-voice-server.oups.net/login";
            const FORWARD_URL = "/website/profil.html";
            const AUTH = encodeCredentials(content["email"], content["password"])

            const params = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=UTF-8",
                    "Authorization": AUTH
                },
                mode: "cors"
            };

            fetch(URL, params)
                .then(response => response.json())
                .then(data => {
                    if (data.sucess == true) {
                        console.log('Success:', data);
                        console.log(content["remember-me"])
                        if (content["remember-me"] == 1) {
                            document.cookie = "account=" + data.content;
                            document.cookie = "authorization=" + AUTH;
                            console.log(document.cookie);
                        } else {
                            document.cookie = "account= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
                            document.cookie = "authorization= ; expires = Thu, 01 Jan 1970 00:00:00 GMT";
                            sessionStorage.setItem('account', data.content);
                            sessionStorage.setItem('authorization', AUTH);
                            console.log('session:', sessionStorage.getItem('account'));
                            console.log('session:', sessionStorage.getItem('authorization'));
                        }
                        window.location.href = FORWARD_URL;
                        toastr.success("Successfully logged-in !")
                    } else {
                        console.log('Error:', data);
                        toastr.error("Failed to login! Please, check your credentials.", data)
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    if (error != undefined) {
                        toastr.error("Failed to login!", error)
                    }
                });
        }

        form.classList.add('was-validated')
    });
});

function getCookiesAuth() {
    let account = getCookie("account");
    let auth = getCookie("authorization");
    if ((account != undefined) && (auth != undefined)) {
        sessionStorage.setItem('account', account);
        sessionStorage.setItem('authorization', auth);
        window.location.href = "/website/profil.html";
    }
}