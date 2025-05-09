package com.my.mall.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.feign.ShortLinkFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.security.AuthUserContext;
import com.my.mall.security.bo.UserInfoInTokenBO;
import com.my.mall.shortlink.admin.constant.RedisCacheConstant;
import com.my.mall.shortlink.admin.dto.req.GroupNameUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.req.GroupSortUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.GroupRespDTO;
import com.my.mall.shortlink.admin.entity.GroupDO;
import com.my.mall.shortlink.admin.entity.GroupUniqueDO;
import com.my.mall.shortlink.admin.mapper.GroupMapper;
import com.my.mall.shortlink.admin.mapper.GroupUniqueMapper;
import com.my.mall.shortlink.admin.service.GroupService;
import com.my.mall.shortlink.admin.tool.KeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.proto.Common;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: haole
 * @Date: 2025/5/6
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImp extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final RBloomFilter<String> gidRegisterBloomFilter;
    private final RedissonClient redissonClient;
    @Value("${short-link.group.max-num}")
    private final Integer groupMaxNum;
    private final GroupUniqueMapper groupUniqueMapper;

    private final ShortLinkFeignClient shortLinkFeignClient;
    private final GroupMapper groupMapper;


    @Override
    public void saveGroup(String groupName, String username) {

        RLock lock = redissonClient.getLock(String.format(RedisCacheConstant.CREATE_GROUP_LOCK, username));
        lock.lock();
        try {
            LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getUserName, username)
                    .eq(GroupDO::getDelFlag, 0);
            List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
            if (CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
                throw new ApiException(String.format("超出分组限制：%d", groupMaxNum));
            }
            int retry = 0;
            int maxRetry = 10;
            String gid = null;
            while (retry < maxRetry) {
                gid = saveGroupUniqueReturnGid();
                if (StrUtil.isNotBlank(gid)) {
                    GroupDO groupDo = GroupDO.builder()
                            .gid(gid)
                            .userName(username)
                            .name(groupName)
                            .sortOrder(0)
                            .build();
                    baseMapper.insert(groupDo);
                    gidRegisterBloomFilter.add(gid);
                }
                retry++;
            }
            if (StrUtil.isBlank(gid)) {
                throw new ApiException("生成分组表示频繁");
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<GroupRespDTO> listGroup() {
        UserInfoInTokenBO userInfoInToken = AuthUserContext.get();
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUserName, userInfoInToken.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(wrapper);
        CommonResult<List<GroupShortLinkCountDTO>> groupResult = shortLinkFeignClient.listGroupShortLinkCount(groupDOList.stream().map(GroupDO::getGid).collect(Collectors.toList()));
        if (!groupResult.isSuccess()) {
            throw new ApiException(groupResult.getMsg());
        }
        List<GroupRespDTO> groupRespDTOS = BeanUtil.copyToList(groupDOList, GroupRespDTO.class);
        groupRespDTOS.forEach(groupRespDTO -> {
            Optional<GroupShortLinkCountDTO> first = groupResult.getData().stream()
                    .filter(item -> groupRespDTO.getGid().equals(item.getGid()))
                    .findFirst();
            first.ifPresent(item -> groupRespDTO.setShortLinkCount(item.getCount()));
        });
        return groupRespDTOS;

    }

    @Override
    public void updateGroupName(GroupNameUpdateReqDTO groupNameUpdateReqDTO) {
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUserName, userInfoInTokenBO.getUsername())
                        .eq(GroupDO::getGid, groupNameUpdateReqDTO.getGid());
        GroupDO groupDO = GroupDO.builder()
                        .name(groupNameUpdateReqDTO.getName())
                                .build();
        baseMapper.update(groupDO, wrapper  );
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUserName, AuthUserContext.get().getUsername())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, wrapper);
    }

    @Override
    public void updateGroupSort(List<GroupSortUpdateReqDTO> groupSortUpdateReqDTOList) {
        groupMapper.groupSortUpdate(groupSortUpdateReqDTOList);

    }

    private String saveGroupUniqueReturnGid() {
        String gid = KeyGenerator.generate();
        if (gidRegisterBloomFilter.contains(gid)) {
            return null;
        }
        GroupUniqueDO groupUniqueDO = GroupUniqueDO.builder()
                .gid(gid)
                .build();
        try {
            groupUniqueMapper.insert(groupUniqueDO);
        } catch (DuplicateKeyException e) {
            return null;
        }
        return gid;
    }
}
