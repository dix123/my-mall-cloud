package com.my.mall.api.shortlink.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.resp.ShortLinkAccessRecordRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkStatsRespDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@FeignClient(value = "short-link-project", contextId = "stats")
public interface ShortLinkStatsFeignClient {

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param fullShortUrl 完整短链接
     * @param gid          分组标识
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @return 短链接监控信息
     */
    @GetMapping("/feign/insider/api/short-link/v1/stats")
    CommonResult<ShortLinkStatsRespDTO> oneShortLinkStats(@RequestParam("fullShortUrl") String fullShortUrl,
                                                          @RequestParam("gid") String gid,
                                                          @RequestParam("enableStatus") Integer enableStatus,
                                                          @RequestParam("startDate") String startDate,
                                                          @RequestParam("endDate") String endDate);

    /**
     * 访问分组短链接指定时间内监控数据
     *
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 分组短链接监控信息
     */
    @GetMapping("/feign/insider/api/short-link/v1/stats/group")
    CommonResult<ShortLinkStatsRespDTO> groupShortLinkStats(@RequestParam("gid") String gid,
                                                      @RequestParam("startDate") String startDate,
                                                      @RequestParam("endDate") String endDate);

    /**
     * 访问单个短链接指定时间内监控访问记录数据
     *
     * @param fullShortUrl 完整短链接
     * @param gid          分组标识
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param current      当前页
     * @param size         一页数据量
     * @return 短链接监控访问记录信息
     */
    @GetMapping("/feign/insider/api/short-link/v1/stats/access-record")
    CommonResult<Page<ShortLinkAccessRecordRespDTO>> shortLinkStatsAccessRecord(@RequestParam("fullShortUrl") String fullShortUrl,
                                                                                @RequestParam("gid") String gid,
                                                                                @RequestParam("startDate") String startDate,
                                                                                @RequestParam("endDate") String endDate,
                                                                                @RequestParam("enableStatus") Integer enableStatus,
                                                                                @RequestParam("current") Long current,
                                                                                @RequestParam("size") Long size);

    /**
     * 访问分组短链接指定时间内监控访问记录数据
     *
     * @param gid       分组标识
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param current   当前页
     * @param size      一页数据量
     * @return 分组短链接监控访问记录信息
     */
    @GetMapping("/feign/insider/api/short-link/v1/stats/access-record/group")
    CommonResult<Page<ShortLinkAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(@RequestParam("gid") String gid,
                                                                                    @RequestParam("startDate") String startDate,
                                                                                    @RequestParam("endDate") String endDate,
                                                                                    @RequestParam("current") Long current,
                                                                                    @RequestParam("size") Long size);
}
