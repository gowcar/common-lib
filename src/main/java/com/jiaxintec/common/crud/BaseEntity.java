package com.jiaxintec.common.crud;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Class Name:  BaseEntity
 * Author:      Jacky Zhang
 * Create Time: 2020/6/2 9:56 上午
 * Description:
 */
@Data
@MappedSuperclass
public class BaseEntity<X extends Serializable> implements Serializable
{
    @Id
    @Column(nullable = false)
    @GeneratedValue(generator = "custom-uuid")
    @GenericGenerator(name = "custom-uuid", strategy = "com.jiaxintec.common.unique.IdGenerator")
    private X id;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreationTimestamp
    private Date createTime;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @UpdateTimestamp
    private Date updateTime;

}
