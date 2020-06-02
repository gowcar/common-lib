package com.jiaxintec.common.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Class Name:  BaseDao
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:31
 * Description:
 */
@Component
public interface BaseDao<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor, QuerydslPredicateExecutor<T> {
}
