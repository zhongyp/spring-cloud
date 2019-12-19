package com.zhongyp.consumer.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author zhongyp.
 * @date 2019/12/14
 */
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Data
public class User implements Serializable {

    @Id
    private long id;

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private Integer age;

    @Column
    private BigDecimal balance;
}
