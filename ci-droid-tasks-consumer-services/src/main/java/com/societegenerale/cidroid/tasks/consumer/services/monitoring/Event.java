package com.societegenerale.cidroid.tasks.consumer.services.monitoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * Enabling monitoring by leveraging on MDC, in which we store key/value pairs that will be logged.
 */
public class Event {
    private static final String TECHNICAL_TYPE = "TECHNICAL";

    private static final String NAME_ATTRIBUTE = "metricName";

    private static final String TYPE_ATTRIBUTE = "type";

    private final Map<String, String> attributes = new HashMap();

    private final Logger logger;

    private Event(String name, String type) {
        this.attributes.put(NAME_ATTRIBUTE, name);
        this.attributes.put(TYPE_ATTRIBUTE, type);
        this.logger = LoggerFactory.getLogger(type);
    }

    public static Event technical(String name) {
        return new Event(name, TECHNICAL_TYPE);
    }

    public synchronized Event addAttribute(String name, String value) {
        this.attributes.putIfAbsent(name, value);
        return this;
    }

    public synchronized void publish() {
        Map copyOfMDC = MDC.getCopyOfContextMap();

        try {
            this.attributes.forEach(MDC::put);
            this.logger.info("");
        } finally {
            if (copyOfMDC != null) {
                MDC.setContextMap(copyOfMDC);
            }

        }

    }
}
