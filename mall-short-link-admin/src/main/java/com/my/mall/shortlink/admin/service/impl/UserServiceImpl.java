package com.my.mall.shortlink.admin.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.auth.feign.TokenFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.security.AuthUserContext;
import com.my.mall.shortlink.admin.constant.RedisCacheConstant;
import com.my.mall.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.my.mall.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.shortlink.admin.entity.UserDO;
import com.my.mall.shortlink.admin.mapper.GroupMapper;
import com.my.mall.shortlink.admin.mapper.UserMapper;
import com.my.mall.shortlink.admin.service.GroupService;
import com.my.mall.shortlink.admin.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupService groupService;

    @Qualifier("userRegisterBloomFilter")
    @Autowired
    private RBloomFilter<String> userRegisterBloomFilter;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private TokenFeignClient tokenFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterReqDTO userRegisterReqDTO) {
        String username = userRegisterReqDTO.getUsername();
        if (hasUsername(username)) {
            throw new ApiException(ErrorCode.USER_NAME_EXIST_ERROR);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.USER_REGISTER_LOCK + username);
        try {
            //这样的话如果创建出现异常那不就没创建了
//            if (!lock.tryLock()) {
//                throw new ApiException(ErrorCode.USER_NAME_EXIST_ERROR);
//            }
            lock.lock();
            if (hasUsername(username)) {
                throw new ApiException(ErrorCode.USER_NAME_EXIST_ERROR);
            }
            baseMapper.insert(BeanUtil.toBean(userRegisterReqDTO, UserDO.class));
            groupService.saveGroup("默认分组", username);
            userRegisterBloomFilter.add(username);
        } finally {
            lock.unlock();
        }

    }

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getDelFlag, 0)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(wrapper);
        if (userDO == null) {
            throw new ApiException("用户不存在");
        }
        return BeanUtil.toBean(userDO, UserRespDTO.class);
    }

    @Override
    public void UpdateUser(UserUpdateReqDTO reqDTO) {
        if (!reqDTO.getUsername().equals(AuthUserContext.get().getUsername())) {
            throw new ApiException("你只能更新你的用户信息！");
        }
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, reqDTO.getUsername()   );
        baseMapper.update(BeanUtil.toBean(reqDTO, UserDO.class), wrapper);
    }

    @Override
    public Boolean checkLogin() {
        return (StrUtil.isBlank(AuthUserContext.get().getUsername()));
    }

    @Override
    public void logout() {
        CommonResult<Void> logoutResult = tokenFeignClient.logout(AuthUserContext.get().getId());
        if (!logoutResult.isSuccess()) {
            throw new ApiException(logoutResult.getMsg());
        }
        AuthUserContext.clean();
    }

    private boolean hasUsername(String username) {
        return userRegisterBloomFilter.contains(username);
    }
}
