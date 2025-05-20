package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.shortlink.entity.LinkAccessLogsDO;
import org.apache.calcite.adapter.java.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {
}
