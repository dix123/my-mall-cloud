package com.my.mall.api.shortlink.feign;

import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
@FeignClient(value = "short-link-project", contextId = "group")
public interface ShortLinkGroupFeignClient {

    /**
     * 获取分组下的短连接数量
     * @param gid
     * @return
     */
    @GetMapping("/feign/insider/shortLink/v1/list")
    CommonResult<List<GroupShortLinkCountDTO>> listGroupShortLinkCount(@RequestParam("gid") List<String> gid);


}
