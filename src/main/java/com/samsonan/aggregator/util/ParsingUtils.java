package com.samsonan.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samsonan.aggregator.domain.ParsingRule;
import com.samsonan.aggregator.service.exception.ParsingException;

public class ParsingUtils {
    
    private static final Logger log = LoggerFactory.getLogger(ParsingUtils.class);

    public static ParsingRule parseRule(String jsonString) throws ParsingException {
        
        if (jsonString.isEmpty()) {
            return new ParsingRule(); // default rule (for RSS)
        }
        
        ObjectMapper mapper = new ObjectMapper();
        ParsingRule rule = null;
        try {
            rule = mapper.readValue(jsonString, ParsingRule.class);
            return rule;
        } catch (Exception e) {
            log.error("Exception parsing json string={}", jsonString, e);
            throw new ParsingException(e.getMessage());
        }
    }
    
}
