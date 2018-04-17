package com.samsonan.aggregator.dao;

import com.samsonan.aggregator.domain.User;

public interface UserDao {

    User findByName(String username);

}