package com.my.mall.shortlink.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.common.core.constant.ShortLinkCache;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageResp;
import com.my.mall.shortlink.constant.HttpConstant;
import com.my.mall.api.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.api.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.common.data.entity.LinkDO;
import com.my.mall.shortlink.mapper.RecycleMapper;
import com.my.mall.shortlink.service.RecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: haole
 * @Date: 2025/5/12
 **/
@Service
public class RecycleServiceImpl extends ServiceImpl<RecycleMapper, LinkDO> implements RecycleService {

    @Autowired
    private RecycleMapper recycleMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycle(SaveRecycleReqDTO saveRecycleReqDTO) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getFullShortUrl, saveRecycleReqDTO.getFullShortUrl())
                .eq(LinkDO::getGid, saveRecycleReqDTO.getGid())
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = LinkDO.builder()
                .enableStatus(0)
                .build();
        baseMapper.update(linkDO, wrapper);
        stringRedisTemplate.delete(String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, saveRecycleReqDTO.getFullShortUrl()));
    }

    @Override
    public IPage<ShortLinkPageResp> pageShortLink(ShortLinkPageReqDTO param) {
        IPage<LinkDO> linkDOIPage = baseMapper.pageRecycle(param);
        return linkDOIPage.convert(each -> {
            ShortLinkPageResp resp = BeanUtil.toBean(each, ShortLinkPageResp.class);
            resp.setDomain(HttpConstant.PREFIX + resp.getDomain());
            return resp;
        });
    }

    @Override
    public void recoverShortLink(ShortLinkRecoverReqDTO param) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getFullShortUrl, param.getFullShortUrl())
                .eq(LinkDO::getGid, param.getGid())
                .eq(LinkDO::getDelFlag, 0)
                .eq(LinkDO::getEnableStatus, 0);
        LinkDO build = LinkDO.builder()
                .enableStatus(1)
                .build();
        baseMapper.update(build, wrapper);
        stringRedisTemplate.opsForSet().remove(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, param.getFullShortUrl());
    }

    @Override
    public void removeRecycle(RecycleRemoveReqDTO param) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getFullShortUrl, param.getFullShortUrl())
                .eq(LinkDO::getGid, param.getGid())
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = LinkDO.builder()
                .delTime(System.currentTimeMillis())
                .build();
        linkDO.setDelFlag(1);
        baseMapper.update(linkDO, wrapper);
    }
}
