package com.samsonan.aggregator.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.samsonan.aggregator.dao.UserDao;
import com.samsonan.aggregator.domain.User;

/**
 * The users are not persisted, entities will always be detached 
 */
@Repository
public class UserDaoJpa implements UserDao {

    @PersistenceContext
    public EntityManager entityManager;
    
    @Override
    public User findByName(String username) {
        
        // TODO: maybe better rewrite using criteria and metadata 
        return entityManager.createQuery("select u from User u WHERE username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
        
    }

}
