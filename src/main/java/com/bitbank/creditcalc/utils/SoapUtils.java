package com.bitbank.creditcalc.utils;

import org.apache.axiom.om.OMElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Locale;

@Service
public class SoapUtils {

    @Resource
    private ReloadableResourceBundleMessageSource resource;

    @Autowired
    private com.bitbank.bitutils.utils.SoapUtils soaplib;

    public OMElement getSiebelHeader(String user, String password) {
        return soaplib.getSiebelHeader(user, password);
    }

    public OMElement getSiebelHeader() {
        return getSiebelHeader(resource.getMessage("soap.header.user", null, Locale.getDefault()),
                resource.getMessage("soap.header.password", null, Locale.getDefault()));
    }

}
