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
    $('posts-wrapper').on('click', '.deletePostButton', function () {
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

    // Ajax request for adding a comment
    // The button is a part of a form.
    $('.posts-wrapper').on('click', '.addCommentButton', function (event) {
        event.preventDefault();

        let formId = -1;

        // Find the element that represents the current form.
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
            }, error: function (result) {
                // TODO: display a modal with the error.
                console.log("Error at posting the comment: " + result);
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
    $('.posts-wrapper').on('click', '.stars a', function () {
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
            }, error: function (result) {
                console.log("Error ar voting: " + result);
            }
        })
    });

    $('.follow-button').on('click', function () {
        let userNameToFollow = extractUsernameFromLocationPath();
        let targetUrl = "/follow/user/" + userNameToFollow;
        let sourceButton = this;
        $.ajax({
            url: targetUrl,
            type: 'GET',
            success: function (result) {
                sourceButton.innerText = "Unfollow";
                sourceButton.classList.add("active", "unfollow-button");
                sourceButton.classList.remove("selected", "follow-button");
            }, error: function (result) {
                console.log("Error at following: " + result);
            }
        })
    });

    $('.unfollow-button').on('click', function () {
        let userNameToUnfollow = extractUsernameFromLocationPath();
        let targetUrl = "/follow/cancel/" + userNameToUnfollow;
        let sourceButton = this;
        $.ajax({
            url: targetUrl,
            type: 'POST',
            success: function (result) {
                sourceButton.innerText = "Follow";
                sourceButton.classList.add("follow-button");
                sourceButton.classList.remove("active", "unfollow-button");
            }, error: function (result) {
                console.log("Error at unfollowing: " + result);
            }
        })
    });

    var extractUsernameFromLocationPath = function () {
        let pathname = window.location.pathname.split("/");
        let userName = pathname[pathname.length - 1];
        return userName;
    }

    /**
     * Used in timeline, for displaying more posts.
     */
    $('.show-more-button').on('click', function () {
        let targetUrl = "/timeline/showMore";
        $.ajax({
            url: targetUrl,
            type: 'POST',
            contentType: 'text/plain',
            data: getLastShowedPostId(),
            success: function (result) {
                $(result).insertBefore('.show-more-button');
            }, error: function (result) {
                console.log("Error at retrieving more posts: " + error);
            }
        })
    });

    // TODO: Make this work.
    var getLastShowedPostId = function () {
        let posts = $('article');
        let lastId = 0;
        if (0 != posts.length) {
            // Get the id of the last post that has been showed.
            console.log(posts[posts.length - 1])
            lastId = posts[posts.length - 1].id;
        }
        return lastId + "";
    }

    $('.profile-show-more-button').on('click', function () {
        let targetUrl = "/timeline/showMore/user/" + extractUsernameFromLocationPath();
        $.ajax({
            url: targetUrl,
            type: 'POST',
            contentType: 'text/plain',
            data: getLastShowedPostId(),
            success: function (result) {
                $(result).insertBefore('.profile-show-more-button');
            },
            error: function (result) {
                console.log("Error at retrieving more posts: " + result);
            }
        })
    });

    $('.top-search-form').on('submit', function (event) {
        // event.preventDefault();
        // let form = this;

        // $.ajax({
        //     url: form.action,
        //     type: 'POST',
        //     contentType: 'text/plain',
        //     data: this[0].value,
        //     success: function (result) {
        //         console.log("succes");
        //     },
        //     error: function (result) {
        //         console.log("error");
        //     }
        // });
    });
});
