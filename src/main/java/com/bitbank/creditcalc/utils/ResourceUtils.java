package com.bitbank.creditcalc.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ResourceUtils {

    public static final String STRING_SPLITTER = ",";
    @Resource
    ReloadableResourceBundleMessageSource resource;

    public String getMessage(String key) {
        return resource.getMessage(key, null, Locale.getDefault());
    }

    public String getMessage(String key, Object arg) {
        return resource.getMessage(key, new Object[] {arg}, Locale.getDefault());
    }

    public int getIntMessage(String key) {
        return Integer.parseInt(resource.getMessage(key, null, Locale.getDefault()));
    }

    /**
     * Extracts values from aggregateKey in format aggregateKey=key1,key2...
     * @param aggregateKey key containing a set of keys as a value
     * @return list of values from key1,key2...
     */
    public List<String> getMessagesFromKey(String aggregateKey) {
        List<String> result = new ArrayList<>();

        String keysString = getMessage(aggregateKey);
        if (StringUtils.isNotEmpty(keysString)) {
            String[] keysList = keysString.split(STRING_SPLITTER);
            for (int i=0; i<keysList.length; i++) {
                result.add(getMessage(keysList[i]));
            }
        }

        return result;
    }

}
