function EntrySelector(baseId, filterInputSelector, targetListSelector) {

    var filterInput = filterInputSelector;
    var targetList = targetListSelector;
    var base = $(baseId);


    var widths;

    this.initFilter = function () {
        var filter = input();

        $(filter).unbind('keyup');

        filter.keyup(function () {
            filterList();
        });

        filter.keyup();

        widths = fetchInitialWidth();
        this.refresh();
    };

    function input() {
        return base.find(filterInput);
    }

    function target() {
        return base.find(targetList);
    }

    function filterList() {
        var q = $.trim(input().val()).toLowerCase();

        target().find(".filterRow").each(function (idx, element) {
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
        var widths = [];

        target().find('tr:first th').each(function (idx) {
            widths[idx] = $(this).width();
        });

        return widths;
    }

    this.refresh = function() {
        staticTableWidth();
        filterList();
    };

    function staticTableWidth() {
        target().find('tr:first th').each(function (idx) {
            $(this).css({'width': widths[idx] + "px"});
        });
    }

    this.initFilter()
}
