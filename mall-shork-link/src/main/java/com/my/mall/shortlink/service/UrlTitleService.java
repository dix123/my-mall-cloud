package com.my.mall.shortlink.service;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/

public interface UrlTitleService {
    /**
     * 根据网址获取标题
     * @param url
     * @return
     */
    String getTitle(String url);
}
