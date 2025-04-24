package com.my.mall.mbg.service.impl;

import com.my.mall.mbg.model.PmsProductCategoryAttributeRelation;
import com.my.mall.mbg.dao.PmsProductCategoryAttributeRelationMapper;
import com.my.mall.mbg.service.IPmsProductCategoryAttributeRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品的分类和属性的关系表，用于设置分类筛选条件（只支持一级分类） 服务实现类
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Service
public class PmsProductCategoryAttributeRelationServiceImpl extends ServiceImpl<PmsProductCategoryAttributeRelationMapper, PmsProductCategoryAttributeRelation> implements IPmsProductCategoryAttributeRelationService {

}
