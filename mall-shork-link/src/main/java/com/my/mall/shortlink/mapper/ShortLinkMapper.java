package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPagesReqDto;
import com.my.mall.common.data.entity.LinkDO;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
@Mapper
public interface ShortLinkMapper extends BaseMapper<LinkDO> {
    /**
     * gid列表对应的连接数
     *
     * @return
     */
    List<GroupShortLinkCountDTO> listGroupShortLinkCount(@Param("gids") List<String> gids);

    /**
     * 短连接自增
     * @param gid
     * @param fullShortUrl
     * @param totalPv
     * @param totalUv
     * @param totalUip
     */
    void incrementStats(@Param("gid") String gid,
                        @Param("fullShortUrl") String fullShortUrl,
                        @Param("totalPv") Integer totalPv,
                        @Param("totalUv") Integer totalUv,
                        @Param("totalUip") Integer totalUip);

    /**
     * 分页查询短链接
     * @param param
     * @return
     */
    IPage<LinkDO> pageShortLink(ShortLinkPagesReqDto param);
}
