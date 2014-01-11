function ListHighlight() {
    this.selectAndDeselectRest = function(toSelect) {
        $(".assignmentList").find(".rowSelector").each(function (idx, element) {
            var row = $(element);

            if (row.attr('id') == toSelect) {
                row.attr('class', 'selected rowSelector');
            } else {
                row.attr('class', 'row rowSelector');
            }
        })
    }

    this.select = function(toSelect) {
        $("#" + toSelect).attr('class', 'selected');
    }

    this.deselect = function(toDeselect) {
        $("#" + toDeselect).attr('class', 'row');
    }
}

var listHighlight = new ListHighlight();
