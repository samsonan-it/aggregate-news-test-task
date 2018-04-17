package com.samsonan.aggregator.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.samsonan.aggregator.domain.Feed;

/**
 * 
 * UI DTO Form
 *
 */
public class FeedForm {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @URL
    private String url;
    
    private String rule;

    public FeedForm() {
    }

    public FeedForm(Feed feed) {
        this.id = feed.getId();
        this.name = feed.getName();
        this.url = feed.getUrl();
        this.rule = feed.getParseRule();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String parsingRule) {
        this.rule = parsingRule;
    }

    public Feed asFeed(String owner) {
        Feed feed = new Feed();
        
        feed.setName(getName());
        feed.setUrl(getUrl());
        feed.setParseRule(getRule());
        feed.setOwner(owner);
        
        return feed;
    }
    
    @Override
    public String toString() {
        return "NewFeedForm [id=" + id + ", name=" + name + ", url=" + url + ", rule=" + rule + "]";
    }

}
