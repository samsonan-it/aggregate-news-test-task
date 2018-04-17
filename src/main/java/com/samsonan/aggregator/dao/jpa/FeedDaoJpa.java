package com.samsonan.aggregator.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.samsonan.aggregator.dao.FeedDao;
import com.samsonan.aggregator.domain.Feed;

@Repository
@Transactional
public class FeedDaoJpa implements FeedDao {

    @PersistenceContext
    public EntityManager entityManager;

    @Override
    public void save(Feed feed) {
        entityManager.persist(feed);
    }

    @Override
    public Feed findById(Long feedId) {
        return entityManager.find(Feed.class, feedId);
    }

    @Override
    public void delete(Feed feed) {
        entityManager.remove(feed);
    }

    @Override
    public List<Feed> findAll() {
        return entityManager.createQuery("select f from Feed f", Feed.class)
                .getResultList();
    }

    @Override
    public List<Feed> findAllForUser(String username) {
        return entityManager.createQuery("select f from Feed f WHERE f.owner = :username", Feed.class)
                .setParameter("username", username)
                .getResultList();
    }

}
