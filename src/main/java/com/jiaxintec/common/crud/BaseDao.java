package com.jiaxintec.common.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Component;

/**
 * Class Name:  BaseDao
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:31
 * Description:
 */
@Component
public interface BaseDao<T, X> extends JpaRepository<T, X>, QuerydslPredicateExecutor<T> {

}
