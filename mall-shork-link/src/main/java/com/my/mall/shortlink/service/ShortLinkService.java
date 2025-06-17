package com.my.mall.shortlink.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkBatchCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPagesReqDto;
import com.my.mall.api.shortlink.dto.req.ShortLinkUpdateReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkBatchCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkGroupLinkCountQueryRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.data.entity.LinkDO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
public interface ShortLinkService extends IService<LinkDO> {

    /**
     * 获取分组下短连接数量
     * @param gids
     * @return
     */
    List<GroupShortLinkCountDTO> listGroupShortLinkCount(List<String> gids);

    /**
     * 创建短连接
     * @param param
     * @return
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO param);

    /**
     * 通过短连接转到目标网址
     * @param fullShortUrl 完整短链接
     * @param request 请求
     * @param response 响应
     */
    void redirectUrl(String fullShortUrl, HttpServletRequest request, HttpServletResponse response);

    /**
     * 批量创建短链接
     * @param param 链接信息
     * @return 短链接信息
     */
    ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO param);

    /**
     * 更新短链接
     * @param param
     */
    void updateShortLink(ShortLinkUpdateReqDTO param);

    /**
     * 分页查询短链接
     * @param param
     * @return
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPagesReqDto param);

    /**
     * 查询分组下的短链接数量
     * @param gidList
     * @return
     */
    List<ShortLinkGroupLinkCountQueryRespDTO> countShortLink(List<String> gidList);


}
