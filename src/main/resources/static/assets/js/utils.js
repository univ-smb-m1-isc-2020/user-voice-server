// Callback to notify DOM loaded event
var ready = (callback) => {
    if (document.readyState != "loading") callback();
    else document.addEventListener("DOMContentLoaded", callback);
}

// Serialize a FormData object to key/value array
function serialize(data) {
    let obj = {};
    for (let [key, value] of data) {
        if (obj[key] !== undefined) {
            if (!Array.isArray(obj[key])) {
                obj[key] = [obj[key]];
            }
            obj[key].push(value);
        } else {
            obj[key] = value;
        }
    }
    return obj;
}

// Encoder function used to create Authorization header for request 
function encodeCredentials(username, password) {
    let sequence = `${username}:${password}`;
    return `Basic ${btoa(sequence)}`;
}

// Update vote counter located in navbar from api data
function updateVoteCount() {
    const URL = "https://user-voice-server.oups.net/features/getNumberVoteToday"
    const params = {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
            "Authorization": sessionStorage.getItem('authorization')
        },
        mode: "cors"
    };
    fetch(URL, params)
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            if (data.sucess == true) {
                document.getElementById("voteCount").innerHTML = 10 - parseInt(data.content);
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

// Check if the requested cookie exist and return it if yes
function getCookie(name) {
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    console.log("match: " + match);
    if (match) return match[2];
}