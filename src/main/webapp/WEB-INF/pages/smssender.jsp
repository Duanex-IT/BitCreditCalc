<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <meta http-equiv="Content-Encoding" charset="UTF-8" />
    <title><spring:message code="page.sms.title"/></title>
    <link href="static/css/smoothness/jquery-ui-1.10.3.custom.css" rel="stylesheet">
    <script src="static/js/libs/jquery-1.10.2.min.js"></script>
    <script src="static/js/libs/jquery-ui-1.10.3.custom.js"></script>
    <script src="static/js/libs/json2.js"></script>

    <script>
        function recalcSymbolsCount() {
            document.getElementById("symbolsCount").innerHTML = document.getElementById("smsText").value.length;
        };

        $(function() {
            $( "#sendSms" ).button().click(
                function( event ) {
                    var phoneNum = $.trim(document.getElementById("phoneNumber").value);
                    var smsLength = document.getElementById("smsText").value.length;
                    if (phoneNum.length != 9 || !$.isNumeric(phoneNum)) {
                        alert("Неверно введен номер телефона: +380"+phoneNum);
                    } else if (smsLength > 127) {
                        alert("Максимальная длина смс = 127символов. Текущая длина: "+smsLength+". Сократите текст смс.");
                    } else {
                        var sendInfo = {
                            "phone": "380"+phoneNum,
                            "smsText": document.getElementById("smsText").value
                        };

                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            contentType: "application/json; charset=utf-8",
                            traditional: true,
                            url: "${smsUrl}",
                            data: JSON.stringify(sendInfo),
                            success: function(pageResponse){
                                if (pageResponse.errorMessage == null) {
                                    alert("Сообщение успешно поставлено в очередь на отправку.");
                                } else {
                                    alert("Ошибка: "+pageResponse.errorMessage);
                                }
                            },
                            error: function (xhr, ajaxOptions, thrownError) {
                                alert(xhr.status+": "+thrownError);
                            }
                        });
                    }
                });
        });

    </script>
</head>
<body>
Текст SMS:<br/>
<textarea id="smsText" cols=40 rows=6 onchange="recalcSymbolsCount()">Підключіться до Грошей.Будьте на Зв`язку. bitcredit.com.ua.0800508004</textarea><br/>
Введено символов: <label id="symbolsCount">0</label><br/>

Номер: +380<input id="phoneNumber" type="text" size="9">
<input id="sendSms" type="button" value="Отправить">

<script language="JavaScript">
    recalcSymbolsCount();
</script>
</body>
</html>