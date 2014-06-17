function EntrySelector(filterInputSelector, targetListSelector) {

    var filterInput = filterInputSelector;
    var targetList = targetListSelector;

    var widths;

    this.initFilter = function () {
        var filter = $(filterInput);

        $(filter).unbind('keyup');

        filter.keyup(function () {
            filterList();
        });

        filter.keyup();

        widths = fetchInitialWidth();
        this.refresh();
    }

    function filterList() {
        var q = $.trim($(filterInput).val()).toLowerCase();

        $(targetList).find(".filterRow").each(function (idx, element) {
            var row = $(element);
            var showRow = false;

            row.find("td").each(function (i, td) {
                var e = $(td);

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

    function fetchInitialWidth() {
        var widths = new Array();

        $(targetList, 'tr:first th').each(function (idx) {
            var width = $(this).width();
            widths[idx] = width;
        });

        return widths;
    }

    this.refresh = function() {
        staticTableWidth();
        filterList();
    };

    function staticTableWidth() {
        $(targetList, 'tr:first th').each(function (idx) {
            $(this).css({'width': widths[idx] + "px"});
        });
    }

    this.initFilter()
}
