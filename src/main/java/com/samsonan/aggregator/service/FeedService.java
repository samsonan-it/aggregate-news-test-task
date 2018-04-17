package com.samsonan.aggregator.service;

import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.samsonan.aggregator.dao.FeedDao;
import com.samsonan.aggregator.dao.NewsItemDao;
import com.samsonan.aggregator.domain.Feed;
import com.samsonan.aggregator.domain.NewsItem;
import com.samsonan.aggregator.dto.FeedForm;

/**
 * Service for general operations with feeds and news
 *
 */
@Service
public class FeedService {

    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(FeedService.class);
    
    private final FeedDao feedDao;
    private final NewsItemDao newsItemDao;
    
    @Autowired
    public FeedService(FeedDao feedDao, NewsItemDao newsItemDao) {
        this.feedDao = feedDao;
        this.newsItemDao = newsItemDao;
    }

    public Feed addFeed(FeedForm feedForm) {
        
        Feed feed = feedForm.asFeed(getCurrentUserLogin());
        feedDao.save(feed);
        return feed;
    }

    public List<NewsItem> getAllNews(@Nullable String filter) {
        return newsItemDao.findAll(filter);
    }

    public List<NewsItem> getAllNewsForUser(String username, @Nullable String filter) {
        return newsItemDao.findAllForUser(username, filter);
    }
    
    public void deleteFeed(Feed feed) {
        feedDao.delete(feed);
    }

    private String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Feed getFeedById(Long feedId) {
        return feedDao.findById(feedId);
    }
    
    public List<Feed> getAllFeeds() {
        return feedDao.findAll();
    }      
    
    public List<Feed> getAllFeedsForUser(String currentUsername) {
        return feedDao.findAllForUser(currentUsername);
    }    
    
    public void updateFeedNews(Feed feed, List<NewsItem> news) {
        List<NewsItem> oldNews = newsItemDao.findAllForFeedId(feed.getId());
        news.removeAll(oldNews);
        news.forEach(newsItemDao :: save);
    }
}
