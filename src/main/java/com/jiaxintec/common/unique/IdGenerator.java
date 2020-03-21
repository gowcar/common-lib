package com.jiaxintec.common.unique;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * IDGenerator
 * Created By Jacky Zhang on 2018/4/17 下午3:36
 */
public class IdGenerator implements IdentifierGenerator
{
    public static long id() {
        return SnowflakeIdWorker.id();
    }

    public static String hexid() {
        return Long.toHexString(id());
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return id();
    }
}
