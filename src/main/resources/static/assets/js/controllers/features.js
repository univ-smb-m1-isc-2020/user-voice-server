ready(() => {
    const URL_PARAMS = new URLSearchParams(window.location.search);
    const SITE_NAME = URL_PARAMS.get('name')
    const URL = "https://user-voice-server.oups.net/features/getAllFromWebSite"
    const params = {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
            "Authorization": sessionStorage.getItem('authorization')
        },
        body: `{"apiKey":"${URL_PARAMS.get('apiKey')}"}`,
        mode: "cors"
    };
    var pager = {};
    pagerInit();
    fetchFeatures();

    function fetchFeatures() {
        fetch(URL, params)
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                if (data.content !== "" && data.content !== undefined) {
                    pager.items = JSON.parse(data.content);
                    pager.refresh();
                } else {
                    toastr.error("No features available for this company!")
                }
                toastr.success("Successfully fetched all features !")
                document.querySelector('#refreshFeaturesIcon').classList.remove('fa-spin');
            })
            .catch((error) => {
                console.error('Error:', error);
                if (error != undefined) {
                    toastr.error("Failed to fetch features!", error)
                }
                document.querySelector('#refreshFeaturesIcon').classList.remove('fa-spin');
            });
    }

    function bindList() {
        var pgItems = pager.pagedItems[pager.currentPage];
        var new_tbody = document.createElement('tbody');
        var old_tbody = document.getElementById("featuresList");
        new_tbody.id = old_tbody.id;
        for (var i = 0; i < pgItems.length; i++) {
            var tr = document.createElement('TR');
            var index = 0;
            for (var key in pgItems[i]) {
                if (++index > 4)
                    break;
                var td = document.createElement('TD')
                td.appendChild(document.createTextNode(pgItems[i][key]));
                tr.appendChild(td);
            }
            var td = document.createElement('TD')
            td.className = "text-end";

            var btn = document.createElement('button')
            btn.className = "btn btn-primary";
            btn.innerHTML = `Vote`;
            btn.addEventListener("click", function(e) {
                /* Vote query */
                vote(e.currentTarget.featureId);
                this.classList.remove("btn-primary");
                this.classList.add("btn-success");
                this.innerHTML = "Done !";
                this.disabled = true;
            });
            btn.featureId = pgItems[i]["id"];

            td.appendChild(btn);
            tr.appendChild(td);
            new_tbody.appendChild(tr);
        }
        old_tbody.parentNode.replaceChild(new_tbody, old_tbody);
        document.getElementById("pageNumber").innerHTML = pager.currentPage + 1;
        updateTableInfo();
    }

    function prevPage() {
        pager.prevPage();
        bindList();
    }

    function nextPage() {
        pager.nextPage();
        bindList();
    }

    function setPagination(nbElem) {
        pager.itemsPerPage = +nbElem;
        pager.refresh();
    }

    function updateTableInfo() {
        let firstElem = pager.currentPage * pager.itemsPerPage + 1;
        let lastElem = 0;
        if (pager.currentPage === pager.pagedItems.length - 1) {
            lastElem = pager.items.length;
        } else {
            lastElem = firstElem + pager.itemsPerPage - 1;
        }
        document.getElementById("dataTable_info").innerHTML = "Showing " + firstElem + " to " + lastElem + " of " + pager.items.length;
    }

    function pagerInit() {
        pager.pagedItems = [];
        pager.currentPage = 0;
        if (pager.itemsPerPage === undefined) {
            pager.itemsPerPage = 25;
        }
        pager.prevPage = function() {
            if (pager.currentPage > 0) {
                pager.currentPage--;
            }
        };
        pager.nextPage = function() {
            if (pager.currentPage < pager.pagedItems.length - 1) {
                pager.currentPage++;
            }
        };
        pager.refresh = function() {
            pager.currentPage = 0;
            pager.pagedItems = [];
            for (var i = 0; i < pager.items.length; i++) {
                if (i % pager.itemsPerPage === 0) {
                    pager.pagedItems[Math.floor(i / pager.itemsPerPage)] = [pager.items[i]];
                } else {
                    pager.pagedItems[Math.floor(i / pager.itemsPerPage)].push(pager.items[i]);
                }
            }
            bindList();
        };
    }

    // Controllers
    /// Buttons
    document.querySelector("#refreshFeaturesBtn").addEventListener("click", function() {
        document.querySelector('#refreshFeaturesIcon').classList.add('fa-spin');
        fetchFeatures();
    });
    document.querySelector("#submitFeature").addEventListener("click", function(e) {
        let form = document.querySelector('#featureForm');
        if (!form.checkValidity()) {
            console.log("form not valid");
            e.preventDefault();
            e.stopPropagation();
        } else {
            let formData = new FormData(form);
            // Convert to a json object
            let content = serialize(formData);
            console.log(content);

            const URL = "https://user-voice-server.oups.net/features/create";
            const params = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                },
                body: `{
                    "apiKey":"${URL_PARAMS.get('apiKey')}",
                    "feature":{
                        "text":"${content["featureDescInput"]}",
                        "emailAuthor":"${userData.user.email}"
                    }
                }`,
                mode: "cors"
            };

            fetch(URL, params)
                .then(response => response.json())
                .then(data => {
                    if (data.sucess == true) {
                        console.log('Success:', data);
                        toastr.success("Your feature was successfully submitted!");

                        const modal = bootstrap.Modal.getInstance(document.getElementById("addFeatureModal"));
                        modal.hide();

                        document.querySelector('#refreshFeaturesIcon').classList.add('fa-spin');
                        fetchFeatures();
                    } else {
                        console.log('Error:', data);
                        toastr.error("Error during feature submission! Please try again or contact website administrator.", data);
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    if (error != undefined) {
                        toastr.error("Error during feature submission!", error);
                    }
                });
        }

        form.classList.add('was-validated')
    });
    document.querySelector("#prevPageBtn").addEventListener("click", function() {
        prevPage();
    });
    document.querySelector("#nextPageBtn").addEventListener("click", function() {
        nextPage();
    });

    /// Select
    document.querySelector("#elementsPerPageSelect").addEventListener("change", function() {
        setPagination(this.value);
    });
});

function vote(id) {
    const URL = "https://user-voice-server.oups.net/features/voteForFeature"
    const params = {
        method: "POST",
        headers: {
            "Content-Type": "application/json; charset=UTF-8",
            "Authorization": sessionStorage.getItem('authorization')
        },
        body: `{"id":"${id}"}`,
        mode: "cors"
    };
    fetch(URL, params)
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            if (data.sucess == true) {
                toastr.success("Successfully voted !")
            } else {
                toastr.error("Failed to vote. Reason:", data.content)
            }
            updateVoteCount();
        })
        .catch((error) => {
            console.error('Error:', error);
            if (error != undefined) {
                toastr.error("Failed to vote!", error)
            }
        });
}