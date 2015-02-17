function Timesheet() {
    this.init = function () {
        $(".foldLink").click(function () {
            foldProjectRow(this);
            return false;
        });

        $("#filter").keyup(function () {
            filter()
        });

        filter();

        $("#toggleOptions").click(function () {
            $("#timesheetOptions").slideToggle('fast');
        });

        var checkbox = $("#hideCheckbox");
        checkbox.click(function () {
            if ($(this).is(':checked')) {
                $(".projectRow > .total").each(function () {
                        var item = $(this);
                        var val = item.html();
                        var sanitized = val.replace(/\D/g, '');

                        if (parseInt(sanitized) === 0) {
                            var e = $(item.closest('.projectRow'));
                            e.hide();
                            e.data("visible", false);
                        }
                    }
                );
            } else {
                $(".projectRow > .total").each(function () {
                    var item = $(this);
                    var e = $(item.closest('.projectRow'));
                    e.show();
                    e.data("visible", true);
                });

                filter();
            }

            hideCustomersWithoutProjects();
        });
    };

    function foldProjectRow(link) {
        var children = $(link).children();
        var isFolded = children.data('folded');

        var customerRow = $(link).closest('.customerRow');

        var projects = findProjects(customerRow);

        for (var i = 0; i < projects.length; i++) {
            $(projects[i]).slideToggle('fast');
        }

        children.data('folded', !isFolded);

        var icon = $(children[0]);

        if (isFolded) {
            icon.removeClass('fa-angle-double-up');
            icon.addClass('fa-angle-double-down');
        } else {
            icon.removeClass('fa-angle-double-down');
            icon.addClass('fa-angle-double-up');
        }
    }


    function findProjects(element) {
        var projects = [];

        $(element).nextAll('tr').each(function (i, row) {
            var r = $(row);
            if (r.attr('class').indexOf("projectRow") < 0) {
                return false;
            } else {
                projects.push(r);
            }
        });

        return projects;
    }

    function filter() {
        var q = $.trim($("#filter").val()).toLowerCase();
        var r = new RegExp(q.replace(/\*/g, '.*').replace(/ /g, '.*'));
        var timesheetTable = $(".timesheetTable");

        timesheetTable.find(".projectName").each(function (idx, element) {
            var e = $(element).closest('.projectRow');

            var xs = r.exec($(element).text().toLowerCase());

            if (xs != null) {
                e.data("visible", true);
                e.show();
            } else {
                e.data("visible", false);
                e.hide();
            }
        });

        hideCustomersWithoutProjects();
    }

    function hideCustomersWithoutProjects() {
        var timesheetTable = $(".timesheetTable");

        timesheetTable.find(".customerRow").each(function (idx, element) {
            var isVisible = false;

            var acts = findProjects(element);

            for (var i = 0; i < acts.length; i++) {
                isVisible |= $(acts[i]).data("visible");
            }

            if (isVisible) {
                $(element).show();
            } else {
                $(element).hide();
            }
        });

    }
}
