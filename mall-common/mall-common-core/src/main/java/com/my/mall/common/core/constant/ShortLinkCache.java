package com.my.mall.common.core.constant;

/**
 * @Author: Base
 * @Date: 2025/5/12
 **/
public interface ShortLinkCache {
    /**
     * 空值key过期时间
     */
    Integer NULL_KEY_OUT_TIME = 30;
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

    /**
     * 将数据加载进缓存的锁
     */
    String GO_TO_SHORT_LINK_LOCK = "mall:short-link:lock:goto:%s";
    /**
     * gid更新读写锁
     */
    String GID_UPDATE_LOCK_KEY = "mall:short-link:lock:gid-update:%s";

}
