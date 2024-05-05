package com.bitbank.creditcalc.service;

import com.bitbank.creditcalc.domain.SubmitDataEntity;
import com.bitbank.creditcalc.stubs.senddata.BIT_spcGetLimit_spcServiceStub;
import com.bitbank.creditcalc.utils.ConfigurationUtils;
import com.bitbank.creditcalc.utils.SoapUtils;
import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;

@Service
public class SendDataServiceImpl implements SendDataService {

    private static Logger log = Logger.getLogger(SendDataServiceImpl.class);

    @Autowired
    private ConfigurationUtils config;

    @Autowired
    private SoapUtils soapUtils;

    @Override
    public BIT_spcGetLimit_spcServiceStub.GetLimitResponse sendData2Siebel(SubmitDataEntity submitData) throws RemoteException {
        BIT_spcGetLimit_spcServiceStub stub;
        try {
            stub = new BIT_spcGetLimit_spcServiceStub(config.getString("senddata.url"));
            stub._getServiceClient().addHeader(soapUtils.getSiebelHeader());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(config.getConfig().getInt("siebel.senddata.timeout.ms"));
        } catch (AxisFault axisFault) {
            log.error(axisFault.getMessage());
            throw axisFault;
        }

        BIT_spcGetLimit_spcServiceStub.GetLimit getLimit = new BIT_spcGetLimit_spcServiceStub.GetLimit();
        getLimit.setCalcLimit(submitData.getCalcLimit());
        getLimit.setDesiredLimit(submitData.getDesiredLimit());
        getLimit.setOpptyId(submitData.getOrderId());

        try {
            BIT_spcGetLimit_spcServiceStub.GetLimitResponse response = stub.getLimit(getLimit);
            log.debug("Error message: "+response.getError_spcMessage());
            log.debug("Error code: "+response.getError_spcCode());

            return response;
        } catch (RemoteException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
