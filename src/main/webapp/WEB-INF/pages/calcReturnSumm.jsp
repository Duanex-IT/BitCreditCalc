<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <title><spring:message code="calcreturnsumm.title"/></title>
    <meta http-equiv="Content-Encoding" charset="UTF-8" />

    <link href="static/css/smoothness/jquery-ui-1.10.3.custom.css" rel="stylesheet">
    <link href="static/css/calc.css" rel="stylesheet">
    <script src="static/js/libs/jquery-1.9.1.js"></script>
    <script src="static/js/libs/jquery-ui-1.10.3.custom.js"></script>

    <script>
        var undoValue = "";

        function c(val) {
            undoValue = document.getElementById("d").value;
            document.getElementById("d").value=val;
        }
        function v(val) {
            undoValue = document.getElementById("d").value;
            document.getElementById("d").value+=val;
        }
        function bs() {
            undoValue = document.getElementById("d").value;
            document.getElementById("d").value = document.getElementById("d").value.substr(0, document.getElementById("d").value.length-1);
        }
        function undo() {
            document.getElementById("d").value = undoValue;
        }
        function e() {
            try {
                undoValue = document.getElementById("d").value;
                var expr = document.getElementById("d").value.replace('**','*').replace('++','+').replace('--','-').replace('//','/');
                c(eval(expr).toFixed(2));
            } catch(e) {
                c('Error');
            }
        }

        $(function() {
            var LIMIT_Max = ${credit_limit};
            var one_day=1000*60*60*24;

            $( "#percentRadio" ).buttonset();

            $( "#creditSummSpinner" ).spinner({
                min: 0,
                max: LIMIT_Max,
                stop: function(event, ui) {
                    $('#creditSummSlider').slider('value', $(this).spinner('value'));
                }
            });

            $.datepicker.regional['ru'] = {
                closeText: 'Закрыть',
                prevText: '&#x3c;Пред',
                nextText: 'След&#x3e;',
                currentText: 'Сегодня',
                monthNames: ['Январь','Февраль','Март','Апрель','Май','Июнь',
                    'Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
                monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн',
                    'Июл','Авг','Сен','Окт','Ноя','Дек'],
                dayNames: ['воскресенье','понедельник','вторник','среда','четверг','пятница','суббота'],
                dayNamesShort: ['вск','пнд','втр','срд','чтв','птн','сбт'],
                dayNamesMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
                weekHeader: 'Не',
                dateFormat: 'dd.mm.yy',
                firstDay: 1,
                isRTL: false,
                showMonthAfterYear: false,
                yearSuffix: ''};

            $( "#getDatepicker" ).datepicker({
                "dateFormat": "dd/mm/yy",
                showOtherMonths: true,
                selectOtherMonths: true,
                numberOfMonths: 3,
                onSelect: function(dateText, inst) {
                    $( "#daysCountSpinner" ).spinner("value", dateDiff());
                }
            });
            $( "#getDatepicker" ).datepicker("setDate", new Date());
            $( "#returnDatepicker" ).datepicker({
                "dateFormat": "dd/mm/yy",
                showOtherMonths: true,
                selectOtherMonths: true,
                numberOfMonths: 3,
                onSelect: function(dateText, inst) {
                    $( "#daysCountSpinner" ).spinner("value", dateDiff());
                }
            });
            $( "#returnDatepicker" ).datepicker("setDate", new Date());

            $.datepicker.setDefaults($.datepicker.regional['ru']);

            $( "#creditSummSlider" ).slider({
                max: LIMIT_Max,
                step: 1,
                slide: function( event, ui ) {
                    $( "#creditSummSpinner" ).spinner("value", ui.value);
                },
                change: function( event, ui ) {
                    $( "#creditSummSpinner" ).spinner("value", ui.value);
                }
            });

            var creditSummAgr=0, returnSummAgr=0, returnPercentAgr=0, returnWithComissionAgr=0;

            $( "#addResult" ).button().click(
                function( event ) {
                    var choosenPercent = parseFloat($("input[name=radio]:checked").val());
                    var creditSumm = parseInt($( "#creditSummSlider" ).slider( "value"));

                    creditSummAgr+=$( "#creditSummSlider" ).slider( "value");
                    returnSummAgr+=(dateDiff()*creditSumm*choosenPercent) + creditSumm;
                    returnPercentAgr+=dateDiff()*creditSumm*choosenPercent;
                    returnWithComissionAgr+=((dateDiff()*creditSumm*choosenPercent) + creditSumm)*1.0399;

                    var table=document.getElementById("resultTable");
                    var row=table.insertRow(-1);
                    var cell0=row.insertCell(0);
                    var cell1=row.insertCell(1);
                    var cell2=row.insertCell(2);
                    var cell3=row.insertCell(3);
                    var cell4=row.insertCell(4);
                    var cell5=row.insertCell(5);
                    var cell6=row.insertCell(6);
                    cell0.innerHTML=$( "#creditSummSlider" ).slider( "value");
                    cell1.innerHTML=((dateDiff()*creditSumm*choosenPercent) + creditSumm).toFixed(2);
                    cell2.innerHTML=(dateDiff()*creditSumm*choosenPercent).toFixed(2);
                    cell3.innerHTML=(((dateDiff()*creditSumm*choosenPercent) + creditSumm)*1.0399).toFixed(2);
                    cell4.innerHTML=$.datepicker.formatDate("dd/mm/yy", $("#getDatepicker").datepicker("getDate"));
                    cell5.innerHTML=$.datepicker.formatDate("dd/mm/yy", $("#returnDatepicker").datepicker("getDate"));
                    cell6.innerHTML=dateDiff();

                    var tableAgr=document.getElementById("resultAgregateTable");
                    if (tableAgr.rows.length>1) {
                        tableAgr.deleteRow(1);
                    }
                    var rowAgr=tableAgr.insertRow(1);
                    var cell0Agr=rowAgr.insertCell(0);
                    var cell1Agr=rowAgr.insertCell(1);
                    var cell2Agr=rowAgr.insertCell(2);
                    var cell3Agr=rowAgr.insertCell(3);
                    cell0Agr.innerHTML=creditSummAgr;
                    cell1Agr.innerHTML=(returnSummAgr).toFixed(2);
                    cell2Agr.innerHTML=(returnPercentAgr).toFixed(2);
                    cell3Agr.innerHTML=(returnWithComissionAgr).toFixed(2);

                    /*
                                        LIMIT_Max -= creditSumm;
                                        $( "#creditSummSlider" ).slider("option", "max", LIMIT_Max);
                                        document.getElementById('creditLimitLabel').innerHTML = LIMIT_Max;
                                        if (parseInt($("#creditSummSpinner").spinner('value')) > LIMIT_Max) {
                                            $( "#creditSummSpinner" ).spinner("value", LIMIT_Max);
                                        }
                    */
                });

            $( "#clearResult" ).button().click(
                function( event ) {
                    window.location.reload();//todo clear all page data
                });

            $( "#daysCountSpinner" ).spinner({
                min: 1,
                max: 540,
                stop: function(event, ui) {
                    $( "#returnDatepicker" ).datepicker("setDate", $(this).spinner('value'));
                }
            });

            function dateDiff() {
                var diff = Math.round(($("#returnDatepicker").datepicker("getDate").getTime()-$("#getDatepicker").datepicker("getDate").getTime())/one_day);
                if (diff == 0) {
                    diff = 1;
                }
                return diff;
            }

        });
    </script>
</head>
<body>
    <form style="width: 1000px;">
        <div id="percentRadio">
            <input type="radio" id="percent33" name="radio" value="0.0033" checked="checked" /><label for="percent33"><spring:message code="calcreturnsumm.percent33.title"/></label>
            <input type="radio" id="percent29" name="radio" value="0.0029" /><label for="percent29"><spring:message code="calcreturnsumm.percent29.title"/></label>
            <input type="radio" id="percent25" name="radio" value="0.0025" /><label for="percent25"><spring:message code="calcreturnsumm.percent25.title"/></label>
        </div>

        <div id="resultSumm" style="border: 2px solid grey; margin: 10px 0px 5px 0px;">
            <table id="resultTable" border="1" ><tr>
                <th><spring:message code="calcreturnsumm.result.table.getsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnpercentsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnsumm.withcomission"/></th>
                <th><spring:message code="calcreturnsumm.result.table.getdate"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returndate"/></th>
                <th><spring:message code="calcreturnsumm.result.table.daysused"/></th>
            </tr></table>
            <spring:message code="calcreturnsumm.result.table.aggregate"/>
            <table id="resultAgregateTable" border="1" ><tr>
                <th><spring:message code="calcreturnsumm.result.table.getsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnpercentsumm"/></th>
                <th><spring:message code="calcreturnsumm.result.table.returnsumm.withcomission"/></th>
            </tr></table>
        </div>

        <div id="creditParams" style="margin: 10px 0px 10px 10px;">
            <spring:message code="calcreturnsumm.creditsumm"/> <input id="creditSummSpinner" value="0" size="4"/> <spring:message code="calcreturnsumm.from"/> <label id="creditLimitLabel">${credit_limit}</label> <spring:message code="unit.uah"/>
            <div id="creditSummSlider" style="margin: 5px auto;">
<%--
                <span style="position:absolute; left: 16.5%">|</span>
                <span style="position:absolute; left: 33.1%">|</span>
                <span style="position:absolute; left: 49.85%">|</span>
                <span style="position:absolute; left: 66.25%">|</span>
                <span style="position:absolute; left: 82.95%">|</span>
--%>
            </div>
<%--
            <div id="creditSummRuler" style="width: 1000px; margin: 0px 0px 20px 0px;">
                <span style="float: left">0</span>
                <span style="position:absolute; left: 9%">1000</span>
                <span style="position:absolute; left: 17.55%">2000</span>
                <span style="position:absolute; left: 26.24%">3000</span>
                <span style="position:absolute; left: 34.73%">4000</span>
                <span style="position:absolute; left: 43.3%">5000</span>
                <span style="float: right">6000</span>
            </div>
--%>
            <p>
                <spring:message code="calcreturnsumm.getdate"/> <input type="text" id="getDatepicker" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <spring:message code="calcreturnsumm.returndate"/> <input type="text" id="returnDatepicker" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <spring:message code="calcreturnsumm.dayscount"/> <input id="daysCountSpinner" value="1" size="4"/>
            </p>
        </div>

        <input type="button" id="addResult" value="<spring:message code="calcreturnsumm.addcalculation"/>">
        <input type="button" id="clearResult" value="<spring:message code="calcreturnsumm.cleanresult"/>">

        <div class="box">
            <div class="display"><input type="text" size="37" id="d"></div>
            <div class="keys">
                <p>
                    <input type="button" class="button gray" value="+3.99%" onclick='v("*1.0399")'>
                    <input type="button" class="button gray" value="undo" onclick='undo()'>
                    <input type="button" class="button gray" value="<-" onclick='bs()'>
                    <input type="button" class="button pink" value="/" onclick='v("/")'>
                </p>

                <p>
                    <input type="button" class="button black" value="7" onclick='v("7")'>
                    <input type="button" class="button black" value="8" onclick='v("8")'>
                    <input type="button" class="button black" value="9" onclick='v("9")'>
                    <input type="button" class="button pink" value="*" onclick='v("*")'>
                </p>

                <p>
                    <input type="button" class="button black" value="4" onclick='v("4")'>
                    <input type="button" class="button black" value="5" onclick='v("5")'>
                    <input type="button" class="button black" value="6" onclick='v("6")'>
                    <input type="button" class="button pink" value="--" onclick='v("-")'>
                </p>

                <p>
                    <input type="button" class="button black" value="1" onclick='v("1")'>
                    <input type="button" class="button black" value="2" onclick='v("2")'>
                    <input type="button" class="button black" value="3" onclick='v("3")'>
                    <input type="button" class="button pink" value="+" onclick='v("+")'>
                </p>

                <p>
                    <input type="button" class="button black" value="0" onclick='v("0")'>
                    <input type="button" class="button black" value="." onclick='v(".")'>
                    <input type="button" class="button black" value="C" onclick='c("")'>
                    <input type="button" class="button orange" value="=" onclick='e()'>
                </p>
            </div>
        </div>
    </form>
</body>
</html>