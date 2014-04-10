function DetailedReportChart(cacheKey, id) {
    this.init = function () {
        console.log("Rendering " + cacheKey + " to " + id);

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
//                    pointStart: Date.UTC(2013,9, 1),
//                    pointInterval: 86400000
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

        console.log(cacheKey);

        $.getJSON('/eh/rest/report/detailed/hour/' + cacheKey, function (data) {
            options.plotOptions.series.pointStart = data.pointStart;
            options.plotOptions.series.pointInterval = data.pointInterval;
            options.series = data.series;
            console.log(options.plotOptions.series.pointStart);
            $('#' + id).highcharts(options);
        });
    }
}


