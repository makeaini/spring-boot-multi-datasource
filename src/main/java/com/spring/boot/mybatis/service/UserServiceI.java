package com.spring.boot.mybatis.service;

import com.github.pagehelper.PageInfo;
import com.spring.boot.mybatis.entity.User;

/**
 * Created by shining on 2017-08-24.
 */
public interface UserServiceI {
    public void insertUser(User user);
    public PageInfo<User> findPageUserList(int page,int size);
}
