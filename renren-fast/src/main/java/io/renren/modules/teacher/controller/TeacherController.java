package io.renren.modules.teacher.controller;

import io.renren.common.utils.R;
import io.renren.modules.teacher.service.TeacherInfoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author qiaoh
 */
@RestController
@RequestMapping("/teacher/teacher")
public class TeacherController {

    @Resource
    TeacherInfoService teacherInfoService;

    /**s
     * 将临时教师设置为正式教师
     *
     * @param ids
     * @return
     */
    @RequestMapping("/formal")
    public R toFormalTeacher(@RequestBody Long[] ids) {
        teacherInfoService.toFormal(Objects.requireNonNull(ids));
        return R.ok();
    }

    /**
     * 给正式教师授予直播的权限
     *
     * @return
     */
    @RequestMapping("/grant/live")
    public R grantLiveRight(@RequestBody Long[] ids) {
        // todo 未测试
        teacherInfoService.grantLive(Objects.requireNonNull(ids));
        return R.ok();
    }

    @RequestMapping("/cancel/grant/live")
    public R cancelGrantLive(@RequestBody Long[] ids){
        teacherInfoService.cancelGrantLive(ids);
        return R.ok();
    }

    /**
     * 拒绝成为老师
     *
     * @param teacherIds 老师id
     * @return {@link R}
     */
    @RequestMapping("/refuse")
    public R refuseBecomeTeacher(@RequestBody Long[] teacherIds) {

        teacherInfoService.refuseBecomeTeacher(teacherIds);
        return R.ok();
    }
}
