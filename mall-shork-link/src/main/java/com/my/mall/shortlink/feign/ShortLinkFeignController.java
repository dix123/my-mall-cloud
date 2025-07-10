package com.my.mall.shortlink.feign;

import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.feign.ShortLinkGroupFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/7
 **/
@RestController
public class ShortLinkFeignController{
    @Autowired
    private ShortLinkService shortLinkService;

    @GetMapping("/feign/insider/shortLink/v1/list")
    public CommonResult<List<GroupShortLinkCountDTO>> listGroupShortLinkCount(@RequestParam("gid") List<String> gid) {
        return CommonResult.success(shortLinkService.listGroupShortLinkCount(gid));
    }


}
