function AffectedListHighlight() {
    this.selectAndDeselectRest = function(toSelect) {
        $(".affectedUsersList").find(".filterRow").each(function (idx, element) {
            var row = $(element);

            if (row.attr('id') == toSelect) {
                row.attr('class', 'selected filterRow');
            } else {
                row.attr('class', 'row filterRow');
            }
        })
    }
}

var affectedListHighlight = new AffectedListHighlight();
