// Disabling form submissions if there are invalid fields
(function () {
    'use strict';
    window.addEventListener('load', function () {
        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        var forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        var validation = Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

$(function () {
    // TODO: make a request for change the current post
    $(".editPostButton").on("click", function () {
    });

    // ajax request for deleting a post
    $(".deletePostButton").on("click", function () {
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                var arr = { id: this.id };
                $.ajax({
                    url: "/deletePost",
                    type: 'DELETE',
                    contentType: 'application/json',
                    data: JSON.stringify(arr),
                    success: function (result) {
                        if (result == true) {
                            $('#' + arr.id).remove();
                        }
                    },
                    error: function () {
                        alert("Something went wrong");
                    }
                });
            }
        });
    });

    // ajax request for adding a comment
    $(".addCommentButton").on("click", function (event) {
        event.preventDefault();

        let formId = -2;
        $(this).parents().map(function () {
            if (this.tagName == "FORM") {
                formId = this.id.value;
            }
        });

        formId = formId + "CommForm";
        let form = $("#" + formId);
        $.ajax({
            url: form[0].action,
            type: 'post',
            data: form.serialize(),
            success: function (result) {
                if (result.length != 0) {
                    let name = result.owner.firstName + " " + result.owner.lastName;
                    let profileURL = "/profiles/" + result.owner.userName;
                    let commentContent = result.content;
                    let element = "<a href=\"" + profileURL + "\">" + name + "</a>: " + commentContent;
                    form.before(element);
                    form[0][0].value = "";
                }
            }
        })
    });

    // To be tested.
    var getIdForPost = function (elementId) {
        $(elementId).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                return this.id;
            }
        });
    };

    // To be tested, as well
    var getFirstIdFoundAscendent = function (element) {
        var parent = element.parentNode;
        while (parent.id == "") {
            parent = parent.parentNode;
        }
        return parent.id;
    };

    $("#confirmAccountDeleteButton").on("click", function () {
        $.ajax({
            url: "/deleteAccount",
            type: 'DELETE',
            contentType: 'application/json',
            // an error needs to be treated as a success case (since a redirect header is returned)
            error: function () {
                window.location.replace("/");
            }
        });
    });

    // Rating stars
    $('.stars a').on('click', function () {
        let articleId = -1;
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                articleId = this.id;
            }
        });
        // For each star, if it is <= this, color it.
        articleId = "#" + articleId;
        let currentPosts = $(articleId);
        let nodeItems = currentPosts.find(".stars")[0].children
        for (let i = 0; i < nodeItems.length; ++i) {
            if (nodeItems[i].innerText <= this.innerText) {
                nodeItems[i].classList.add('active');
            } else {
                nodeItems[i].classList.remove("active");
            }
        }
        articleId = articleId.substr(1);
        let targetUrl = "/votePost/" + articleId + "/" + this.innerText;
        $.ajax({
            url: targetUrl,
            type: 'POST',
            success: function (result) {
                // The result is equal to post's rank value after computing the vote.
                // -1 is the value for an error.
                if (-1 != result) {
                    currentPosts.find(".postRanking")[0].innerText = "Post rank: " + result.toFixed(1);
                }
            },
            error: function (result) {
                console.log("Error ar voting: " + result);
            }
        })
    });

    $('.follow-button').on('click', function () {
        let pathname = window.location.pathname.split("/");
        let userNameToFollow = pathname[pathname.length - 1];
        let targetUrl = "/follow/user/" + userNameToFollow;
        let sourceButton = this;
        $.ajax({
            url: targetUrl,
            type: 'GET',
            success: function (result) {
                sourceButton.innerText = "Unfollow";
                sourceButton.classList.add("active").remove("selected");
            }
        })
    });
});
