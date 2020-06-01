package com.jiaxintec.common.crud;

import com.jiaxintec.common.http.Response;
import com.jiaxintec.common.util.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.ParameterizedType;

/**
 * Class Name:  BaseController
 * Author:      Jacky Zhang
 * Create Time: 2020/6/1 下午4:30
 * Description:
 */
@Slf4j
@RestController
public abstract class BaseController<T, X>
{
    @Autowired
    private BaseDao<T,X> dao;

    @PutMapping("create")
    @Transactional(rollbackFor = Exception.class)
    public Response<X> create(@RequestBody T info) {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            T entity = entityClass.newInstance();
            BeanUtils.copyPropertiesIgnoreNull(info, entity);
            validate(entity);
            dao.saveAndFlush(entity);
        } catch (Exception e) {
            log.error("创建对象出错", e);
        }
        return Response.success("OK");
    }

    protected void validate(T entity) {

    }

}
