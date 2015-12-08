function DetailedReportChart(cacheKey, id) {
    Date.prototype.getMonthName = function() {
        var monthNames = [ "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December" ];
        return monthNames[this.getMonth()];
    };

    Date.prototype.getWeekNumber = function(){
        var d = new Date(+this);
        d.setHours(0,0,0);
        d.setDate(d.getDate()+4-(d.getDay()||7));
        return Math.ceil((((d-new Date(d.getFullYear(),0,1))/8.64e7)+1)/7);
    };

    Highcharts.dateFormats = {
        D: function (timestamp) {
            var date = new Date(timestamp);
            var month = date.getMonth() + 1;

            return date.getDay() + "/" + month + "/" + date.getFullYear();
        },
        W: function (timestamp) {
            var date = new Date(timestamp);
            return "Week " + date.getWeekNumber() + "<br>" + date.getFullYear();
        },
        M: function (timestamp) {
            var date = new Date(timestamp);
            return date.getMonthName() + " " + date.getFullYear();

        },
        Q: function (timestamp) {
            var date = new Date(timestamp);
            var month = date.getMonth() + 1;

            var quarter = "Q" + Math.floor(month / 3) + (((month % 3) > 0) ? 1 : 0);

            return quarter;
        },
        Y: function (timestamp) {
            var date = new Date(timestamp);
            return date.getFullYear();
        }
    };

    var options = {
        chart: {
            defaultSeriesType: 'column',
            zoomType: 'x'
        },
        title: {
            text: 'Hours booked on clients per day'
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
            var dateParts = data.pointStart.split('-');

            var startYear = parseInt(dateParts[0]);
            var startMonth = parseInt(dateParts[1] - 1);

            var pointStart = Date.UTC(startYear, startMonth, parseInt(dateParts[2]));

            switch (data.type) {
                case 'D':
                    options.plotOptions.series.pointInterval = 24 * 3600 * 1000;
                    options.plotOptions.series.pointStart = pointStart;
                    options.xAxis[0].labels.format = '{value: %D}';
                    break;
                case 'W':
                    options.plotOptions.series.pointInterval = 7 * 24 * 3600 * 1000;
                    options.plotOptions.series.pointStart = pointStart;
                    options.xAxis[0].labels.format = '{value: %W}';

                    break;
                case 'M':
                    for (var s = 0; s < data.series.length; s++) {
                        for (var i = 0; i < data.series[s].data.length; i++) {
                            data.series[s].data[i] = [Date.UTC(startYear, startMonth + i, 1), data.series[s].data[i]];
                        }
                    }

                    options.plotOptions.series.pointInterval = undefined;
                    options.xAxis[0].labels.format = '{value: %M}';

                    break;
                case 'Q':
                    for (var s = 0; s < data.series.length; s++) {
                        for (var i = 0; i < data.series[s].data.length; i++) {
                            data.series[s].data[i] = [Date.UTC(startYear, startMonth + (i * 3), 1), data.series[s].data[i]];
                        }
                    }

                    options.plotOptions.series.pointInterval = undefined;
                    options.xAxis[0].labels.format = '{value: %Q}';

                    break;
                case 'Y':
                    for (var s = 0; s < data.series.length; s++) {
                        for (var i = 0; i < data.series[s].data.length; i++) {
                            data.series[s].data[i] = [Date.UTC(startYear + i, 0, 1), data.series[s].data[i]];
                        }
                    }

                    options.plotOptions.series.pointInterval = undefined;
                    options.xAxis[0].labels.format = '{value: %Y}';

                    break;
            }

            options.title.text = data.title;
            options.yAxis.title.text = data.yAxis;
            options.series = data.series;

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