function DetailedReportChart(cacheKey, id) {
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
                stacking: "normal",
                pointWidth: 10
            }
        },
        xAxis: [
            {
                type: "datetime",
                maxZoom: 3
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

            $('#' + id).highcharts(options);

            if (data.hasReportRole)
                $('#buttons').show();
        });
    }

    this.update = function() {
        updateChart(options, 'hour');
    };


    this.init = function () {
        console.log("Rendering " + cacheKey + " to " + id);

        jQuery.fn.extend({
            disable: function(state) {
                return this.each(function() {
                    this.disabled = state;
                });
            }
        });

        $("#turnover").click(function() {
            updateChart(options, 'turnover');
            $("#turnover").disable(true);
            $("#time").disable(false);
        });


        var time = $("#time");

        time.click(function() {
            updateChart(options, 'hour');
            $("#turnover").disable(false);
            time.disable(true);
        });

        $("#buttons").buttonset();

        updateChart(options, 'hour');
    }
}


