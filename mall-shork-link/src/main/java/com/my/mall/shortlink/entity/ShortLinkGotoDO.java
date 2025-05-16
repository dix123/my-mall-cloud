package com.my.mall.shortlink.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/5/14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_goto")
@Builder
public class ShortLinkGotoDO {
    private String gid;
    private String fullShortUrl;
}
