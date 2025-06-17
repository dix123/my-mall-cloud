package com.my.mall.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.auth.mapper.UserMapper;
import com.my.mall.auth.service.UserService;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.auth.model.UserDO;
import org.springframework.stereotype.Service;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private static final String USER_NOT_FOUND_SECRET = "USER_NOT_FOUND_SECRET";
    private static String userNotFoundEncodedPassword;

    @Override
    public UserInfoInTokenBO getUserInfoInTokenByUserNameAndPassword(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ApiException("用户名或密码不能为空", ErrorCode.CLIENT_ERROR);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            if (userNotFoundEncodedPassword == null) {
                userNotFoundEncodedPassword = BCrypt.hashpw(USER_NOT_FOUND_SECRET);
            }
            BCrypt.checkpw(password, userNotFoundEncodedPassword);
            throw new ApiException("用户名或密码不正确");
        }
        if (!BCrypt.checkpw(password, userDO.getPassword())) {
            throw new ApiException("用户名或密码不正确");
        }
        return BeanUtil.toBean(userDO, UserInfoInTokenBO.class);

    }
}
