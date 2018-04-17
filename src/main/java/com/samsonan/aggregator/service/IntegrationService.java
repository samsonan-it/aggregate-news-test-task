package com.samsonan.aggregator.service;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.samsonan.aggregator.service.exception.RemoteServiceException;

/**
 * Service we use to read from external resources 
 *
 */
@Service
public class IntegrationService {

    private static final Logger log = LoggerFactory.getLogger(IntegrationService.class);
    
    /**
     * If multiple users use one feed, no need to actually request it every time
     */
    @Cacheable("feed-content")
    public InputStream read(String feedUrl) throws RemoteServiceException {
        
        log.debug("downloading content for feed url={}", feedUrl);
        
        try {
            return new URL(feedUrl).openStream();
        } catch (Exception e) { // MalformedURLException, IOException
            throw new RemoteServiceException(e);
        }
        
    }
    
}
