package com.my.mall.mbg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
/**
 * <p>
 * 后台用户和角色关系表
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("ums_admin_role_relation")
public class UmsAdminRoleRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("admin_id")
    private Long adminId;

    @TableField("role_id")
    private Long roleId;
}
