package com.my.mall.api.shortlink.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.api.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@FeignClient(value = "short-link-project", contextId = "recycleBin")
public interface RecycleBinFeignClient {

    @PostMapping("/feign/insider/api/short-link/v1/recycle-bin/save")
    public CommonResult<Void> saveRecycle(@RequestBody SaveRecycleReqDTO saveRecycleReqDTO);

    @GetMapping("/feign/insider/api/short-link/v1/recycle-bin/page")
    public CommonResult<Page<ShortLinkPageRespDTO>> pageRecycle(@RequestParam("gids") List<String> gids, @RequestParam("current") Long current, @RequestParam("size") Long size);

    @PostMapping("/feign/insider/api/short-link/v1/recycle-bin/recover")
    public CommonResult<Void> recoverShortLink(@RequestBody ShortLinkRecoverReqDTO param);

    @PostMapping("/feign/insider/api/short-link/v1/recycle-bin/remove")
    public CommonResult<Void> removeRecycle(@RequestBody RecycleRemoveReqDTO param);
}
