package io.renren.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.dao.CourseDao;
import io.renren.modules.course.entity.CommentsEntity;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.service.CourseService;
import io.renren.modules.course.vo.CourseTeacherVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CourseEntity> page;
        if(Integer.parseInt((String) params.get("sort"))==0){
            page = this.page(
                    new Query<CourseEntity>().getPage(params),
                    new QueryWrapper<CourseEntity>().like("course_title",params.get("key"))
            );
        }else if(Integer.parseInt((String) params.get("sort"))==1){
            page = this.page(
                    new Query<CourseEntity>().getPage(params),
                    new QueryWrapper<CourseEntity>().eq("is_public",1).like("course_title",params.get("key"))
            );
        }else {
            page = this.page(
                    new Query<CourseEntity>().getPage(params),
                    new QueryWrapper<CourseEntity>().eq("is_public",0).like("course_title",params.get("key"))
            );
        }

        return new PageUtils(page);
    }

    /**
     * 查询推荐课程
     *
     * @param num 推荐销量前几
     * @return 推荐课程列表
     * ---JOJO
     */
    @Override
    public List<CourseEntity> queryRecommendCourseEntity(int num) {
        return getBaseMapper().queryRecommendCourseEntity(num);
    }


    @Override
    public ArrayList<CourseTeacherVO> queryLessonList(Map<String, Object> params, Integer total) {
        //为分页做准备

        int page = Integer.parseInt(params.get("page") == null ? "1" : (String) params.get("page"));
        int limit = Integer.parseInt(params.get("limit") == null ? "16" : (String) params.get("limit"));
        if (page <= 0) {
            page = 1;
        }
        int totalPage = 1;
        if (page % limit != 0) {
            totalPage = total / limit + 1;
        } else {
            totalPage = total / limit;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Page<CourseTeacherVO> p = new Page<>(page, limit);

        ArrayList<CourseTeacherVO> list = null;
        String sort = params.get("sort") == null ? "0" : (String) params.get("sort");
        //根据排序的类型来判断是否相应的排序方法，并做好分页
        if (sort.equals("0")) {
            //这里写默认排序
            list = getBaseMapper().selectCourseTeacher(p, 0);
        } else if (sort.equals("1")) {
            //这里写按时间排序
            list = getBaseMapper().selectCourseTeacher(p, 1);
        } else if (sort.equals("2")) {
            //这里写按价格排序
            list = getBaseMapper().selectCourseTeacher(p, 2);
        } else {
            //这里写按销量排序
            list = getBaseMapper().selectCourseTeacher(p, 3);

        }

        if (params.containsKey("page")) {
            params.replace("page", page);
        } else {
            params.put("page", page);
        }

        return list;
    }


}
