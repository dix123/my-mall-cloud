package com.my.mall.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.shortlink.dto.req.RecycleRemoveReqDTO;
import com.my.mall.api.shortlink.dto.req.SaveRecycleReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkRecoverReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.api.shortlink.feign.RecycleBinFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.common.data.entity.LinkDO;
import com.my.mall.security.AuthUserContext;
import com.my.mall.shortlink.admin.entity.GroupDO;
import com.my.mall.shortlink.admin.mapper.GroupMapper;
import com.my.mall.shortlink.admin.mapper.RecycleBinMapper;
import com.my.mall.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<RecycleBinMapper, LinkDO> implements RecycleBinService {

    private final RecycleBinFeignClient recycleBinFeignClient;
    private final GroupMapper groupMapper;

    @Override
    public void saveRecycleBin(SaveRecycleReqDTO requestParam) {
        recycleBinFeignClient.saveRecycle(requestParam);
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, AuthUserContext.get().getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)) {
            throw new ApiException("用户无分组信息");
        }
        requestParam.setGids(groupDOList.stream().map(GroupDO::getGid).toList());
        CommonResult<Page<ShortLinkPageRespDTO>> pageCommonResult = recycleBinFeignClient.pageRecycle(requestParam.getGids(), requestParam.getCurrent(), requestParam.getSize());
        if (!pageCommonResult.isSuccess()) {
            throw new ApiException(pageCommonResult.getMsg());
        }
        return pageCommonResult.getData();
    }

    @Override
    public void recoverRecycleBin(ShortLinkRecoverReqDTO requestParam) {
        recycleBinFeignClient.recoverShortLink(requestParam);
    }

    @Override
    public void removeRecycleBin(RecycleRemoveReqDTO requestParam) {
        recycleBinFeignClient.removeRecycle(requestParam);
    }
}
