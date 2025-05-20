package com.mall.common.cache.constant;

/**
 * @Author: Base
 * @Date: 2025/5/12
 **/
public interface ShortLinkCache {
    /**
     * 短连接到长连接的映射
     */
    String SHORT_LINK_GOTO_KEY = "mall:short-link:cache:goto:%s";
    /**
     * 空链接
     */
    String SHORT_LINK_IS_NULL_KEY = "mall:short-link:cache:is-null:%s";
    /**
     * uv统计key
     */
    String SHORT_LINK_UV_STATUS_KEY = "short-link:uv:status:key:%s";
    String SHORT_LINK_UIP_STATUS_KEY = "short-link:uip:status:key:%s";
}
