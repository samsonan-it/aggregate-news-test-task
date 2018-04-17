package com.samsonan.aggregator.dao;

import java.util.List;

import javax.annotation.Nullable;

import com.samsonan.aggregator.domain.NewsItem;

public interface NewsItemDao {

    List<NewsItem> findAll(@Nullable String filter);

    List<NewsItem> findAllForUser(String username, @Nullable String filter);
    
    NewsItem findById(Long newsItemId);
    
    void save(NewsItem newsItem);

    List<NewsItem> findAllForFeedId(Long feedId);
}