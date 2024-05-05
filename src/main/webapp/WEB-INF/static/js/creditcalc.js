function calcMonthPayment(limit, term, month_percent) {
    //V = month_percent / (1 - (1 + month_percent)^-term) * limit
    return Math.round((month_percent*limit)/(1 - (1/Math.pow(1+month_percent,term))));
    //how we need rounding to 5 e.g. 72 will be 70 and 73 will be 75
    //return Math.round(preciseResult/5)*5;
}

function calcLimit(term, payment, month_percent) {
    return Math.round(payment*(1 - (1/Math.pow(1+month_percent,term))) / month_percent);
}

function calcTerm(limit, payment, month_percent) {
    return Math.round(limit / (payment - (month_percent * limit)));
}

function calcOverpayment() {
    limit = $("#slider_limit" ).slider( "value");
    term = $("#slider_term" ).slider( "value");
    payment = $("#slider_payment" ).slider( "value");

    overpayment = term*payment - limit;
    //alert(limit+", "+term+", "+payment+", "+overpayment);
    document.getElementById('overpaymentValue').innerHTML = overpayment;
}

/**
 * order_id - string
 * desiredLimit - integer
 * calcLimit - integer
 * activities -
 * pages -
 */
function siebelSender(order_id, desiredLimit, calcLimit, sendDataUrl) {
    var requestObj = {};
    requestObj.orderId = order_id;
    requestObj.desiredLimit = desiredLimit;
    requestObj.calcLimit = calcLimit;

    //send information to siebel
    $.ajax({
        type: "POST",
        url: sendDataUrl,
        async: false,
        data: JSON.stringify(requestObj),
        contentType: "application/json;charset=UTF-8",
        success: function(pageResponse){
            alert(pageResponse);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status+": "+thrownError);
        }
    });

}
