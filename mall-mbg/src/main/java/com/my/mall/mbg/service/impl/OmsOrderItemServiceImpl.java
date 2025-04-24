package com.my.mall.mbg.service.impl;

import com.my.mall.mbg.model.OmsOrderItem;
import com.my.mall.mbg.dao.OmsOrderItemMapper;
import com.my.mall.mbg.service.IOmsOrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author haole
 * @since 2025-04-23
 */
@Service
public class OmsOrderItemServiceImpl extends ServiceImpl<OmsOrderItemMapper, OmsOrderItem> implements IOmsOrderItemService {

}
