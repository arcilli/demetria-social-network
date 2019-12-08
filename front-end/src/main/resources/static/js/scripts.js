$(function () {
// jQuery shoul be loaded by bootstrap
    $(".editPostButton").on("click", function () {
        // make a request for
        console.log("Smth");
    });

    $(".deletePostButton").on("click", function () {
        var parents = $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                // make the ajax request
                $.ajax({
                    url: "/deletePost",
                    type: 'DELETE',
                    data: {
                        "postId": this.id
                    },
                    success: function (result) {
                        console.log(result);
                    }
                });
            }
        });
    });
});