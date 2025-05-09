package com.my.mall.shortlink.service.Impl;

import cn.hutool.core.annotation.Link;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.shortlink.entity.LinkDO;
import com.my.mall.shortlink.mapper.ShortLinkMapper;
import com.my.mall.shortlink.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/7
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, LinkDO> implements ShortLinkService {

    ShortLinkMapper shortLinkMapper;

    @Override
    public List<GroupShortLinkCountDTO> listGroupShortLinkCount(List<String> gids) {
         return shortLinkMapper.listGroupShortLinkCount(gids);
    }
}
