package com.mnnu.examine.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.LogoEntity;
import com.mnnu.examine.modules.sys.service.LogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;





/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/logo")
public class LogoController {
    @Autowired
    private LogoService logoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = logoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		LogoEntity logo = logoService.getById(id);

        return R.ok().put("logo", logo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LogoEntity logo){
		logoService.save(logo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody LogoEntity logo){
		logoService.updateById(logo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		logoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
