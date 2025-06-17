package com.my.mall.api.shortlink.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/26
 **/
@Data
public class ShortLinkUpdateReqDTO {
    private String originUrl;
    private String fullShortUrl;
    private String originGid;
    private String gid;
    private Integer validDateType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validDate;
    private String describe;
}
