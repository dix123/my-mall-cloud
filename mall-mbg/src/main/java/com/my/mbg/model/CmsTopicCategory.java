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
 * 话题分类表
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("cms_topic_category")
public class CmsTopicCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name;

    /**
     * 分类图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 专题数量
     */
    @TableField("subject_count")
    private Integer subjectCount;

    @TableField("show_status")
    private Integer showStatus;

    @TableField("sort")
    private Integer sort;
}
