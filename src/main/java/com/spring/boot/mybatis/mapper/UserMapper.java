package com.spring.boot.mybatis.mapper;

import com.spring.boot.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by shining on 2017-08-16.
 */
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(name,password) VALUES (#{name}, #{password}) ")
    @Options(useGeneratedKeys= true, keyProperty="id")
    int insert(User user);

    @Select(value = "select * from user")
    List<User> findUserList();

}
