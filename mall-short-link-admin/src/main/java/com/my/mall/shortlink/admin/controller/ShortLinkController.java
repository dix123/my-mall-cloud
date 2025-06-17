package com.my.mall.shortlink.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.*;
import com.my.mall.api.shortlink.dto.resp.ShortLinkBatchCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.api.shortlink.feign.ShortLinkFeignClient;
import com.my.mall.common.core.api.CommonResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkFeignClient shortLinkFeignClient;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public CommonResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkFeignClient.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        CommonResult<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkFeignClient.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBatchCreateInsideRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getInfos();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("批量创建短链接-SaaS短链接系统", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), ShortLinkBatchCreateInsideRespDTO.class).sheet("Sheet").doWrite(baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public CommonResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkFeignClient.updateShortLink(requestParam);
        return CommonResult.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public CommonResult<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPagesReqDto requestParam) {
        return shortLinkFeignClient.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
