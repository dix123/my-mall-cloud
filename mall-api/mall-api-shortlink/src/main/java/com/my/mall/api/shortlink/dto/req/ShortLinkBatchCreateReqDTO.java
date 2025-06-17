package com.my.mall.api.shortlink.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/23
 **/
@Data
public class ShortLinkBatchCreateReqDTO {
    private List<String> originUrls;
    private List<String> describes;
    private String gid;
    /**
     * 0接口创建 1控制台创建
     */
    private Integer createdType;
    /**
     * 0永久有效 1自定义
     */
    private Integer validType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;
}
