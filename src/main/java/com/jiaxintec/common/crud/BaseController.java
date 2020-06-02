package com.jiaxintec.common.crud;

import com.jiaxintec.common.exception.Http500Exception;
import com.jiaxintec.common.exception.ServiceException;
import com.jiaxintec.common.http.Response;
import com.jiaxintec.common.util.BeanUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Class Name:  BaseController
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:30
 * Description:
 */
@Slf4j
@RestController
public abstract class BaseController<T extends BaseEntity, ID extends Serializable> {
    @Autowired
    private BaseDao<T, ID> dao;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Response create(@RequestBody T info) throws Exception {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            T entity = entityClass.newInstance();
            BeanUtils.copyPropertiesIgnoreNull(info, entity);
            validate(entity);
            dao.saveAndFlush(entity);
        } catch (Exception e) {
            log.error("创建对象出错", e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new Http500Exception(90000, "创建对象出现错误: " + e.getMessage());
        }
        return Response.success("OK");
    }

    @PostMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response update(@PathVariable ID id, @RequestBody T info) throws Throwable {
        T t = dao.findById(id).orElseThrow(() -> new Http500Exception(90002, "指定的实体不存在"));
        try {
            validate(info);
            BeanUtils.copyPropertiesIgnoreNull(info, t);
            dao.saveAndFlush(t);
        } catch (Exception e) {
            log.error("更新对象出错", e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new Http500Exception(90000, "更新出现错误: " + e.getMessage());
        }
        return Response.success("OK");
    }

    protected void validate(T entity) {
    }

    @DeleteMapping("/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Response delete(@PathVariable ID id) throws Throwable {
        T t = dao.findById(id).orElseThrow(() -> new Http500Exception(90002, "指定的实体不存在"));
        try {
            dao.delete(t);
        } catch (Exception e) {
            log.error("删除对象出错", e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new Http500Exception(90000, "删除出现错误: " + e.getMessage());
        }
        return Response.success("OK");
    }

    @GetMapping
    public Response findAll(
            Expression[] selector,
            Predicate filter,
            OrderSpecifier order,
            int start,
            int limit) throws Throwable {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String varible = WordUtils.uncapitalize(entityClass.getSimpleName());
        EntityPathBase entityPathBase = new EntityPathBase(entityClass, varible);
        EntityPathBase base = null;
        QueryResults<T> results = jpaQueryFactory
                .select(Projections.bean(entityClass, selector))
                .from(entityPathBase)
                .where(filter)
                .orderBy(order)
                .offset(start)
                .limit(limit)
                .fetchResults();
        return Response.success(results);
    }

    @GetMapping("/{id}")
    public Response findById(@PathVariable ID id) throws Throwable {
        T t = dao.findById(id).orElseThrow(() -> new Http500Exception(90002, "指定的实体不存在"));
        return Response.success(t);
    }
}
