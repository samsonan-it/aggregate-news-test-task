package com.samsonan.aggregator.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.samsonan.aggregator.domain.Feed;
import com.samsonan.aggregator.domain.NewsItem;

/**
 * All sync related methods, threads and schedules
 */
@Service
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private ExecutorService singleThreadPool;
    
    private final ParsingService parsingService;
    private final FeedService feedService;

    @Autowired
    public SyncService(ParsingService parsingService, FeedService feedService) {
        this.parsingService = parsingService;
        this.feedService = feedService;
    }

    @PostConstruct
    public void init() {

        log.debug("[sync service] pool init.");

        /**
         * What if it is occupied? -> tasks will be queued
         */
        singleThreadPool = Executors.newCachedThreadPool();
    }

    @PreDestroy
    protected void shutdownExecutor() {

        log.debug("[sync service] pool shutdown.");

        singleThreadPool.shutdown();
    }

    /**
     * Yes, there is now one scheduled task which sync all feeds, one by one. They
     * are not concurrent. For multiple concurrently running task use
     * ScheduledThreadPool
     */
    @Scheduled(fixedRateString = "${sync.delay}", initialDelayString = "${initial.sync.delay}")
    public void syncAllNewsFeeds() {
        log.debug("[sync service] sync all feeds started");
        feedService.getAllFeeds().forEach(this :: refreshFeedNews);
    }

    /**
     * We run sync right after feed is added.
     * Potentially it may lead to duplicates, because it may happen that the scheduled sync is started, which will sync
     * newly created feed. It can be easily solved by adding flag, which will prevent schedulted sync before initial sync is completed.
     */
    public void syncSingleFeedAsync(Feed feed) {
        singleThreadPool.submit(() -> refreshFeedNews(feed));
    }
    
    private void refreshFeedNews(Feed feed) {
        List<NewsItem> newsReceived = parsingService.parse(feed);
        log.debug("feed {} parsed. news received = {}", feed, newsReceived.size());
        feedService.updateFeedNews(feed, newsReceived);
    }

}
