package com.my.mall.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.api.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    private final RecycleBinService recycleBinService;

    /**
     * 保存回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public CommonResult<Void> saveRecycleBin(@RequestBody SaveRecycleReqDTO requestParam) {
        recycleBinService.saveRecycleBin(requestParam);
        return CommonResult.success();
    }

    /**
     * 分页查询回收站短链接
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public CommonResult<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return CommonResult.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 恢复短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public CommonResult<Void> recoverRecycleBin(@RequestBody ShortLinkRecoverReqDTO requestParam) {
        recycleBinService.recoverRecycleBin(requestParam);
        return CommonResult.success();
    }

    /**
     * 移除短链接
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public CommonResult<Void> removeRecycleBin(@RequestBody RecycleRemoveReqDTO requestParam) {
        recycleBinService.removeRecycleBin(requestParam);
        return CommonResult.success();
    }
}
