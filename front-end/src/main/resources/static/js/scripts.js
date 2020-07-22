// Disabling form submissions if there are invalid fields
(function () {
    'use strict';
    window.addEventListener('load', function () {
        // Fetch all the forms we want to apply custom Bootstrap validation styles to
        let forms = document.getElementsByClassName('needs-validation');
        // Loop over them and prevent submission
        let validation = Array.prototype.filter.call(forms, function (form) {
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

$(function (events, handler) {
// TODO: make a request for change the current post
    $(".editPostButton").on("click", function () {
    });

    $("#confirmAccountDeleteButton").on("click", function () {
        $.ajax({
            url: "/profiles/deleteAccount",
            type: 'DELETE',
            contentType: 'application/json',
            // an error needs to be treated as a success case (since a redirect header is returned)
            error: function () {
                window.location.replace("/");
            }
        });
    });

    let postsWrapper = $(".posts-wrapper");
    // Rating stars
    postsWrapper.on('click', '.stars a', function (event) {
        event.preventDefault();
        let articleId = -1;
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                articleId = this.id;
            }
        });
        // For each star, if it is <= this, color it.
        articleId = "#" + articleId;
        let currentPosts = $(articleId);
        let nodeItems = currentPosts.find(".stars")[0].children;
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

    // Delete a post
    postsWrapper.on('click', '.deletePostButton', function () {
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                var arr = {id: this.id};
                // ajax request for deleting a post
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
                    error: function (result) {
                        console.log(result);
                        alert("Something went wrong");
                    }
                });
            }
        });
    });

    // Ajax request for adding a comment.
    // The button is a part of a form.
    postsWrapper.on('click', '.addCommentButton', function (event) {
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
        // Do nothing if the comment is empty;
        if ("" === form[0][0].value) {
            return;
        }
        $.ajax({
            url: form[0].action,
            type: 'post',
            data: form.serialize(),
            success: function (result) {
                if (result.length != 0) {
                    let name = result.owner.firstName + " " + result.owner.lastName;
                    let profileURL = "/profiles/" + result.owner.userName;
                    let commentContent = result.content;
                    let parentDiv = '<div class="post-comment"><div>';
                    let date = new Date();
                    const monthNames = ["January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"
                    ];
                    let formattedDate = date.getDate() + " " + monthNames[date.getMonth()].substr(0, 3) + " " + date.getHours() + ":" + date.getMinutes()
                    formattedDate += ": ";
                    let formattedContent = "<a href=\"" + profileURL + "\">" + name + "</a> on " + formattedDate + commentContent;
                    formattedContent = parentDiv + formattedContent + "</div></div>";
                    $(formattedContent).insertBefore('#' + formId);
                    form[0][0].value = "";
                }
            }, error: function (result) {
                // TODO: display a modal with the error.
                console.log("Error at posting the comment: " + result);
            }
        })
    });

    // Auto-height for comments.
    postsWrapper.on('input', 'textarea', function () {
        this.style.height = 'auto';
        this.style.height = (this.scrollHeight) + 'px';
    });

    $('.profile-follow-unfollow-button-wrapper')
        .on('click', '.follow-button', followUser)
        .on('click', '.unfollow-button', unfollowUser);

    $('.single-post.button-wrapper')
        .on("click", '.follow-button', followUserFromSinglePost)
        .on("click", '.unfollow-button', unfollowUserFromSinglePost);

    $('.user-list-wrapper')
        .on('click', '.follow-button', followUser)
        .on('click', '.unfollow-button', unfollowUser);

    $('.show-more-button').on('click', showMoreFromNewsFeed);
    $('.profile-show-more-button').on('click', showMoreFromUserProfile);

    $('.profile-picture-uploader').on('change', function () {
        let pictureUploaderForm = this;
        readFileAsBase64(this.files[0], function (e) {
            let base64Image = e.target.result;
            resizeImage(base64Image, function (result) {
                let resizedImage = result;
                let form = new FormData();
                form.append('profilePicture', resizedImage);
                let targetUrl = "/settings/changeProfilePhoto";
                let elements = pictureUploaderForm.id.split("-");
                let userId = elements[elements.length - 1];
                $.ajax({
                    url: targetUrl,
                    type: 'POST',
                    cache: false,
                    contentType: false,
                    processData: false,
                    data: form,
                    success: function (data) {
                        $("#pp-" + userId)[0].src = data;
                    },
                    error: function (data) {
                        console.log(data);
                    }
                });
            });
        });
    });

    // Prevent the user to perform a search with an empty query.
    $('.top-search-form').on('submit', function (event) {
        if ("" === this[0].value) {
            event.preventDefault();
        }
    });

    // Prevent the user to create a post with empty content.
    $('.create-post-button').on('click', function (event) {
        Array.from($('.create-post-button').siblings()).forEach(sibling => {
            if ("TEXTAREA" === sibling.tagName && "" === sibling.value) {
                event.preventDefault();
            }
        })
    });
});

/**
 * Used in timeline, for displaying more posts.
 */
let showMoreFromNewsFeed = function () {
    let targetUrl = "/timeline/showMore";
    let lastShowedId = getLastShowedPostId();
    $.ajax({
        url: targetUrl,
        type: 'POST',
        contentType: 'text/plain',
        data: lastShowedId,
        success: function (result) {
            $(result).insertBefore('.show-more-button');
        }, error: function (result) {
            console.log("Error at retrieving more posts: " + result);
        }
    }).then(r => {
    });
};

let showMoreFromUserProfile = function (doneCallback, ...doneCallbacks) {
    let targetUrl = "/timeline/showMore/user/" + extractUsernameFromLocationPath();
    let lastShowedId = getLastShowedPostId();
    $.ajax({
        url: targetUrl,
        type: 'POST',
        contentType: 'text/plain',
        data: lastShowedId,
        success: function (result) {
            if ("noMorePosts" !== lastShowedId) {
                $(result).insertBefore('.profile-show-more-button');
            }
        },
        error: function (result) {
            console.log("Error at retrieving more posts: " + result);
            console.log("Result: " + result.responseText);
        }
    }).then(r => {
    });
};

let getLastShowedPostId = function getLastShowedPostId() {
    let posts = $('article');
    let lastId = 0;
    if (0 != posts.length) {
        // Get the id of the last post that has been showed.
        lastId = posts[posts.length - 1].id;
    }
    return lastId + "";
};

let extractUsernameFromLocationPath = function () {
    return extractUsernameFromPath(window.location.pathname);
};

let extractUsernameFromPath = function (path) {
    let pathElements = path.split("/");
    let userName = "";
    pathElements.forEach(function (element, i) {
        if (element === "profiles") {
            userName = pathElements[i + 1];
        }
    });
    return userName;
}

let followUser = function () {
    let userNameToFollow = "";
    let sourceButton = this;
    if (this.classList.contains('notSelf')) {
        let userSpan = sourceButton.parentNode.previousElementSibling.children[1].firstElementChild;
        userNameToFollow = extractUsernameFromPath(userSpan.attributes.href.nodeValue);
    } else {
        userNameToFollow = extractUsernameFromLocationPath();
    }
    performFollowUserRequest(sourceButton, userNameToFollow);
};

let followUserFromSinglePost = function () {
    let userNameToFollow = $(".profile-username")[0].innerText.substring(1);
    let sourceButton = this;
    performFollowUserRequest(sourceButton, userNameToFollow);
}

let performFollowUserRequest = function (sourceButton, userNameToFollow) {
    let targetUrl = "/follow/user/" + userNameToFollow + "/";
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
    });
}

let unfollowUser = function () {
    let userNameToUnfollow = "";
    let sourceButton = this;
    if (this.classList.contains('notSelf')) {
        let userSpan = sourceButton.parentNode.previousElementSibling.children[1].firstElementChild;
        userNameToUnfollow = extractUsernameFromPath(userSpan.attributes.href.nodeValue);
    } else {
        userNameToUnfollow = extractUsernameFromLocationPath();
    }
    performUnfollowUserRequest(sourceButton, userNameToUnfollow);
};

let unfollowUserFromSinglePost = function () {
    let userNameToUnfollow = $(".profile-username")[0].innerText.substring(1);
    let sourceButton = this;
    performUnfollowUserRequest(sourceButton, userNameToUnfollow);
}

let performUnfollowUserRequest = function (sourceButton, userNameToUnfollow) {
    let targetUrl = "/follow/cancel/" + userNameToUnfollow + "/";
    $.ajax({
        url: targetUrl,
        type: 'POST',
        success: function () {
            sourceButton.innerText = "Follow";
            sourceButton.classList.add("follow-button");
            sourceButton.classList.remove("active", "selected", "unfollow-button");
        }, error: function (result) {
            console.log("Error at unfollowing: " + result);
        }
    });
}

let readFileAsBase64 = function (file, callbackFunction) {
    let reader = new FileReader();
    reader.onload = callbackFunction;
    reader.readAsDataURL(file);
}

const MAX_WIDTH_PROFILE_PICTURE = 3840;
const MAX_HEIGHT_PROFILE_PICTURE = 2160;

let resizeImage = function (base64Image, callbackFunction) {
    let image = new Image();
    let canvas = document.createElement("canvas");
    let ctx = canvas.getContext("2d");
    image.onload = function (e) {
        let width = this.width;
        let height = this.height;

        while (width > MAX_WIDTH_PROFILE_PICTURE || height > MAX_HEIGHT_PROFILE_PICTURE) {
            width = width / 1.618033;
            height = height / 1.618033;
        }
        // Resize the canvas and draw the image data into it.
        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(this, 0, 0, width, height);
        callbackFunction(canvas.toDataURL("image/jpeg", 0.7));
    }
    image.src = base64Image;
}