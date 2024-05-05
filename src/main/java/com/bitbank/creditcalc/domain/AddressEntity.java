package com.bitbank.creditcalc.domain;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Map;


public class AddressEntity implements Serializable {

    private String city;

    private String cityType;

    private String street;

    private String region;

    private String district;

    private String zipcode;

    public AddressEntity() {
    }

    public AddressEntity(Map<String, Object> row) {
        if (StringUtils.isNotEmpty((String) row.get("addr"))) {
            this.street = (String) row.get("addr");
        }
        if (StringUtils.isNotEmpty((String) row.get("addr_line_2"))) {
            this.cityType = (String) row.get("addr_line_2");
        }
        if (StringUtils.isNotEmpty((String) row.get("city"))) {
            this.city = (String) row.get("city");
        }
        if (StringUtils.isNotEmpty((String) row.get("country"))) {
            this.region = (String) row.get("country");
        }
        if (StringUtils.isNotEmpty((String) row.get("zipcode"))) {
            this.zipcode = (String) row.get("zipcode");
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
