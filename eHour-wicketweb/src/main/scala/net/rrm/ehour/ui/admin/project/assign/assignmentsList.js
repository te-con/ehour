function AssignmentFilter() {

    this.initAssignmentFilter = function () {
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
    }

    this.highlight = function(toHighlight) {
        $("#assignmentList").find(".row").each(function (idx, element) {
            var row = $(element);

            if (row.attr('id') == toHighlight) {
                row.attr('class', 'row selected');
            } else {
                row.attr('class', 'row');
            }
        })
    }
}

var assignmentFilter = new AssignmentFilter();
