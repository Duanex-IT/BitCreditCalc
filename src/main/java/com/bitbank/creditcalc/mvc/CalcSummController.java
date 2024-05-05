package com.bitbank.creditcalc.mvc;

import org.apache.log4j.Logger;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Locale;

@Controller
@RequestMapping("/calcReturnSumm")
public class CalcSummController {

    private static Logger log = Logger.getLogger(CalcSummController.class);

    @Resource
    private ReloadableResourceBundleMessageSource resource;

    @RequestMapping(method = RequestMethod.GET)
	public ModelAndView calcSumPage(@RequestParam(required = false) Long limit) {
        log.debug("Got parameters: limit="+limit);
        ModelAndView model = new ModelAndView("calcReturnSumm");

        if (limit==null) {
            model.addObject("credit_limit", getDefaultLong("credit.LIMIT.Max"));
        } else {
            model.addObject("credit_limit", limit);
        }

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