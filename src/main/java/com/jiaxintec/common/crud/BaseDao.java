package com.jiaxintec.common.crud;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Optional;

/**
 * Class Name:  BaseDao
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:31
 * Description:
 */
@Component
public class BaseDao<T extends BaseEntity<ID>, ID extends Serializable>
{
    @Resource
    private EntityManager em;
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private Class<T> domainType;

    public Optional<T> findById(ID id) {
        Optional<T> opt = Optional.ofNullable(em.find(domainType, id));
        return opt;
    }

    public T save(T t) {
        if (em.contains(t)) {
            t = em.merge(t);
        } else {
            em.persist(t);
        }
        return findOne(t.getId());
    }

    public T findOne(ID id) {
        return em.getReference(domainType, id);
    }

    public Class<T> domainType() {
        return domainType;
    }

    public void delete(T t) {
        em.remove(em.contains(t) ? t: em.merge(t));
    }


    public QueryResults<T> query(
            Expression[] selector,
            Predicate filter,
            OrderSpecifier order,
            int start,
            int limit) {
        String varible = WordUtils.uncapitalize(domainType.getSimpleName());

        EntityPathBase entityPathBase = new EntityPathBase(domainType, varible);

        JPAQuery<T> query;
        if (selector != null) {
            query = jpaQueryFactory.select(Projections.bean(domainType, selector));
        } else {
            query = jpaQueryFactory.select(entityPathBase);
        }
        query = query.from(entityPathBase);
        if (filter != null) {
            query = query.where(filter);
        }
        if (order != null) {
            query = query.orderBy(order);
        }

        QueryResults<T> results = query
                .offset(start)
                .limit(limit)
                .fetchResults();
        return results;
    }

    public void setDomainType(Class<T> domainType) {
        this.domainType = domainType;
    }
}
