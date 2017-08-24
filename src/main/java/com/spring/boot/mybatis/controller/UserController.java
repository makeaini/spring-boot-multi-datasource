package com.spring.boot.mybatis.controller;

import com.github.pagehelper.PageInfo;
import com.spring.boot.mybatis.entity.User;
import com.spring.boot.mybatis.service.UserServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shining on 2017-08-24.
 */
@RestController
public class UserController {
    private static final Logger LOG= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceI userServiceI;
    @GetMapping("insertUser")
    public String insertUser(){
        User user = new User();
        user.setName("133");
        user.setPassword("2333");
        userServiceI.insertUser(user);
        return "success";
    }


    @GetMapping("/findUserPageList")
    public PageInfo<User> findUserPageList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "2")Integer size){
        LOG.debug("第一个请求findUserPageList");
        User u = new User();
        PageInfo<User> userPageInfo =  userServiceI.findPageUserList(page,size);
        return userPageInfo;
    }
}
