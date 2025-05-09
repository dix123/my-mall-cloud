package com.my.mall.api.shortlink.feign;

import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
@FeignClient("mall-shortlink")
public interface ShortLinkFeignClient {

    /**
     * 获取分组下的短连接数量
     * @param gid
     * @return
     */
    @GetMapping("/feign/shortLink/v1/list")
    CommonResult<List<GroupShortLinkCountDTO>> listGroupShortLinkCount(List<String> gid);
}
