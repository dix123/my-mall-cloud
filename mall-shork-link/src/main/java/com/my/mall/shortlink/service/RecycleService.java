package com.my.mall.shortlink.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.shortlink.dto.resp.ShortLinkPageResp;
import com.my.mall.shortlink.entity.LinkDO;

/**
 * @Author: Base
 * @Date: 2025/5/12
 **/
public interface RecycleService extends IService<LinkDO> {
    /**
     * 保存到回收站
     * @param saveRecycleReqDTO
     */
    void saveRecycle(SaveRecycleReqDTO saveRecycleReqDTO);

    /**
     * 分页获取回收站
     * @param param
     * @return
     */
    IPage<ShortLinkPageResp> pageShortLink(ShortLinkPageReqDTO param);

    /**
     * 恢复短连接
     * @param param
     */
    void recoverShortLink(ShortLinkRecoverReqDTO param);

    /**
     * 移除回收站短链
     * @param param
     */
    void removeRecycle(RecycleRemoveReqDTO param);
}
