jQuery.fn.filterOn = function (filterInput) {
    return this.each(function () {
        var select = this;
        var options = [];
        $(select).find('option').each(function () {
            options.push({value: $(this).val(), text: $(this).text()});
        });
        $(select).data('ops', options);

        $(filterInput).unbind('keyup');

        $(filterInput).keyup(function () {
            var options = $(select).empty().data('ops');
            var filter = $(this).val().toLowerCase();

            $.each(options, function (i) {
                var option = options[i];

                var s = option.text.toLowerCase();
                if (s.indexOf(filter) >= 0) {
                    $(select).append(
                        $('<option>').text(option.text).val(option.value)
                    );
                }
            });
        });

        $(filterInput).keyup();
    });
};

function toggleFilter(id) {
    $(id).toggle('fast');
    return false;
}

function initFilter(selectId, filterId) {
    $(selectId).filterOn(filterId);
}
