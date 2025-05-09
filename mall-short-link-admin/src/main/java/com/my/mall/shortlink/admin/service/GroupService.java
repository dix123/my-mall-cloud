package com.my.mall.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.shortlink.admin.dto.req.GroupNameUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.req.GroupSortUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.GroupRespDTO;
import com.my.mall.shortlink.admin.entity.GroupDO;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/6
 **/
public interface GroupService extends IService<GroupDO> {
    /**
     * 新增分组
     * @param group
     * @return
     */
    void saveGroup(String group, String username);

    /**
     * 获取用户分组信息
     * @return
     */
    List<GroupRespDTO> listGroup();

    /**
     * 修改分组名称
     * @param groupNameUpdateReqDTO
     * @return
     */
    void updateGroupName(GroupNameUpdateReqDTO groupNameUpdateReqDTO);

    /**
     * 删除指定gid的组
     * @param gid
     */
    void deleteGroup(String gid);

    /**
     * 跟新组的排序
     * @param groupSortUpdateReqDTOList
     */
    void updateGroupSort(List<GroupSortUpdateReqDTO> groupSortUpdateReqDTOList);
}
