package com.spring.boot.mybatis.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.spring.boot.mybatis.annotation.TargetDataSource;
import com.spring.boot.mybatis.common.DataSourceEnum;
import com.spring.boot.mybatis.entity.User;
import com.spring.boot.mybatis.mapper.UserMapper;
import com.spring.boot.mybatis.service.UserServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by shining on 2017-08-24.
 */
@Service
public class UserServiceImpl implements UserServiceI {
    @Autowired
    private UserMapper userMapper;
    @Transactional
    @Override
    @TargetDataSource(DataSourceEnum.master)
    public void insertUser(User user) {
        userMapper.insert(user);
    }

    @Override
    @TargetDataSource(DataSourceEnum.slaver)
    public PageInfo<User> findPageUserList(int page, int size) {
        PageHelper.startPage(page, size);
        List<User> users = userMapper.findUserList();
        PageInfo<User> userPageInfo = new PageInfo<User>(users);
        return userPageInfo;
    }
}
