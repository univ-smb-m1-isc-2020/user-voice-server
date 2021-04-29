ready(() => {
    document.querySelector("#RegisterButton").addEventListener("click", (e) => {
        let form = document.querySelector('#registerForm');
        if (!form.checkValidity()) {
            console.log("form not valid")
            e.preventDefault()
            e.stopPropagation()
        } else {
            let formData = new FormData(form);
            // Convert to a json object
            let content = serialize(formData);
            // if (typeof content !== "undefined") {
            //     message.payload = content;
            // }

            const URL = "https://user-voice-server.oups.net/register";

            console.log(content)

            const params = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                },
                body: JSON.stringify(content),
                mode: "cors"
            };

            fetch(URL, params)
                .then(response => response.json())
                .then(data => {
                    console.log('Success:', data);
                    toastr.success("Account successfully registered! You can now login!")
                })
                .catch((error) => {
                    console.error('Error:', error);
                    if (error != undefined) {
                        toastr.error("Failed to register!", error)
                    }
                });
        }

        form.classList.add('was-validated')
    });
});