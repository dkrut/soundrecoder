package com.github.dkrut.soundrecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Property {
    private static final Logger log = LoggerFactory.getLogger(Property.class);
    private static final String PROPERTY_FILE = "src/main/resources/app.properties";

    public String getProperty(String propertyName) {
        Properties properties = new Properties();
        try {
            log.debug("Load property file ");
            properties.load(new FileInputStream(PROPERTY_FILE));
        } catch (IOException e) {
            log.error("Error loading property file: "  + e.getMessage());
            e.printStackTrace();
        }
        log.debug("Get property '{}'", propertyName);
        return properties.getProperty(propertyName);
    }
}
