package com.bitbank.creditcalc.mvc;

import com.bitbank.creditcalc.utils.ConfigurationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@RequestMapping("/smssender")
public class SmsSenderController {

    private static Logger log = Logger.getLogger(SmsSenderController.class);

    @Resource
    private ReloadableResourceBundleMessageSource resource;

    @Autowired
    private ConfigurationUtils config;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView mainPage() {
		ModelAndView model = new ModelAndView("smssender");

        model.addObject("smsUrl", config.getConfig().getString("sms.send.url"));

		return model;
	}

}