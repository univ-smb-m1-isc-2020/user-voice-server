ready(() => {
    const URL = "https://user-voice-server.oups.net/site/get/user";
    const params = {
        method: "POST",
        headers: {
            "Authorization": sessionStorage.getItem('authorization')
        },
        mode: "cors"
    };
    var pager = {};
    pagerInit();
    refreshList();

    function refreshList() {
        fetch(URL, params)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                if (data.sucess == true) {
                    pager.items = JSON.parse(data.content);
                    pager.refresh();
                } else {
                    toastr.error(data.msg)
                }
                document.querySelector('#refreshListIcon').classList.remove('fa-spin');
                console.log('Success:', data);
                toastr.success("Successfully refreshed website list!")
            })
            .catch((error) => {
                console.error('Error:', error);
                if (error != undefined) {
                    toastr.error("Failed to fetch all websites!", error)
                }
                document.querySelector('#refreshListIcon').classList.remove('fa-spin');
            });
    }

    function bindList() {
        var pgItems = pager.pagedItems[pager.currentPage];
        var new_tbody = document.createElement('tbody');
        var old_tbody = document.getElementById("websiteList");
        new_tbody.id = old_tbody.id;
        for (var i = 0; i < pgItems.length; i++) {
            var tr = document.createElement('TR');
            var index = 0;
            for (var key in pgItems[i]) {
                var td = document.createElement('TD');
                if (++index == Object.keys(pgItems[i]).length) {
                    td.className = "text-end";
                    var a = document.createElement('a');
                    a.className = "btn btn-primary";
                    a.href = `/website/features.html?name=${pgItems[i]["nameSite"]}&apiKey=${pgItems[i]["apiKey"]}`;
                    a.innerHTML = `Features`;
                    td.appendChild(a);
                } else {
                    td.appendChild(document.createTextNode(pgItems[i][key]));
                }
                tr.appendChild(td);
            }
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
    document.querySelector("#refreshListBtn").addEventListener("click", function() {
        document.querySelector('#refreshListIcon').classList.add('fa-spin');
        refreshList();
    });
    document.querySelector("#submitSite").addEventListener("click", function(e) {
        let form = document.querySelector('#websiteForm');
        if (!form.checkValidity()) {
            console.log("form not valid");
            e.preventDefault();
            e.stopPropagation();
        } else {
            let formData = new FormData(form);
            // Convert to a json object
            let content = serialize(formData);
            console.log(content);

            const URL = "https://user-voice-server.oups.net/site/create";
            const params = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=UTF-8",
                    "Authorization": sessionStorage.getItem('authorization')
                },
                body: `{
                    "nameSite":"${content["siteNameInput"]}"
                }`,
                mode: "cors"
            };

            fetch(URL, params)
                .then(response => response.json())
                .then(data => {
                    if (data.sucess == true) {
                        console.log('Success:', data);
                        toastr.success("Your website was successfully added!");

                        const modal = bootstrap.Modal.getInstance(document.getElementById("addWebsiteModal"));
                        modal.hide();

                        document.querySelector('#refreshListIcon').classList.add('fa-spin');
                        refreshList();
                    } else {
                        console.log('Error:', data);
                        toastr.error("Error during website addition! Please try again or contact website administrator.", data);
                    }
                })
                .catch((error) => {
                    console.error('Error:', error);
                    if (error != undefined) {
                        toastr.error("Error during website addition!", error);
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