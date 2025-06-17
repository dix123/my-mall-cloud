package com.my.mall.shortlink.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.common.data.entity.LinkDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@Mapper
public interface RecycleBinMapper extends BaseMapper<LinkDO> {
}
