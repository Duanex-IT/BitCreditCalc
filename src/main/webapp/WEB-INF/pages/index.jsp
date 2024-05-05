<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
    <meta http-equiv="Content-Encoding" charset="UTF-8" />
    <title><spring:message code="page.title"/></title>
    <link href="static/css/smoothness/jquery-ui-1.10.3.custom.css" rel="stylesheet">
    <script src="static/js/libs/jquery-1.9.1.js"></script>
    <script src="static/js/libs/jquery-ui-1.10.3.custom.js"></script>
    <script src="static/js/libs/json2.js"></script>
    <script src="static/js/creditcalc.js" charset="UTF-8"></script>

    <script>

        $(function() {

            // string constants
            MONTHLY_PERCENT = <spring:message code="monthly.percent"/>;

            var CurLockedSliderID = 1; // by default term is locked

            // limit
            var LIMIT_Max = ${credit_limit};
            var LIMIT_Min = <spring:message code="credit.LIMIT.Min"/>;
            var LIMIT_default = <spring:message code="credit.LIMIT.Default"/>;
            if (LIMIT_default>LIMIT_Max) {
                LIMIT_default = LIMIT_Max;
            }

            // term
            var Term_Default = <spring:message code="credit.Term.Default"/>;
            var Term_Min = <spring:message code="credit.Term.Min"/>;
            var Term_Max = ${credit_term};
            if (Term_Default>Term_Max) {
                Term_Default = Term_Max;
            }

            // monthly payment
            var MPay_Default = calcMonthPayment(LIMIT_default, Term_Default, MONTHLY_PERCENT);
            var MPay_Min = calcMonthPayment(LIMIT_Min, Term_Max, MONTHLY_PERCENT)
            var MPay_Max = calcMonthPayment(LIMIT_Max, Term_Min, MONTHLY_PERCENT);

            var sliderNamesArr = ["#slider_limit","#slider_term","#slider_payment"];

            function UpdateOrDoNoSlide (item, newValue){
                if ((item.slider("option", "min") <= newValue) && (newValue <= item.slider("option", "max")))  {
                    item.slider( "value",  newValue);
                    return true;
                }

                return false;
            }


            $( "#slider_limit" ).slider({		// ID = 0
                value: LIMIT_default,
                min: LIMIT_Min,
                max: LIMIT_Max,
                step: 1,
                slide: function( event, ui ) {
                    document.getElementById('input_limit_amount' ).value = ui.value;
                    slideLimitAction(ui.value)
                },
                change: function( event, ui ) {
                    document.getElementById('input_limit_amount' ).value = ui.value;
                    calcOverpayment();
                }
            });

            $( "#slider_term" ).slider({		// ID = 1
                value: Term_Default,
                min: Term_Min,
                max: Term_Max,
                step: 1,
                disabled: true,
                slide: function( event, ui ) {
                    $( "#slider_term_amount" ).text( ui.value + " <spring:message code="term.month"/>" );
                    if (CurLockedSliderID == 0)  // if currently locked Limit Scroll, change MPay
                        return UpdateOrDoNoSlide ($( "#slider_payment" ),  calcMonthPayment($("#slider_limit" ).slider( "value"), ui.value, MONTHLY_PERCENT));
                    else	// else - currently locked is MPay scroll, change Limit
                        return UpdateOrDoNoSlide ($( "#slider_limit" ),  calcLimit(ui.value, $("#slider_payment" ).slider( "value"), MONTHLY_PERCENT));
                },
                change: function( event, ui ) {
                    $( "#slider_term_amount" ).text( ui.value + " <spring:message code="term.month"/>" );
                    calcOverpayment();
                }
            });

            $( "#slider_payment" ).slider({		// ID = 2
                value: MPay_Default,
                min: MPay_Min,
                max: MPay_Max,
                step: 1,
                slide: function( event, ui ) {
                    document.getElementById('input_payment_amount' ).value = ui.value;
                    slidePaymentAction(ui.value)
                },
                change: function( event, ui ) {
                    document.getElementById('input_payment_amount' ).value = ui.value;
                    calcOverpayment();
                }
            });

            // Locker is changing... update styles
            $("p").click(function(event){
                if (event.target.type != 'text') {//I hate IE!!!
                    if (event.target.type != 'number') {
                        UpdateLocked ($(this).attr("id"));
                    }
                }
            });

            $("#btnSubmit").button().click(function(){/*send data to siebel*/
                siebelSender("${credit_orderId}", $('input[id=desired_limit]').val(),
                        $("#slider_limit" ).slider( "value"), "${applicationUrl}<spring:message code="url.senddata"/>");
            });

            function UpdateLocked (NewLockedID){
                CurLockedSliderID = NewLockedID;
                // toggle
                for (var i = 0; i < 3; i++) {
                    if (i!=CurLockedSliderID) { //unlocked
                        $("p[id="+i+"]").attr("class", "ui-state-default ui-corner-all ui-helper-clearfix");
                        $("p[id="+i+"] > span").attr("class","ui-icon ui-icon-transferthick-e-w");
                        $( sliderNamesArr[i] ).slider("enable");
                    } else { // disable selected CurLockedSliderID
                        $("p[id="+i+"]").attr("class","ui-state-highlight ui-corner-all ui-helper-clearfix");
                        $("p[id="+i+"] > span").attr("class","ui-icon ui-icon-pin-s");
                        $( sliderNamesArr[i] ).slider("disable");
                    }
                }
            }

            UpdateLocked (CurLockedSliderID);
            calcOverpayment();

            document.getElementById('input_limit_amount' ).value = $( "#slider_limit" ).slider( "value" );
            $( "#slider_term_amount" ).text( $( "#slider_term" ).slider( "value" ) + " <spring:message code="term.month"/>" );
            document.getElementById('input_payment_amount' ).value = $( "#slider_payment" ).slider( "value" );
            document.getElementById('input_payment_amount' ).min = MPay_Min;
            document.getElementById('input_payment_amount' ).max = MPay_Max;



            $("#input_limit_amount").change(function(){
                $('#slider_limit').slider('value', document.getElementById('input_limit_amount' ).value);
                slideLimitAction(document.getElementById('input_limit_amount' ).value);
            });
            $("#input_limit_amount").keypress(function(event){//duplicated for IE     //todo remove two keypress duplicates
                if(event.keyCode == 13){
                    $('#slider_limit').slider('value', document.getElementById('input_limit_amount' ).value);
                    slideLimitAction(document.getElementById('input_limit_amount' ).value);
                }
            });

            function slideLimitAction(uiValue) {
                if (CurLockedSliderID == 1)  // if currently locked Term Scroll, change MPay
                    return UpdateOrDoNoSlide ($( "#slider_payment" ), calcMonthPayment(uiValue, $("#slider_term" ).slider( "value"), MONTHLY_PERCENT));
                else	// else - currently locked is MPay scroll, change Term
                    return UpdateOrDoNoSlide ($( "#slider_term" ), calcTerm(uiValue, $("#slider_payment" ).slider( "value"), MONTHLY_PERCENT));
            }

            $("#input_payment_amount").change(function(){
                $('#slider_payment').slider('value', document.getElementById('input_payment_amount' ).value);
                slidePaymentAction(document.getElementById('input_payment_amount' ).value);
            });
            $("#input_payment_amount").keypress(function(event){//duplicated for IE
                if(event.keyCode == 13){
                    $('#slider_payment').slider('value', document.getElementById('input_payment_amount' ).value);
                    slidePaymentAction(document.getElementById('input_payment_amount' ).value);
                }
            });

            function slidePaymentAction(uiValue) {
                if (CurLockedSliderID == 0)  // if currently locked Limit Scroll, change Term
                    return UpdateOrDoNoSlide ($( "#slider_term" ),  calcTerm($("#slider_limit" ).slider( "value"), uiValue, MONTHLY_PERCENT));
                else	// else - currently locked is Term scroll, change Limit
                    return UpdateOrDoNoSlide ($( "#slider_limit" ),  calcLimit($("#slider_term" ).slider( "value"), uiValue, MONTHLY_PERCENT));
            }

        });

    </script>

</head>
<body>
    <div id="wrapper" style="width: 1000px;">
        <%--first column--%>
        <div class="ui-widget" style="width: 500px; float:left;">
            <p id="0" class="ui-state-default ui-corner-all ui-helper-clearfix" style="padding:0px; margin-top: 0px;">
                <span class="ui-icon ui-icon-transferthick-e-w" style="float:left; margin:-2px 5px 0 0;"></span>
                <spring:message code="credit.limit"/>
                <input id="input_limit_amount" type="number" style="margin: 0 0 0 0; padding:0px;" size="5"
                       max="${credit_limit}" min="<spring:message code="credit.LIMIT.Min"/>"/>
                <spring:message code="unit.uah"/>
                <div id="slider_limit"></div>
            </p>

            <p id="1" class="ui-state-highlight ui-corner-all ui-helper-clearfix" style="padding:0px;">
                <span class="ui-icon ui-icon-pin-s" style="float:left; margin:-2px 5px 0 0;"></span>
                <spring:message code="credit.term"/>
                <label id="slider_term_amount" style="border:0; color:#000000; font-weight:bold;">XX&nbsp;<spring:message code="term.month"/></label>
                <div id="slider_term"></div>
            </p>

            <p id="2" class="ui-state-default ui-corner-all ui-helper-clearfix" style="padding:0px;">
                <span class="ui-icon ui-icon-transferthick-e-w" style="float:left; margin:-2px 5px 0 0;"></span>
                <spring:message code="credit.payment"/>
                <input id="input_payment_amount" type="number" style="margin: 0 0 0 0; padding:0px;" size="5"/> <%-- min and max are defined in JS --%>
                <spring:message code="unit.uah"/>
                <div id="slider_payment"></div>
            </p>
        </div>

        <%--second column--%>
        <div class="ui-widget" style="width: 495px; float:left; margin-left: 5px">
            <table class="ui-widget" style="width: 100%" border="0"><tr><td>
                <input type="radio" name="payment_profile" value="annuitet" checked="true" disabled="true">
                    <spring:message code="credit.payment.profile.annuity"/>   <%--ravn chasti--%>
                </input>
              </td><td>
                <input type="radio" name="payment_profile" value="standard" disabled="true">
                    <spring:message code="credit.payment.profile.standard"/>
                </input>
            </td></tr>

            <tr><td>
                <spring:message code="credit.info.percent"/> <spring:message code="credit.info.percent.value"/>
            </td><td>
                <spring:message code="credit.info.overpayment"/> <label id="overpaymentValue">XXX</label> <spring:message code="unit.uah"/>
            </td></tr>
            <tr><td colspan="2">
                <spring:message code="credit.info.desired_limit"/>
                <input id="desired_limit" min="1000" max="99000" step="100" value="${credit_limit}" type="number" maxlength="5"/>
            </td></tr>
            </table>
        </div>
    </div>
<%--
    <br/>
    <div class="ui-widget" style="width: 1000px; text-align: center; clear: left;">
        <input id = "btnSubmit" type="button" value="<spring:message code="button.senddata"/>"/>
    </div>--%>
</body>
</html>