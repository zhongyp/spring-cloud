package com.zhongyp.provider.repository;

import com.zhongyp.provider.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zhongyp.
 * @date 2019/12/16
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
