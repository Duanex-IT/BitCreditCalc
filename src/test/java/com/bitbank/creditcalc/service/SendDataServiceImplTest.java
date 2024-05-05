package com.bitbank.creditcalc.service;

import com.bitbank.creditcalc.domain.SubmitDataEntity;
import com.bitbank.creditcalc.stubs.senddata.BIT_spcGetLimit_spcServiceStub;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.rmi.RemoteException;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleg.danilyuk
 * Date: 03.07.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class SendDataServiceImplTest {

    @Autowired
    private SendDataService sendDataService;

    @Test
    public void testSendData2Siebel() throws RemoteException {
        //initial data
        SubmitDataEntity submitDataEntity = new SubmitDataEntity();
        submitDataEntity.setCalcLimit("4000");
        submitDataEntity.setDesiredLimit("5000");
        submitDataEntity.setOrderId("234-234");

        //perform call
        BIT_spcGetLimit_spcServiceStub.GetLimitResponse response = sendDataService.sendData2Siebel(submitDataEntity);

        //test
        assertEquals("4", response.getError_spcCode());//todo need to have orderId for test
    }

}
