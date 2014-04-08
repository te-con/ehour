function DetailedReportChart(cacheKey, id) {
    this.init = function () {
        console.log("Rendering " + cacheKey + " to " + id);

        var options = {
            chart: {
                type: 'bar'
            },
            title: {
                text: 'Fruit Consumption'
            },
            xAxis: {
                categories: ['Apples', 'Bananas', 'Oranges']
            },
            yAxis: {
                title: {
                    text: 'Fruit eaten'
                }
            },
            series: [
                {},
                {}
            ]
        };

        $.getJSON('/eh/rest/report/detailed/hour/' + cacheKey, function (data) {
            options.series[0].data = data;
            $('#' + id).highcharts(options);
        });
    }
}
