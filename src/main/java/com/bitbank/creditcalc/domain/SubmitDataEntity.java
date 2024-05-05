package com.bitbank.creditcalc.domain;

public class SubmitDataEntity {

    private String orderId;
    private String desiredLimit;
    private String calcLimit;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDesiredLimit() {
        return desiredLimit;
    }

    public void setDesiredLimit(String desiredLimit) {
        this.desiredLimit = desiredLimit;
    }

    public String getCalcLimit() {
        return calcLimit;
    }

    public void setCalcLimit(String calcLimit) {
        this.calcLimit = calcLimit;
    }

}
