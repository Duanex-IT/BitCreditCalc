package com.bitbank.creditcalc.mvc;

import com.bitbank.creditcalc.domain.SubmitDataEntity;
import com.bitbank.creditcalc.service.SendDataService;
import com.bitbank.creditcalc.stubs.senddata.BIT_spcGetLimit_spcServiceStub;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;

@Controller
@RequestMapping("/creditcalc/senddata")
public class SubmitController {

    private static Logger log = Logger.getLogger(SubmitController.class);

    @Autowired
    private SendDataService sendDataService;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE+"; charset=UTF-8")
    public @ResponseBody String sendToSiebel(@RequestBody SubmitDataEntity inputData) {
        try {
            BIT_spcGetLimit_spcServiceStub.GetLimitResponse response = sendDataService.sendData2Siebel(inputData);
            return response.getError_spcCode()+", msg: "+response.getError_spcMessage();
        } catch (RemoteException e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}