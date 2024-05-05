package com.bitbank.creditcalc.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: oleg.danilyuk
 * Date: 21.10.13
 */
public class AddressUtilsTest {

    @Test
    public void testFindDistance() {
        assertEquals(14, AddressUtils.findDistance("Киев ул.Бориспольская 64", "Киев Борис"));
        assertEquals(15, AddressUtils.findDistance("Киев ул.Бориспольская 64", "Киев борис"));
        assertEquals(10, AddressUtils.findDistance("Киев ул.Борисова 64", "Киев борис"));
        assertEquals(5, AddressUtils.findDistance("сплячка", "тебе сплячка"));

        assertEquals(4, AddressUtils.findDistance("1234", "5678"));
    }

    @Test
    public void testSplitString() {
        String ccase = "This IsCamel CaseString";
        String ccase2 = "this.IS-Camel Case,String";
        String ccase3 = "thisISS CAMEL.CaseString";
        String ccase4 = "thisISS.CAMELCaseString";
        String ccase5 = "ThisIsCamelCaseString";
        String ccase6 = "thisISCamelCaseString";
        String ccase7 = "thisISCAMELCaseString";
        String ccase8 = "";

        List<String> ccaseList = AddressUtils.splitAndLowercaseString(ccase);
        List<String> ccaseList2 = AddressUtils.splitAndLowercaseString(ccase2);
        List<String> ccaseList3 = AddressUtils.splitAndLowercaseString(ccase3);
        List<String> ccaseList4 = AddressUtils.splitAndLowercaseString(ccase4);
        List<String> ccaseList5 = AddressUtils.splitAndLowercaseString(ccase5);
        List<String> ccaseList6 = AddressUtils.splitAndLowercaseString(ccase6);
        List<String> ccaseList7 = AddressUtils.splitAndLowercaseString(ccase7);
        List<String> ccaseList8 = AddressUtils.splitAndLowercaseString(ccase8);

        assertEquals(5, ccaseList.size());
        assertEquals(5, ccaseList2.size());
        assertEquals(5, ccaseList3.size());
        assertEquals(5, ccaseList4.size());
        assertEquals(5, ccaseList5.size());
        assertEquals(5, ccaseList6.size());
        assertEquals(4, ccaseList7.size());
        assertEquals(1, ccaseList8.size());
    }

}
