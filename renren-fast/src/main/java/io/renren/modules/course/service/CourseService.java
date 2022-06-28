package io.renren.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.vo.CourseTeacherVO;

import java.util.ArrayList;
import java.util.List;
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



    public ArrayList<CourseTeacherVO> queryLessonList(Map<String,Object> params, Integer total);
}

