function ListHighlight() {
    this.selectAndDeselectRest = function(toSelect) {
        $("#assignmentList").find(".row").each(function (idx, element) {
            var row = $(element);

            if (row.attr('id') == toSelect) {
                row.attr('class', 'row selected');
            } else {
                row.attr('class', 'row');
            }
        })
    }

    this.select = function(toSelect) {
        $("#" + toSelect).attr('class', 'row selected');
    }

    this.deselect = function(toDeselect) {
        $("#" + toDeselect).attr('class', 'row');
    }
}

var listHighlight = new ListHighlight();
