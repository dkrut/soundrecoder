package com.github.dkrut.soundrecorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final Logger log = LoggerFactory.getLogger(Property.class);
    private static final String PROPERTY_SOURCE = "src/main/resources/";
    private static final String PROPERTY_FILE = "app.properties";

    public String getProperty(String propertyName) {
        Properties properties = new Properties();
        try {
            log.debug("Load property file ");
            properties.load(new FileInputStream(PROPERTY_SOURCE+PROPERTY_FILE));
        } catch (IOException e) {
            log.error("Error loading property file: "  + e.getMessage());
            e.printStackTrace();
        }
        log.debug("Get property '{}'", propertyName);
        String property = properties.getProperty(propertyName);
        if (property == null) {
            log.error("Not find property '{}' in '{}'", propertyName, PROPERTY_FILE );
            return "0";
        }
        else return property;
    }
}
