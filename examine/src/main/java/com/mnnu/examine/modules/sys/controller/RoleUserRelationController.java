package com.mnnu.examine.modules.sys.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@RestController
@RequestMapping("sys/roleuserrelation")
public class RoleUserRelationController {
    @Autowired
    private RoleUserRelationService roleUserRelationService;


    /**
     *
     *
     * @description: 获取直播间用户类型
     * @param userId
     * @return:
     * @author: Geralt
     * @time: 2021/11/26 16:59
     */
    @RequestMapping("/roleType")
    public R getRoleType(@RequestBody Map<String,Object> form){
        String userId=(String)form.get("userId") ;
        int role = roleUserRelationService.getUserRoleByUserId(userId);
        if(role==5)return R.ok().put("role",1);
        else return R.ok().put("role",2);

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = roleUserRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		RoleUserRelationEntity roleUserRelation = roleUserRelationService.getById(id);

        return R.ok().put("roleUserRelation", roleUserRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleUserRelationEntity roleUserRelation){
		roleUserRelationService.save(roleUserRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleUserRelationEntity roleUserRelation){
		roleUserRelationService.updateById(roleUserRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		roleUserRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
