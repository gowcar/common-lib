package com.jiaxintec.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;


/**
 * Class Name:  PropertiesUtils
 * Author:      Jacky Zhang
 * Create Time: 2020-03-19 下午11:02
 * Description:
 */
@Slf4j
public class CacheUtils
{
    private static File storeFile;
    private static Properties store;
    private final static String PROPERTIES_FILE = "store.properties";

    static {
        init();
    }

    private static void init() {
        String home = System.getProperty("user.home") + File.separator + ".rms";
        File repo = new File(home);
        if (repo.exists() && repo.isFile()) {
            repo.delete();
        }
        if (!repo.exists()) {
            repo.mkdirs();
        }

        storeFile = new File(repo, PROPERTIES_FILE);
        store = new Properties();
        try {
            store = new Properties();
            if (storeFile.exists()) {
                store.load(new FileInputStream(storeFile));
            }
        } catch (Exception e) {
            log.error("初始化Properties错误", e);
        }
    }

    public static void set(String k, String v) {
        store.setProperty(k, v);
    }

    public static String get(String k) {
        return store.getProperty(k);
    }

    public static void remove(String k) {
        store.remove(k);
    }

    public static void saveAndFlush() {
        try {
            String ts = DateFormatUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
            store.store(new FileOutputStream(storeFile), ts);
        } catch(Exception ex) {
            log.error("持久化错误", ex);
        }
    }
}
