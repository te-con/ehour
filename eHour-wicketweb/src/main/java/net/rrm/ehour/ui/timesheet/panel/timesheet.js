function Timesheet() {
    var hideZero = false;


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
                hideZero = true;
            } else {
                hideZero = false;
            }

            filter();
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


    function filter() {
        var q = $.trim($("#filter").val()).toLowerCase();
        if (!q) {
            q = "";
        }
        var r = new RegExp(q.replace(/\*/g, '.*').replace(/ /g, '.*'));

        var timesheetTable = $(".timesheetTable");

        timesheetTable.find(".customerRow").each(function (idx, element) {
            var customerRow = $(element);
            var customerName = $(customerRow.find(".customerNameFilter")).text();
            var customerHit = r.exec(customerName.toLowerCase());

            var projects = $(findProjects(customerRow));

            if (customerHit) {
                customerRow.show();

                projects.each(function (id, e) {
                    var projectRow = $(e);

                    if (hideZero && isProjectRowWithZeroHours(projectRow)) {
                        projectRow.hide();
                        projectRow.data("visible", false);
                    } else {
                        projectRow.show();
                        projectRow.data("visible", true);
                    }
                });
            } else {
                projects.each(function (id, e) {
                    var projectRow = $(e);

                    if (hideZero && isProjectRowWithZeroHours(projectRow)) {
                        projectRow.hide();
                        projectRow.data("visible", false);
                    } else {
                        var name = $(projectRow.find(".projectNameFilter")).text();
                        var nameHit = r.exec(name.toLowerCase());

                        var code = $(projectRow.find(".projectCodeFilter")).text();
                        var codeHit = r.exec(code.toLowerCase());

                        if (codeHit || nameHit) {
                            customerRow.show();
                            projectRow.show();
                            projectRow.data("visible", true);
                        } else {
                            projectRow.hide();
                            projectRow.data("visible", false);
                        }
                    }
                });
            }
        });

        hideCustomersWithoutProjects();
    }

    function isProjectRowWithZeroHours(projectRow) {
        var item = projectRow.find(".total");

        var val = item.html();
        var sanitized = val.replace(/\D/g, '');

        return parseInt(sanitized) === 0;
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
}
