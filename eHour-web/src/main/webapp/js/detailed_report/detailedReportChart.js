function DetailedReportChart(cacheKey, id) {
    Highcharts.dateFormats = {
        D: function (timestamp) {
            var date = new Date(timestamp);
            var month = date.getMonth() + 1;

            return date.getDay() + "/" + month + "/" + date.getFullYear();
        },
        W: function (timestamp) {
            var date = new Date(timestamp),
                day = date.getUTCDay() == 0 ? 7 : date.getUTCDay(),
                dayNumber;
            date.setDate(date.getUTCDate() + 4 - day);
            dayNumber = Math.floor((date.getTime() - new Date(date.getUTCFullYear(), 0, 1, -6)) / 86400000);
            return 1 + Math.floor(dayNumber / 7);
        },
        M: function (timestamp) {
            var date = new Date(timestamp);
            var month = date.getMonth() + 1;

            return month;

        },
        Q: function (timestamp) {
            var date = new Date(timestamp);
            var month = date.getMonth() + 1;

            var quarter = Math.floor(month / 3) + (((month % 3) > 0) ? 1 : 0);

            return quarter;
        },
        Y: function (timestamp) {
            var date = new Date(timestamp);
            return date.getFullYear();
        }
    }

    var options = {
        chart: {
            defaultSeriesType: 'column',
            zoomType: 'x'
        },
        title: {
            text: 'Hours booked on customers per day'
        },
        plotOptions: {
            series: {
                shadow: false
            },
            column: {
                stacking: 'normal'
            }
        },
        xAxis: [
            {
                type: 'datetime',
                maxZoom: 3,
                labels: {
                    format: '{value: Week %Q}'
                }
            }
        ],
        yAxis: {
            title: {
                text: 'Hours'
            }
        },
        credits: {
            enabled: false
        },

        tooltip: {
            formatter: function () {
                return new Date(this.x).toLocaleDateString() + '<br />' + this.series.name + ': ' + this.y.toLocaleString() + ' hours'
            }
        },
        series: [
            {}
        ]
    };

    function updateChart(options, operation) {
        $.getJSON('/eh/rest/report/detailed/' + operation + '/' + cacheKey, function (data) {
            options.plotOptions.series.pointStart = data.pointStart;
            options.plotOptions.series.pointInterval = data.pointInterval;
            options.title.text = data.title;
            options.yAxis.title.text = data.yAxis;
            options.series = data.series;
            options.xAxis[0].labels.format = '{value: ' +  data.xAxisFormat + '}';

            $('#' + id).highcharts(options);

            if (data.hasReportRole)
                $('#buttons').show();
        });
    }

    this.update = function(updatedCacheKey) {
        cacheKey = updatedCacheKey;
        updateChart(options, 'hour');
    };


    this.init = function () {
        console.log('Rendering ' + cacheKey + ' to ' + id);

        jQuery.fn.extend({
            disable: function(state) {
                return this.each(function() {
                    this.disabled = state;
                });
            }
        });

        $('#turnover').click(function() {
            updateChart(options, 'turnover');
            $('#turnover').disable(true);
            $('#time').disable(false);
        });


        var time = $('#time');

        time.click(function() {
            updateChart(options, 'hour');
            $('#turnover').disable(false);
            time.disable(true);
        });

        $('#buttons').buttonset();

        updateChart(options, 'hour');
    }
}


