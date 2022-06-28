package io.renren.modules.sys.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.QQServiceEntity;
import io.renren.modules.sys.service.QQServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/service")
@CacheEvict(cacheNames = "qqService", allEntries = true)
public class QQServiceController {
    @Autowired
    private QQServiceService serviceService;

    /**
     * 传入手机号、姓名、qq号
     * phone
     * name
     * qqNum
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = serviceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        QQServiceEntity service = serviceService.getById(id);

        return R.ok().put("service", service);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QQServiceEntity service) {
        serviceService.save(service);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QQServiceEntity service) {
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
