package com.my.mall.shortlink.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.my.mall.common.data.model.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/5/6
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_group_unique")
@Builder
public class GroupUniqueDO{
    private Long id;
    private String gid;
}
