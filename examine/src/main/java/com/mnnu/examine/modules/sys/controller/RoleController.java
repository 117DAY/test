package com.mnnu.examine.modules.sys.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.RoleEntity;
import com.mnnu.examine.modules.sys.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RequestMapping("sys/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = roleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		RoleEntity role = roleService.getById(id);

        return R.ok().put("role", role);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RoleEntity role){
		roleService.save(role);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role){
		roleService.updateById(role);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		roleService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
