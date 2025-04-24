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
 * 首页推荐品牌表
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("sms_home_brand")
public class SmsHomeBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("brand_id")
    private Long brandId;

    @TableField("brand_name")
    private String brandName;

    @TableField("recommend_status")
    private Integer recommendStatus;

    @TableField("sort")
    private Integer sort;
}
