package com.samsonan.aggregator.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;

@Entity
@Table(name = "NEWS_ITEM")
public class NewsItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "link")
    private String link;

    @ManyToOne
    @JoinColumn(name = "news_feed_id")
    private Feed feed;

    public NewsItem() {
    }
    
    public NewsItem(String title, String description, String link, Feed feed) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.feed = feed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }
    
    @Override
    public String toString() {
        return "NewsItem [id=" + id + ", title=" + title + ", description=" + description + ", link=" + link + ", feed="
                + feed + "]";
    }

    /**
     * Thats a good question - which news item should be considered as equal
     */
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.title, this.link, this.feed.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final NewsItem other = (NewsItem) obj;
        return Objects.equal(this.title, other.title)
            && Objects.equal(this.link, other.link)
            && Objects.equal(this.feed.getId(), other.feed.getId());
    }
       
    
    
    

}
