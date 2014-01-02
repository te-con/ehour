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
            var selected = $(select).val();
            var options = $(select).empty().data('ops');
            var filter = $(this).val().toLowerCase();

            $.each(options, function (i) {
                var option = options[i];

                var s = option.text.toLowerCase();
                if (s.indexOf(filter) >= 0) {
                    var o = $('<option>').text(option.text).val(option.value);

                    if ($.inArray(option.value, selected) > -1) {
                        o.prop('selected', true);
                    }

                    $(select).append(o);
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
