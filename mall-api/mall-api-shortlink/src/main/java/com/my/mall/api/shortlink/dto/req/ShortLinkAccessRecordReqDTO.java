package com.my.mall.api.shortlink.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.entity.LinkAccessLogsDO;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@Data
public class ShortLinkAccessRecordReqDTO extends Page<LinkAccessLogsDO> {
    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableStatus;
}
