package com.zhongyp.provider.controller;

import com.zhongyp.provider.bean.User;
import com.zhongyp.provider.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongyp.
 * @date 2019/12/16
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping("/{id}")
    public User findById(@PathVariable Long id) {
        User user = this.userRepository.getOne(id);
        return user;
    }

}
