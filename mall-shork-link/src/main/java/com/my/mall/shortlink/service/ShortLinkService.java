package com.my.mall.shortlink.service;

import cn.hutool.core.annotation.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.shortlink.entity.LinkDO;
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
     * @param fullShortUrl
     * @param request
     * @param response
     */
    void redirectUrl(String fullShortUrl, HttpServletRequest request, HttpServletResponse response);
}
