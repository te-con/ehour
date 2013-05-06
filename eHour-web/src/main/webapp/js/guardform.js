function guardForm() {
    $("form :input").change(function () {
        $(this).closest('form').data('changed', true);
    });
}