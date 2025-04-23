package com.my.mbg.service.impl;

import com.my.mbg.model.UmsAdminPermissionRelation;
import com.my.mbg.dao.UmsAdminPermissionRelationMapper;
import com.my.mbg.service.IUmsAdminPermissionRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台用户和权限关系表(除角色中定义的权限以外的加减权限) 服务实现类
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Service
public class UmsAdminPermissionRelationServiceImpl extends ServiceImpl<UmsAdminPermissionRelationMapper, UmsAdminPermissionRelation> implements IUmsAdminPermissionRelationService {

}
