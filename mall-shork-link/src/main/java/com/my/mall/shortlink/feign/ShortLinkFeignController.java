package com.my.mall.shortlink.feign;

import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.feign.ShortLinkFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.service.ShortLinkService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/7
 **/
@RestController
public class ShortLinkFeignController implements ShortLinkFeignClient {
    @Autowired
    private ShortLinkService shortLinkService;
    @Override
    public CommonResult<List<GroupShortLinkCountDTO>> listGroupShortLinkCount(List<String> gid) {
        return CommonResult.success(shortLinkService.listGroupShortLinkCount(gid));
    }
}
