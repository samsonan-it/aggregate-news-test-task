package com.samsonan.aggregator.dao;

import java.util.List;

import com.samsonan.aggregator.domain.Feed;

public interface FeedDao {

    List<Feed> findAll();

    List<Feed> findAllForUser(String username);

    Feed findById(Long feedId);

    void save(Feed feed);

    void delete(Feed feed);

}