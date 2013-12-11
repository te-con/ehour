var WARNING_MSG = "You entered hours but you have not stored them yet. Please click 'store' before proceeding, otherwise your data may be lost.";

function guardForm() {
    $('#timesheetForm :input').change(function () {
        $('#timesheetForm').data('changed', true);
    });

    $(window).unbind("beforeunload");

    $(window).bind('beforeunload', function () {
        if ($('#timesheetForm').data('changed') === true) {
            return WARNING_MSG;
        }
    });

    var submitted = function () {
        $('#timesheetForm').data('changed', false);
        $(window).unbind("beforeunload");
    };

    $("#submit").click(submitted);
    $("#submitButtonTop").click(submitted);
}

function ajaxGuard() {
    if ($('#timesheetForm').data('changed') === true) {

        return confirm(WARNING_MSG);
    } else {
        return true;
    }
}


$(document).ready(function () {
    guardForm();
});