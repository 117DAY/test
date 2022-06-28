package com.mnnu.examine.modules.course.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface CourseService extends IService<CourseEntity> {

    PageUtils queryPage(Map<String, Object> params);
//    查询推荐课程 num 为销量前几名
    List<CourseEntity> queryRecommendCourseEntity(int num);



    public ArrayList<CourseTeacherVO> queryLessonList(Map<String,Object> params,Integer total);

    Integer count(String key);

    Boolean isBuy(Integer id,Integer type);

    /**
     * 查找老师自己的所有视频
     * @param params
     * @param userId
     * @return
     */
    PageUtils getCoursesByTeacherId(Map<String, Object> params, Long teacherId);

    Map Buy(Integer id, Integer type);


}

