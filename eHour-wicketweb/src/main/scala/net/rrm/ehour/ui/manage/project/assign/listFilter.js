function ListFilter(filterInputSelector, targetListSelector) {

    var filterInput = filterInputSelector;
    var targetList = targetListSelector;

    this.initFilter = function () {
        var filter = $(filterInput);

        $(filter).unbind('keyup');

        filter.keyup(function () {
            filterList();
        });

        filter.keyup();
    }

    function filterList() {
        var q = $.trim($(filterInput).val()).toLowerCase();

        $(targetList).find(".row").each(function (idx, element) {
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

    this.initFilter()
}


