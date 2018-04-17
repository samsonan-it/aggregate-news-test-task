package com.samsonan.aggregator.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.samsonan.aggregator.dao.NewsItemDao;
import com.samsonan.aggregator.domain.Feed;
import com.samsonan.aggregator.domain.NewsItem;

@Repository
@Transactional
public class NewsItemDaoJpa implements NewsItemDao {

    private static Logger log = LoggerFactory.getLogger(NewsItemDaoJpa.class);
    
    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public List<NewsItem> findAll(@Nullable String filter) {
        return findAllForUser(null, filter);
    }
    
    @Override
    public List<NewsItem> findAllForUser(String username, @Nullable String filter) {
        
        log.debug("findAllForUser. user={}, filter={}", username, filter);
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<NewsItem> cq = cb.createQuery(NewsItem.class);
        Root<NewsItem> newsItem = cq.from(NewsItem.class);
        cq.select(newsItem);
        
        // TODO: use metadata
        
        List<Predicate> predicates = new ArrayList<Predicate>();
        
        if (!StringUtils.isEmpty(username)) {
            
            Join<NewsItem, Feed> feed = newsItem.join("feed");
            predicates.add(cb.equal(feed.get("owner"), username));
            
        }

        if (!StringUtils.isEmpty(filter)) {
            predicates.add(cb.like(cb.upper(newsItem.get("title")), "%" + filter.toUpperCase() + "%"));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        
        return entityManager.createQuery(cq).getResultList();
    }  

    @Override
    public List<NewsItem> findAllForFeedId(Long feedId) {
        return entityManager.createQuery("select n from NewsItem n WHERE n.feed.id = :feedId", NewsItem.class)
                .setParameter("feedId", feedId)
                .getResultList();
    }
    
    @Override
    public NewsItem findById(Long newsItemId) {
        return entityManager.find(NewsItem.class, newsItemId);
    }

    @Override
    public void save(NewsItem newsItem) {
        entityManager.persist(newsItem);        
    }

}
