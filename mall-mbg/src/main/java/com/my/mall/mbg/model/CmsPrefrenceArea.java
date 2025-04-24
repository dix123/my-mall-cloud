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
 * 优选专区
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("cms_prefrence_area")
public class CmsPrefrenceArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    @TableField("sub_title")
    private String subTitle;

    /**
     * 展示图片
     */
    @TableField("pic")
    private byte[] pic;

    @TableField("sort")
    private Integer sort;

    @TableField("show_status")
    private Integer showStatus;
}
