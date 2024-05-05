package com.bitbank.creditcalc.mvc;

import com.bitbank.creditcalc.utils.ConfigurationUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Locale;

@Controller
@RequestMapping("/creditcalc")
public class MainController {

    private static Logger log = Logger.getLogger(MainController.class);

    @Resource
    private ReloadableResourceBundleMessageSource resource;

    @Autowired
    private ConfigurationUtils config;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView mainPage(@RequestParam(required = false) Long limit,
                                 @RequestParam(required = false) Long term,
                                 @RequestParam(required = false) String orderId) {
        log.debug("Got parameters: limit="+limit+", term="+term+", orderId="+orderId);

		ModelAndView model = new ModelAndView("index");

        if (limit==null) {
            model.addObject("credit_limit", getDefaultLong("credit.LIMIT.Max"));
        } else {
            model.addObject("credit_limit", limit);
        }

        if (term==null) {
            model.addObject("credit_term", getDefaultLong("credit.Term.Max"));
        } else {
            model.addObject("credit_term", term);
        }

        model.addObject("credit_orderId", orderId);
        model.addObject("applicationUrl", config.getConfig().getString("application.url"));

		return model;
	}

    private Long getDefaultLong(String messageKey) {
        Long result;
        try {
            result = Long.parseLong(resource.getMessage(messageKey, null, Locale.getDefault()));
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            throw e;
        }

        return result;
    }
}