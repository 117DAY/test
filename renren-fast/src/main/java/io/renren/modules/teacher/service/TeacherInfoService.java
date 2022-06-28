package io.renren.modules.teacher.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.teacher.entity.TeacherInfoEntity;

import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface TeacherInfoService extends IService<TeacherInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 传入指定用户id，调整为正式教师，前提是当前传入的id是临时教师
     *
     * @param ids
     */
    void toFormal(Long[] ids);

    /**
     * 授予老师直播的权限，前提是正式的教师
     *
     * @param ids
     */
    void grantLive(Long[] ids);

    void cancelGrantLive(Long[] ids);
    /**
     * 拒绝成为老师,将角色变为学生
     *
     * @param teacherIds 老师id
     */
    void refuseBecomeTeacher(Long[] teacherIds);

    /**
     * 删除老师(仅仅将关系表中的老师修改为学生，不删除老师的个人信息)
     *
     * @param ids 要删除的教师id
     * @return
     * @author JOJO
     */
    boolean deleteTeacher(Long[] ids);

    /**
     * 查询临时老师
     * @param params
     * @return
     */
    PageUtils queryTempTeacherPage(Map<String, Object> params);
}

