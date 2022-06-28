package com.mnnu.examine.modules.teacher.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.teacher.entity.TeacherInfoEntity;
import com.mnnu.examine.modules.teacher.service.TeacherInfoService;
import com.mnnu.examine.modules.teacher.vo.TeacherInfoRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

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
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = teacherInfoService.queryPage(params);

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
        teacherInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 成为老师
     *
     * @return {@link R}
     */
    @PostMapping("/become/teacher")
    @PreAuthorize("hasRole('student')")
    public R becomeTeacher(@Validated @RequestBody TeacherInfoRegisterVO teacher) {
 
        teacherInfoService.saveRegisterInfo(teacher);

        return R.ok();
    }
    @RequestMapping("/get/temteacherinfo")
    public R getTemTeacherInfo(){
        TeacherInfoEntity temTeacher = teacherInfoService.getTemTeacher();
//        System.out.println(temTeacher);
        return R.ok().put("teacherInfo",temTeacher);
    }


}
