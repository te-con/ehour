function ListHighlight() {
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

var listHighlight = new ListHighlight();
