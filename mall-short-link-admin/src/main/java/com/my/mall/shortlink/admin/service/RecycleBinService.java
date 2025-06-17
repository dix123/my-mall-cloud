package com.my.mall.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.api.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.api.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.data.entity.LinkDO;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/

public interface RecycleBinService extends IService<LinkDO> {
    /**
     * 保存回收站
     *
     * @param requestParam 请求参数
     */
    void saveRecycleBin(SaveRecycleReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页查询短链接请求参数
     * @return 短链接分页返回结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * 从回收站恢复短链接
     *
     * @param requestParam 恢复短链接请求参数
     */
    void recoverRecycleBin(ShortLinkRecoverReqDTO requestParam);

    /**
     * 从回收站移除短链接
     *
     * @param requestParam 移除短链接请求参数
     */
    void removeRecycleBin(RecycleRemoveReqDTO requestParam);
}
