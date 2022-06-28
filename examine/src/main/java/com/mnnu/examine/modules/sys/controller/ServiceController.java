package com.mnnu.examine.modules.sys.controller;


import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.ServiceEntity;
import com.mnnu.examine.modules.sys.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/service")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @Cacheable(cacheNames = "qqService",key = "'qqService'")
    public R list(@RequestParam Map<String, Object> params) {
        List<ServiceEntity> qqServices = serviceService.list();
        List<ServiceEntity> collect = qqServices.stream().filter(q -> q.getStatus() == 1).collect(Collectors.toList());

        return R.ok().put("list", collect);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        ServiceEntity service = serviceService.getById(id);

        return R.ok().put("service", service);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ServiceEntity service) {
        serviceService.save(service);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ServiceEntity service) {
        serviceService.updateById(service);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        serviceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
