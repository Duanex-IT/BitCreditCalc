package com.bitbank.creditcalc.utils;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * User: oleg.danilyuk
 * Date: 19.06.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class SoapUtilsTest {

    static Logger log = Logger.getLogger(SoapUtilsTest.class);

    @Autowired
    private SoapUtils utils;

    @Resource
    ReloadableResourceBundleMessageSource resource;

    private final String ETALON_HEADER = "<wsse:Security xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/07/secext\">" +
            "<wsse:UsernameToken>" +
                "<wsse:Password Type=\"wsse:PasswordText\">passw</wsse:Password>" +
                "<wsse:Username>uname</wsse:Username>" +
            "</wsse:UsernameToken></wsse:Security>";

    @Test
    public void testGetHeader() {
        OMElement xmlHeader = utils.getSiebelHeader("uname", "passw");

        assertNotNull(xmlHeader);

        log.debug("==============");
        log.debug(xmlHeader);
        log.debug("==============");
        assertEquals(ETALON_HEADER, xmlHeader.toString());
    }

    @Test
    public void testGetDefaultHeader() {
        String user = resource.getMessage("soap.header.user", null, Locale.getDefault());
        String passw = resource.getMessage("soap.header.password", null, Locale.getDefault());

        OMElement xmlHeader = utils.getSiebelHeader();

        assertNotNull(xmlHeader);

        log.debug("==============");
        log.debug(xmlHeader);
        log.debug("==============");

        String testHeader = "<wsse:Security xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/07/secext\">" +
                "<wsse:UsernameToken>" +
                "<wsse:Password Type=\"wsse:PasswordText\">" + passw + "</wsse:Password>" +
                "<wsse:Username>" + user + "</wsse:Username>" +
                "</wsse:UsernameToken></wsse:Security>";
        assertEquals(testHeader, xmlHeader.toString());
    }

}
