package io.renren.modules.stu.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.stu.entity.StuInfoEntity;
import io.renren.modules.stu.service.StuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@RestController
@RequestMapping("stu/stuinfo")
public class StuInfoController {
    @Autowired
    private StuInfoService stuInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = stuInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 获取到各个用户的积分信息
     * 传入用户id
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long stuId) {
        StuInfoEntity infoEntity = stuInfoService.getOne(new QueryWrapper<StuInfoEntity>()
                .eq("stu_id", stuId));

        return R.ok().put("stuPoint", infoEntity.getPoint());
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody StuInfoEntity stuInfo) {
        stuInfoService.save(stuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody StuInfoEntity stuInfo) {
        stuInfoService.updateById(stuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        stuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
