function initAssignmentFilter() {
    var filter = $("#filterAssignmentInput");

    $(filter).unbind('keyup');

    filter.keyup(function () {
        filterAssignmentList();
    });

    filter.keyup();
}

function filterAssignmentList() {
    var q = $.trim($("#filterAssignmentInput").val()).toLowerCase();

    $(".project_users").find(".row").each(function (idx, element) {
        var row = $(element);
        var showRow = false;

        row.find(".filterable").each(function (i, filterable) {
            var e = $(filterable);

            if (e.text().toLowerCase().indexOf(q) >= 0 && !showRow) {
                showRow = true;
            }
        });

        if (showRow)
            row.show();
        else
            row.hide();
    });
}z