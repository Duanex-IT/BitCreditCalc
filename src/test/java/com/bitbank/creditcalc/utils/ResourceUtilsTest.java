package com.bitbank.creditcalc.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * User: oleg.danilyuk
 * Date: 20.06.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml"})
public class ResourceUtilsTest {

    @Autowired
    private ResourceUtils resource;

    @Test
    public void testGetMessage() {
        String msg = resource.getMessage("test");

        assertNotNull(msg);
        assertEquals("testMsg", msg);
    }

    @Test
    public void testGetMessagesFromKey() {
        List<String> msgList = resource.getMessagesFromKey("testAggregate");

        assertNotNull(msgList);
        assertEquals(3, msgList.size());

        assertTrue(msgList.contains("testOne"));
        assertTrue(msgList.contains("testTwo"));
        assertTrue(msgList.contains("testThree"));
    }

}
