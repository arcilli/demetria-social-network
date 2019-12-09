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
});