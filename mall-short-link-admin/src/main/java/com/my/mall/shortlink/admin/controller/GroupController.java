package com.my.mall.shortlink.admin.controller;

import com.my.mall.common.core.api.CommonResult;
import com.my.mall.security.AuthUserContext;
import com.my.mall.shortlink.admin.dto.req.GroupNameUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.req.GroupSaveReqDTO;
import com.my.mall.shortlink.admin.dto.req.GroupSortUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.GroupRespDTO;
import com.my.mall.shortlink.admin.service.GroupService;
import org.apache.calcite.avatica.proto.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/8
 **/
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/api/short-link/admin/v1/group")
    public CommonResult<Void> saveGroup(@RequestBody GroupSaveReqDTO groupSaveReq) {
        groupService.saveGroup(groupSaveReq.getName(), AuthUserContext.get().getUsername());
        return CommonResult.success();
    }

    @GetMapping("/api/short-link/admin/group")
    public CommonResult<List<GroupRespDTO>> listGroup() {
        return CommonResult.success(groupService.listGroup());
    }

    @PutMapping("/api/short-link/v1/group")
    public CommonResult<Void> updateGroupName(@RequestBody GroupNameUpdateReqDTO reqDTO) {
        groupService.updateGroupName(reqDTO);
        return CommonResult.success();
    }

    @DeleteMapping("/api/short-link/v1/group")
    public CommonResult<Void> deleteGroup(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return CommonResult.success();
    }

    @PostMapping("/api/short-link/v1/group")
    public CommonResult<Void> groupSortUpdate(@RequestBody List<GroupSortUpdateReqDTO> groupSortUpdateReqDTOList) {
        groupService.updateGroupSort(groupSortUpdateReqDTOList);
        return CommonResult.success();
    }
}
