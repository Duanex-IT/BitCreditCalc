package com.bitbank.creditcalc.service;

import com.bitbank.creditcalc.domain.SubmitDataEntity;
import com.bitbank.creditcalc.stubs.senddata.BIT_spcGetLimit_spcServiceStub;

import java.rmi.RemoteException;

public interface SendDataService {
    BIT_spcGetLimit_spcServiceStub.GetLimitResponse sendData2Siebel(SubmitDataEntity submitData) throws RemoteException;
}
