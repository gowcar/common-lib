package com.jiaxintec.common.http;

import lombok.Data;

import java.util.List;

/**
 * Class Name:  Page
 * Author:      Jacky Zhang
 * Create Time: 2019/6/17 下午5:26
 * Description:
 */
@Data
public class Page <T>{
    private List<T> content;
    private long totalElements;
    private Integer totalPages;
    private Integer start;
    private Integer limit;
}
