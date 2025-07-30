package com.my.mall.security.adapter;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/4/30
 **/
public interface AuthConfigAdapter {


    /**
     * 包含这个就放行
     * @return
     */
    String unCheck();

    String unCheckIp();

    /**
     * 匹配的路径
     * @return
     */
    List<String> pathPatterns();

    /**
     * 不需要验证的路径
     * @param path
     * @return
     */
    List<String> excludePathPatterns(String... path);


}
