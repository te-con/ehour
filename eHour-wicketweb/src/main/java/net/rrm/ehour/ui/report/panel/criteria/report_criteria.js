jQuery.fn.filterOn = function (filterId) {
    return this.each(function () {
        var select = this;
        var options = [];
        $(select).find('option').each(function () {
            options.push({value: $(this).val(), text: $(this).text()});
        });
        $(select).data('ops', options);

        $(filterId).unbind('keyup');

        $(filterId).keyup(function () {
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

        $(filterId).keyup();
    });
};

function toggleFilter(id) {
    $(id).slideToggle('fast');
    return false;
}

function initFilter(selectId, filterId) {
    $(selectId).filterOn(filterId);
}

function clearFilter(filterId) {
    $(filterId).val('');
    $(filterId).keyup();
}
