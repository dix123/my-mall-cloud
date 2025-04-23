package com.my.mbg.model;

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
 * 后台角色资源关系表
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("ums_role_resource_relation")
public class UmsRoleResourceRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 资源ID
     */
    @TableField("resource_id")
    private Long resourceId;
}
