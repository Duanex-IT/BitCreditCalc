package com.bitbank.creditcalc.service;

import com.bitbank.creditcalc.domain.AddressEntity;

import java.util.Collection;
import java.util.List;

public interface AddressService {

    void reloadAddressesFromSiebel();

    Collection<String> getStreets(String region, String city, String searchString);

    Collection<String> getCities(String region, String searchString);

    List<String> getRegions();

    Collection<AddressEntity> getAddressesForRegionCityStreet(String region, String city, String street);
}
