

/**
 * BIT_spcGetLimit_spcServiceTest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package com.bitbank.creditcalc.stubs.senddata;

    /*
     *  BIT_spcGetLimit_spcServiceTest Junit test case
    */

import com.bitbank.creditcalc.utils.ConfigurationUtils;
import com.bitbank.creditcalc.utils.SoapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class BIT_spcGetLimit_spcServiceTest {

    @Autowired
    private SoapUtils soapUtils;

    @Autowired
    private ConfigurationUtils config;

    /**
     * Auto generated test method
     */
    @Test
    public void testgetLimit() throws java.lang.Exception {

        BIT_spcGetLimit_spcServiceStub stub =
                new BIT_spcGetLimit_spcServiceStub(config.getConfig().getString("senddata.url"));
        stub._getServiceClient().addHeader(soapUtils.getSiebelHeader());

        BIT_spcGetLimit_spcServiceStub.GetLimit getLimit4 =
                (BIT_spcGetLimit_spcServiceStub.GetLimit) getTestObject(BIT_spcGetLimit_spcServiceStub.GetLimit.class);
        getLimit4.setCalcLimit("4000");
        getLimit4.setDesiredLimit("6000");
        getLimit4.setOpptyId("34-234");

        assertNotNull(stub.getLimit(getLimit4));


    }

    /**
     * Auto generated test method
     */
    public void testStartgetLimit() throws java.lang.Exception {
        BIT_spcGetLimit_spcServiceStub stub = new BIT_spcGetLimit_spcServiceStub();
        BIT_spcGetLimit_spcServiceStub.GetLimit getLimit4 =
                (BIT_spcGetLimit_spcServiceStub.GetLimit) getTestObject(BIT_spcGetLimit_spcServiceStub.GetLimit.class);
        // TODO : Fill in the getLimit4 here


        stub.startgetLimit(
                getLimit4,
                new tempCallbackN1000C()
        );


    }

    private class tempCallbackN1000C extends BIT_spcGetLimit_spcServiceCallbackHandler {
        public tempCallbackN1000C() {
            super(null);
        }

        public void receiveResultgetLimit(
                BIT_spcGetLimit_spcServiceStub.GetLimitResponse result
        ) {

        }

        public void receiveErrorgetLimit(java.lang.Exception e) {
            fail();
        }

    }

    //Create an ADBBean and provide it as the test object
    public org.apache.axis2.databinding.ADBBean getTestObject(java.lang.Class type) throws java.lang.Exception {
        return (org.apache.axis2.databinding.ADBBean) type.newInstance();
    }


}
    