package com.my.mall.api.shortlink.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.entity.LinkAccessLogsDO;
import lombok.Data;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@Data
public class ShortLinkGroupAccessRecordReqDTO extends Page<LinkAccessLogsDO> {
    private String gid;
    private String startDate;
    private String endDate;
}
