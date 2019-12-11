$(function () {
// jQuery is alreadyloaded by bootstrap
    $(".editPostButton").on("click", function () {
        // make a request for change the current post
    });

    $(".deletePostButton").on("click", function () {
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                var arr = {id: this.id};
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

    // modify the request to use ajax
    $(".addCommentButton").on("click", function () {
        let form = -2;
        let postId = -1;
        $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                postId = this.id;
            } else if (this.tagName == "FORM") {
                form = this;
            }
        });
        // form.submit(function (e) {
        //     form.preventDefault();
        //     // add necessarry posts
        //     form.submit();
        // });
    });

    // To be tested
    var getIdForPost = function (elementId) {
        $(elementId).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                return this.id;
            }
        });
    }
});