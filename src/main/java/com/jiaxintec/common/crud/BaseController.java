package com.jiaxintec.common.crud;

import com.jiaxintec.common.exception.Http500Exception;
import com.jiaxintec.common.exception.ServiceException;
import com.jiaxintec.common.http.Response;
import com.jiaxintec.common.jwt.PassThrough;
import com.jiaxintec.common.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * Class Name:  BaseController
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:30
 * Description:
 */
@Slf4j
@RestController
@PassThrough
public abstract class BaseController<T extends BaseEntity<ID>, ID extends Serializable> {

    @Autowired
    protected BaseDao<T, ID> dao;

    @PostConstruct
    private void init() {
        dao.setDomainType(domainType());
    }

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Response create(@RequestBody T info) throws Exception {
        try {
            T entity = domainType().newInstance();
            BeanUtils.copyPropertiesIgnoreNull(info, entity);
            validate(entity);
            dao.save(entity);
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
        Optional<T> opt = dao.findById(id);
        log.debug("Optional is {}", opt.isPresent());
        T t = dao.findById(id).orElseThrow(() -> new Http500Exception(90002, "指定的实体不存在"));
        try {
            validate(info);
            BeanUtils.copyPropertiesIgnoreNull(info, t);
            dao.save(t);
        } catch (Exception e) {
            log.error("更新对象出错", e);
            if (e instanceof ServiceException) {
                throw e;
            }
            throw new Http500Exception(90000, "更新出现错误: " + e.getMessage());
        }
        return Response.success("OK");
    }

    protected Class<T> domainType() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass;
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

    @GetMapping("/{id}")
    public Response findOne(@PathVariable ID id) throws Throwable {
        T t = dao.findById(id).orElseThrow(() -> new Http500Exception(90002, "指定的实体不存在"));
        return Response.success(t);
    }

    @GetMapping
    public Response findAll(
            @RequestParam(required = false, defaultValue = "0") int start,
            @RequestParam(required = false, defaultValue = "20") int limit
            ) {
        return Response.success(dao.query(null, null, null, start, limit));
    }

}
