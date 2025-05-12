package com.my.mall.shortlink.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.shortlink.dto.resp.ShortLinkPageResp;
import com.my.mall.shortlink.service.RecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haole
 * @Date: 2025/5/12
 **/
@RestController
public class RecycleController {

    @Autowired
    private RecycleService recycleService;

    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public CommonResult<Void> saveRecycle(@RequestBody SaveRecycleReqDTO saveRecycleReqDTO) {
        recycleService.saveRecycle(saveRecycleReqDTO);
        return CommonResult.success();
    }

    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public CommonResult<IPage<ShortLinkPageResp>> pageRecycle(ShortLinkPageReqDTO param) {
        return CommonResult.success(recycleService.pageShortLink(param));
    }

    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public CommonResult<Void> recoverShortLink(@RequestBody ShortLinkRecoverReqDTO param) {
        recycleService.recoverShortLink(param);
        return CommonResult.success();
    }

    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    public CommonResult<Void> removeRecycle(@RequestBody RecycleRemoveReqDTO param) {
        recycleService.removeRecycle(param);
        return CommonResult.success();
    }

}
