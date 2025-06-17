package com.my.mall.api.shortlink.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
@Data
public class ShortLinkStatsReqDTO {
    private String gid;
    private String fullShortUrl;
    private String startDate;
    private String endDate;
    private Integer enableStatus;
}
