package com.mall.common.cache.adapter;

import com.mall.common.cache.bo.CacheNameWithTtlBO;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/4/28
 **/
public interface CacheTtlAdapter {
    /**
     * 返回设置的名称和过期时间
     * @return
     */
    List<CacheNameWithTtlBO> listCacheNameWithTtl();
}
