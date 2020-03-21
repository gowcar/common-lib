package com.jiaxintec.common.unique;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * SnowflakeIdWorker
 * Created By Jacky Zhang on 2018/4/17 下午3:41
 */
public class SnowflakeIdWorker {

    private final AtomicLong twepoch = new AtomicLong(1420041600000L);
    private final AtomicLong ts = new AtomicLong();
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long sequenceBits = 12L;
    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    private long workerId;
    private long datacenterId;
    private AtomicLong sequence = new AtomicLong(0L);
    private AtomicLong lastTimestamp = new AtomicLong(-1L);

    private static volatile AtomicReference<SnowflakeIdWorker[]> instance = new AtomicReference<>();
    private static int count = 12;
    private static AtomicInteger currentIndex = new AtomicInteger(0);

    static {
        SnowflakeIdWorker[] workers = new SnowflakeIdWorker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = new SnowflakeIdWorker(i, 0);
        }
        instance.getAndSet(workers);
    }

    static long id() {
        int ind = currentIndex.incrementAndGet();
        SnowflakeIdWorker worker = null;
        if (ind < instance.get().length) {
            worker = instance.get()[ind];
        } else {
            currentIndex.getAndSet(0);
            worker = instance.get()[0];
        }
        return worker.nextId();
    }

    private SnowflakeIdWorker(long workerId, long datacenterId) {
        if ((workerId > maxWorkerId || workerId < 0) || (datacenterId > maxDatacenterId || datacenterId < 0)) {
            throw new IllegalArgumentException("worker id or data-center id is invalid!");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        ts.getAndSet(System.currentTimeMillis());

        if (ts.get() < lastTimestamp.get()) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id !");
        }

        if (lastTimestamp.get() == ts.get()) {
            sequence.getAndIncrement();
            sequence.getAndSet(sequence.get() & sequenceMask);
            if (sequence.get() == 0) {
                ts.getAndSet(System.currentTimeMillis());
                while (ts.get() <= lastTimestamp.get()) {
                    ts.getAndSet(System.currentTimeMillis());
                }
            }
        } else {
            sequence.getAndSet(0L);
        }

        lastTimestamp.getAndSet(ts.get());

        return ((ts.get() - twepoch.get()) << timestampLeftShift)
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence.get();
    }

    public static void main(String[] args) {
        long current = System.currentTimeMillis();
        Set<String> ids = new HashSet<>();
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        for (; ; ) {
            long id = idWorker.nextId();
            String sid = String.valueOf(id);
            if (ids.contains(sid)) {
                System.out.println("duplucation id:\t" + sid);
                System.exit(-1);
            }
            ids.add(String.valueOf(id));
            if (ids.size() % 1000000 == 0) {
                System.out.println(id);
                long now = System.currentTimeMillis();
                System.out.println("generate " + ids.size() + " id, used : \t" + (now - current) / 1000);
            }
        }
    }
}
