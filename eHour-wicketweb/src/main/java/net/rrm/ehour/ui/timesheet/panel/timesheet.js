function initialize() {
    $(".foldLink").click(function() {
        foldProjectRow(this);
        return false;
    });

    initializeFilter();
}

function foldProjectRow(link) {
    var children = $(link).children();
    var isFolded = children.data('folded');

    var prj = $(link).closest('.customerRow');

    var acts = findActivitiesForProject(prj);

    for (var i = 0; i < acts.length; i++) {
        $(acts[i]).toggle();
    }

    children.data('folded', !isFolded);

    var icon = $(children[0])

    if (isFolded) {
        icon.removeClass('fa-angle-double-up');
        icon.addClass('fa-angle-double-down');

    } else {
        icon.removeClass('fa-angle-double-down');
        icon.addClass('fa-angle-double-up');
    }
}

function findActivitiesForProject(element) {
    var activities = [];

    $(element).nextAll('tr').each(function(i, row) {
        var r = $(row);
        if (r.attr('class').indexOf("projectRow") < 0) {
            return false;
        } else {
            activities.push(r);
        }
    });

    return activities;
}

function initializeFilter() {
    $("#filter").keyup(function () {
        filter()
    });

    filter();
}


function filter() {
    var q = $.trim($("#filter").val()).toLowerCase();

    $(".timesheetTable").find(".projectName").each(function (idx, element) {
        var e = $(element).closest('.projectRow')

        if ($(element).text().toLowerCase().indexOf(q) >= 0) {
            e.data("visible", true);
            e.show();
        } else {
            e.data("visible", false);
            e.hide();
        }
    });

    $(".timesheetTable").find(".customerRow").each(function (idx, element) {
        var isVisible = false;

        var acts = findActivitiesForProject(element);

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