package com.my.mall.mbg.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <p>
 * 商品限时购与商品关系表
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Getter
@Setter
@ToString
@TableName("sms_flash_promotion_product_relation")
public class SmsFlashPromotionProductRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("flash_promotion_id")
    private Long flashPromotionId;

    /**
     * 编号
     */
    @TableField("flash_promotion_session_id")
    private Long flashPromotionSessionId;

    @TableField("product_id")
    private Long productId;

    /**
     * 限时购价格
     */
    @TableField("flash_promotion_price")
    private BigDecimal flashPromotionPrice;

    /**
     * 限时购数量
     */
    @TableField("flash_promotion_count")
    private Integer flashPromotionCount;

    /**
     * 每人限购数量
     */
    @TableField("flash_promotion_limit")
    private Integer flashPromotionLimit;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;
}
