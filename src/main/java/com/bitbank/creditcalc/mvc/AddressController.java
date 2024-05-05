package com.bitbank.creditcalc.mvc;

import com.bitbank.creditcalc.domain.AddressEntity;
import com.bitbank.creditcalc.service.AddressService;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Collection;

@Controller
@RequestMapping("/address")
public class AddressController {

    private static Logger log = Logger.getLogger(AddressController.class);

    @Resource
    private ReloadableResourceBundleMessageSource resource;

    @Autowired
    private PropertiesConfiguration config;

    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadAddressPage() {
        log.debug("loadAddressPage");
        ModelAndView model =new ModelAndView("address");

        model.addObject("regions", addressService.getRegions());
        model.addObject("applicationUrl", config.getString("application.url"));

        return model;
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE+"; charset=UTF-8")
    public @ResponseBody Collection<AddressEntity> findAddress(@RequestParam String region, @RequestParam String city, @RequestParam String street) {
        log.debug("region: "+region+", city: "+city+", street: "+street);
        return addressService.getAddressesForRegionCityStreet(region, city, street);
    }

    @RequestMapping(value = "/find/street", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE+"; charset=UTF-8")
    public @ResponseBody Collection<String> findStreet(@RequestParam(required = false) String region,
                                                       @RequestParam(required = false) String city,
                                                       @RequestParam String searchString) {
        log.debug("street search. region: "+region+", city: "+city+", street: "+searchString);
        return addressService.getStreets(region, city, searchString);
    }

    //todo url parameter to merge into one method
    @RequestMapping(value = "/find/city", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE+"; charset=UTF-8")
    public @ResponseBody Collection<String> findCity(@RequestParam(required = false) String region,
                                                     @RequestParam String searchString) {
        log.debug("city search. region: "+region+", city: "+searchString);
        return addressService.getCities(region, searchString);
    }

}
