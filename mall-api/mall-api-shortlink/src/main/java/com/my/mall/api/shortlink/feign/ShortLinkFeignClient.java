package com.my.mall.api.shortlink.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.ShortLinkBatchCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkUpdateReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkBatchCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@FeignClient(value = "short-link-project", contextId = "link")
public interface ShortLinkFeignClient {

    /**
     * 创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建响应
     */
    @PostMapping("/feign/insider/api/short-link/v1/create")
    CommonResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     *
     * @param requestParam 批量创建短链接请求参数
     * @return 短链接批量创建响应
     */
    @PostMapping("/feign/insider/api/short-link/v1/create/batch")
    CommonResult<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    /**
     * 修改短链接
     *
     * @param requestParam 修改短链接请求参数
     */
    @PostMapping("/feign/insider/api/short-link/v1/update")
    CommonResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param gid      分组标识
     * @param orderTag 排序类型
     * @param current  当前页
     * @param size     当前数据多少
     * @return 查询短链接响应
     */
    @GetMapping("/feign/insider/api/short-link/v1/page")
    CommonResult<Page<ShortLinkPageRespDTO>> pageShortLink(@RequestParam("gid") String gid,
                                                           @RequestParam("orderTag") String orderTag,
                                                           @RequestParam("current") Long current,
                                                           @RequestParam("size") Long size);

    @GetMapping("/feign/insider/api/short-link/v1/title")
    CommonResult<String> getTitleByUrl(@RequestParam("url") String url);
}
