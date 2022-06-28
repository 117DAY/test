package io.renren.modules.teacher.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.teacher.entity.TeacherInfoEntity;
import io.renren.modules.teacher.service.TeacherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@RestController
@RequestMapping("teacher/teacherinfo")
public class TeacherInfoController {
    @Autowired
    private TeacherInfoService teacherInfoService;


    /**
     * 查找出所有的教师
     * teacher的角色id
     * 临时教师(6)、正式教师(2)、全部教师(不传条件)      * RoleId
     * 按名字                                      * name
     * 以及其他的一些分页需要的信息
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = teacherInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/temp")
    public R tempTeacher(@RequestParam Map<String, Object> params) {

        PageUtils page = teacherInfoService.queryTempTeacherPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        TeacherInfoEntity teacherInfo = teacherInfoService.getById(id);

        return R.ok().put("teacherInfo", teacherInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TeacherInfoEntity teacherInfo) {
        teacherInfoService.save(teacherInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TeacherInfoEntity teacherInfo) {
        teacherInfoService.updateById(teacherInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        boolean b = teacherInfoService.deleteTeacher(ids);
        return R.ok();
    }


}
