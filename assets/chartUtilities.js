//Draw AmLineChart
function drawAmLineChart(data, holderName, legendPosition, scrollbar, imagePath) {
    //Default params 
    legendPosition = legendPosition || "left";
    scrollbar = scrollbar || false;
    //Parse Data 
    var chartData = data.Values;
    var legend = data.Legend;
    var colors = data.Colors;
    var icons = data.Icons;
    var units = data.Units;
    var title = data.Title;
    // SERIAL CHART
    chart = new AmCharts.AmSerialChart();
    chart.pathToImages = "../Content/Images/";
    chart.dataProvider = chartData;
    chart.categoryField = "V0";
    chart.balloon.bulletSize = 2;


    // AXES
    // Category ==> Date
    var categoryAxis = chart.categoryAxis;
    categoryAxis.title = title;
    categoryAxis.titlePosition = "top";
    categoryAxis.titleColor = "#9ABF18";

    categoryAxis.gridAlpha = 0.05;
    categoryAxis.axisColor = "#9ABF18";
    categoryAxis.color = "#9ABF18";
    categoryAxis.startOnAxis = true;
    categoryAxis.labelRotation = 45; // this line makes category values to be rotated

    // Value ==> Statistics values
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.gridAlpha = 0.05;
    valueAxis.axisColor = "#9ABF18";
    valueAxis.color = "#9ABF18";
    //valueAxis.title = "Traffic incidents";
    chart.addValueAxis(valueAxis);

    // GRAPHS
    for (var i = 1; i < Object.keys(legend).length; i++) {
        key = "V".concat(i);
        var graph = new AmCharts.AmGraph();
        graph.type = "line"; // it's simple line graph 
        graph.title = legend[key] + " (" + units[key] + ")";
        graph.valueField = key;
        graph.lineAlpha = 1;
        graph.lineColor = colors[key];
        graph.lineThickness = 2;
        graph.fillAlphas = 0.5; // setting fillAlphas to > 0 value makes it area graph
        // graph.balloonText = "<img src='../Content/Images/SensorProperties/ColoredProperties/" + icons[key] + ".png ' style='vertical-align:bottom; margin-right: 10px;'><span style='font-size:14px; color:#000000;'><b>[[value]]</b></span>";
        graph.balloonText = "<img src='" + imagePath + icons[key] + ".png ' style='vertical-align:bottom; margin-right: 10px;'><span style='font-size:14px; color:#000000;'><b>[[value]]</b></span>";
        // style bullets
        graph.bullet = "round";
        graph.bulletColor = "#FFFFFF";
        graph.bulletBorderColor = colors[key];
        graph.bulletBorderAlpha = 1;
        graph.bulletBorderThickness = 2;
        graph.bulletSize = 7;
        chart.addGraph(graph);
    }

    // LEGEND
    var amlegend = new AmCharts.AmLegend();
    amlegend.position = legendPosition;
    // amlegend.valueText = "[[value]]";
    //legend.valueWidth = 100;
    //amlegend.valueAlign = "left";   
    //legend.equalWidths = false;
    // amlegend.periodValueText = "total: [[value.sum]]"; // this is displayed when mouse is not over the chart.                
    chart.addLegend(amlegend);

    // CURSOR
    var chartCursor = new AmCharts.ChartCursor();
    //chartCursor.cursorAlpha = 0;
    chartCursor.cursorPosition = "mouse";
    chartCursor.cursorColor = "#85DF59";
    chart.addChartCursor(chartCursor);

    // SCROLLBAR
    if (scrollbar) {
        var chartScrollbar = new AmCharts.ChartScrollbar();
        chartScrollbar.graph = graph;
        //chartScrollbar.scrollbarHeight = 20;
        chartScrollbar.color = "#9ABF18";
        chartScrollbar.autoGridCount = true;
        chart.addChartScrollbar(chartScrollbar);
    }

    // WRITE
    chart.write(holderName);
    $('.loading-Chart').hide();
}

//Draw AmBarChart
function drawAmBarChart(data, holderName, legendPosition) {
    //Default params 
    legendPosition = legendPosition || "left";

    //Parse Data 
    var chartData = data.Values;
    var legend = data.Legend;
    var colors = data.Colors;
    var icons = data.Icons;
    var title = data.Title; 
    // SERIAL CHART
    chart = new AmCharts.AmSerialChart();
   
    chart.dataProvider = chartData;
    chart.categoryField = "V0";
    chart.startDuration = 1;
    chart.plotAreaBorderColor = "#DADADA";
    chart.plotAreaBorderAlpha = 1;
    // this single line makes the chart a bar chart          
    //chart.rotate = true;
    chart.angle = 30;
    chart.depth3D = 40;

    // AXES
    // Category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.title = title;
    categoryAxis.titlePosition = "top";
    categoryAxis.titleColor = "#9ABF18";
    //categoryAxis.gridPosition = "start";
    categoryAxis.gridCount = 1;
    categoryAxis.gridAlpha = 0.1;
    //categoryAxis.axisAlpha = 0;
    categoryAxis.startOnAxis = true;
    categoryAxis.axisColor = "#9ABF18";
    categoryAxis.color = "#9ABF18";
    categoryAxis.labelRotation = 45; // this line makes category values to be rotated


    // Value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.stackType = "3d";
    valueAxis.axisAlpha = 0;
    valueAxis.gridAlpha = 0.1;
    //valueAxis.position = "top";
    valueAxis.axisColor = "#9ABF18";
    valueAxis.color = "#9ABF18";
    chart.addValueAxis(valueAxis);

    // GRAPHS
    for (var i = 1; i < Object.keys(legend).length; i++) {
        key = "V".concat(i);
        var graph = new AmCharts.AmGraph();
        graph.type = "column";
        graph.title = legend[key];
        graph.valueField = key;
        graph.lineAlpha = 1;
        graph.lineColor = colors[key];
        graph.lineThickness = 2;
        graph.fillAlphas = 0.5;
        // graph1.balloonText = "OnValues:[[value]]"; 
        graph.fillColors = colors[key];
        chart.addGraph(graph);

    }

    // LEGEND
    var amLegend = new AmCharts.AmLegend();
    amLegend.position = legendPosition;
    chart.addLegend(amLegend);

    // CURSOR
    var chartCursor = new AmCharts.ChartCursor();
    //chartCursor.cursorAlpha = 0;
    chartCursor.cursorPosition = "mouse";
    chartCursor.cursorColor = "#85DF59";
    chart.addChartCursor(chartCursor);

    // WRITE
    chart.write(holderName);
    $('.loading-Chart').hide();
}

function drawPieChart(data, holderName, legendDiv) {

    AmCharts.makeChart(holderName, {
        "type": "pie",
        "theme": "none",
        "dataProvider": data,
        "titleField": "Entitled",
        "valueField": "Energy",
        "labelRadius": 5,
        "fontFamily": "hero",
        "colors": ['#f9d904', '#24acbb', '#74e90c', '#ff8b02', '#0be883'],
        "radius": "42%",
        "innerRadius": "60%",
        "depth3D": 5,       
        "labelText": "[[Entitled]]",
        "labelsEnabled":false,
        "legend": {
            //"divId": legendDiv,
            "position": "right",
            //"marginLeft":-20,

        },
        "exportConfig": {
            "menuItems": [{
                "icon": '/lib/3/images/export.png',
                "format": 'png'
            }]
        }
    });
}

function switchChart(chartId) {
    //chartId= 0 ==> lineChart
    //chartId= 1 ==> BarreChart
    $('.loading-Chart').show();
    if (chartId == 0) {
        $("#selectZawve").show();
        $("#selectZawveBarre").hide();
        $("#lineChart").css("background-color", "#60C29F");
        $("#barreChart").css("background-color", "#4E4D4D");
        ShowStatistical(-1);
    }
    else {
        $("#selectZawve").hide();
        $("#selectZawveBarre").show();
        $("#lineChart").css("background-color", "#4E4D4D");
        $("#barreChart").css("background-color", "#60C29F");
        getCommandValues();
    }
}

function getTitle(selectedPeriod) {
    var currentLanguageName = '@System.Threading.Thread.CurrentThread.CurrentCulture.TwoLetterISOLanguageName';
    var currentTime = new Date();
    var month = currentTime.getMonth() + 1;
    year = currentTime.getFullYear();
    var day = currentTime.getDay() + 1;
    months = new Array();
    months = new Array('@Labels.Lbl_January', '@Labels.Lbl_February', '@Labels.Lbl_March', '@Labels.Lbl_April', '@Labels.Lbl_May', '@Labels.Lbl_June', '@Labels.Lbl_July', '@Labels.Lbl_August', '@Labels.Lbl_September', '@Labels.Lbl_October', '@Labels.Lbl_November', '@Labels.Lbl_December');
    var periodStat;
    var selectedEquipmentName;
    if (selectedPeriod == "month") {
        periodStat = getFormattedText('@Labels.Lbl_TrendPerMonth', currentLanguageName) + " " + getFormattedText(months[month - 1], currentLanguageName);
    }
    else if (selectedPeriod == "year") {
        periodStat = getFormattedText('@Labels.Lbl_TrendPerYear', currentLanguageName) + " " + year;
    }
    else if (selectedPeriod == "week") {
        periodStat = getFormattedText('@Labels.Lbl_TrendPerWeek', currentLanguageName) + " ";
    }
    else if (selectedPeriod == "hour") {
        periodStat = getFormattedText('@Labels.Lbl_TrendPerHour', currentLanguageName) + " ";
    }
    else {
        periodStat = getFormattedText('@Labels.Lbl_TrendPerDay', currentLanguageName) + " ";
    }
    var title = '@Labels.Lbl_TitleCharte' + " " + periodStat;
    return title;
}
