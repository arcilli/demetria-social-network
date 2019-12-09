$(function () {
// jQuery shoul be loaded by bootstrap
    $(".editPostButton").on("click", function () {
        // make a request for
        console.log("Smth");
    });

    $(".deletePostButton").on("click", function () {
        var parents = $(this).parents().map(function () {
            if (this.tagName == "ARTICLE") {
                var arr = {id: this.id};
                // make the ajax request
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
                        alert("Something wrong");
                    }
                });
            }
        });
    });
});