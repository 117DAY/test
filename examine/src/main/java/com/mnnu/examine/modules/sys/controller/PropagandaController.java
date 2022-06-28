package com.mnnu.examine.modules.sys.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.PropagandaEntity;
import com.mnnu.examine.modules.sys.service.PropagandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/propaganda")
@CacheConfig(cacheNames = "propaganda")
public class PropagandaController {
    @Autowired
    private PropagandaService propagandaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = propagandaService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有要展示在前台的轮播图
     *
     * @return
     */
    @RequestMapping("/all")
    @Cacheable(key = "#root.methodName")
    public R allPropaganda() {
        List<PropagandaEntity> list = propagandaService.list();

        List<PropagandaEntity> collect = list.stream().filter(p -> {
            return p.getIsShow() == 1;
        }).sorted(Comparator.comparingInt(PropagandaEntity::getSort)).collect(Collectors.toList());
        return R.ok().put("allPropaganda", collect);
    }

    /*
     *//**
     * 信息
     *//*
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        PropagandaEntity propaganda = propagandaService.getById(id);

        return R.ok().put("propaganda", propaganda);
    }*/
    /*
     *//**
     * 保存
     *//*
    @RequestMapping("/save")
    public R save(@RequestBody PropagandaEntity propaganda) {
        propagandaService.save(propaganda);

        return R.ok();
    }*/
    /*
     *//**
     * 修改
     *//*
    @RequestMapping("/update")
    public R update(@RequestBody PropagandaEntity propaganda) {
        propagandaService.updateById(propaganda);

        return R.ok();
    }*/


    /*
     *//**
     * 删除
     *//*
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        propagandaService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }*/

}
