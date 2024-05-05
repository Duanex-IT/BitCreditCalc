package com.bitbank.creditcalc.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationUtils {

    public static final String CREDITCALC_PROPERTIES = "creditcalc.properties";
    static Logger log = Logger.getLogger(ConfigurationUtils.class);

    public static final String BIT_CONFIG = "bit_config";

    PropertiesConfiguration config;

    public ConfigurationUtils() throws ConfigurationException {
        try {
            String filePath = System.getenv(BIT_CONFIG);
            log.debug("config folder: "+filePath);

            if (filePath == null) {
                filePath = System.getProperty(BIT_CONFIG, "");
                log.debug("config folder from property: "+filePath);
            }

            config = new PropertiesConfiguration(filePath+ CREDITCALC_PROPERTIES);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public PropertiesConfiguration getConfig() {
        return config;
    }

    public String getString(String key) {
        return config.getString(key);
    }
}
