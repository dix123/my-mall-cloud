package com.my.mall.common.cache.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Data
@AllArgsConstructor
public class CacheNameWithTtlBO {
        private String cacheName;
        private Integer ttl;
}
