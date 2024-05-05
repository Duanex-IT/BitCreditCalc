package com.bitbank.creditcalc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: oleg.danilyuk
 * Date: 15.10.13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
//@ContextConfiguration("classpath:mvc-dispatcher-servlet.xml")
public class AddressServiceImplTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void testFindBrovaryCity() throws NoSuchFieldException, IllegalAccessException {
        Collection<String> citiesResult = addressService.getCities(null, "бровары");
        assertNotNull(citiesResult);
        assertEquals(3, citiesResult.size());
    }

    @Test
    public void testGetCities() {
        Collection<String> cities = addressService.getCities("Київ", "Бровари");
        Collection<String> cities2 = addressService.getCities("Київська обл.", "Бровари");
        Collection<String> cities3 = addressService.getCities("", "Калинівка");

        assertEquals(0, cities.size());
        assertEquals(1, cities2.size());
        assertTrue("2 < "+cities3.size(), 2 < cities3.size());
    }

    @Test
    public void testGetStreets() {
        Collection<String> cities1 = addressService.getStreets(null, null, "Закревського");
        Collection<String> cities2 = addressService.getStreets(null, "Київ", "Закревського");
        Collection<String> cities3 = addressService.getStreets(null, "Бровари", "Закревського");
        Collection<String> cities4 = addressService.getStreets("", null, "Калинівка");
        Collection<String> cities5 = addressService.getStreets("Київ", "Київ", "Бровари");
        Collection<String> cities6 = addressService.getStreets("Київ", "Київ", "Бровари");

        assertEquals(2, cities1.size());
        assertEquals(1, cities2.size());
//        assertTrue(2 < cities3.size());
    }


    @Test
    public void testGetStreetsFindByStart() {
        List<String> dict = new ArrayList<>(2);
        dict.add("вул.");
        dict.add("Закревського");
        int dist = ((AddressServiceImpl)addressService).returnAddressIfSimilar(Collections.singletonList("Закр"), dict, 16);

        assertTrue(dist >= 0);
    }
}
