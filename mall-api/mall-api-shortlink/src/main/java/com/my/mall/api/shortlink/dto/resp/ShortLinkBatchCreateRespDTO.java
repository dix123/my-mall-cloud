package com.my.mall.api.shortlink.dto.resp;

import com.my.mall.api.shortlink.dto.req.ShortLinkBatchCreateInsideRespDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/23
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkBatchCreateRespDTO {
    private Integer total;
    private List<ShortLinkBatchCreateInsideRespDTO> infos;
}
