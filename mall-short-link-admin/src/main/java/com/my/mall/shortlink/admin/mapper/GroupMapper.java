package com.my.mall.shortlink.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.shortlink.admin.dto.req.GroupSortUpdateReqDTO;
import com.my.mall.shortlink.admin.entity.GroupDO;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/6
 **/
@Mapper
public interface GroupMapper extends BaseMapper<GroupDO> {

    /**
     * 改变分组优先级
     * @param param
     */
    void groupSortUpdate(@Param("param") List<GroupSortUpdateReqDTO> param);

}
