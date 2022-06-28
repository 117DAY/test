package io.renren.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.UserEntity;
import io.renren.modules.app.service.UserService;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.entity.CourseOrderEntity;
import io.renren.modules.course.service.CourseOrderService;
import io.renren.modules.course.service.CourseService;
import io.renren.modules.course.vo.CourseTeacherVO;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.entity.RoleUserRelationEntity;
import io.renren.modules.sys.service.GwyUserService;
import io.renren.modules.sys.service.RoleUserRelationService;
import io.renren.modules.teacher.entity.TeacherInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@RestController
@RequestMapping("course/course")
@CacheEvict(cacheNames = "course",allEntries = true)

public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseOrderService courseOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    Gson gson;
    @Autowired
    private GwyUserService gwyUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = courseService.queryPage(params);

        return R.ok().put("page", page);
    }

    @Autowired
    RoleUserRelationService roleUserRelationService;
    /**
     * 列表
     */
    @RequestMapping("/getTeacherInfo")
    public R getTeacherInfo() {
        QueryWrapper<RoleUserRelationEntity> wrapper=new QueryWrapper<RoleUserRelationEntity>();
        wrapper.select("user_id");
        wrapper.eq("role_id",2).or().eq("role_id",5);

        List<RoleUserRelationEntity> list=roleUserRelationService.list(wrapper);
        ArrayList teacherList=new ArrayList();
        for(RoleUserRelationEntity r:list){
            QueryWrapper wrapper1=new QueryWrapper();
            wrapper1.eq("id",r.getUserId());
            GwyUserEntity userEntity=gwyUserService.getOne(wrapper1);
            teacherList.add(userEntity);
        }
        return R.ok().put("teacherList", teacherList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        CourseEntity course = courseService.getById(id);

        return R.ok().put("course", course);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @CacheEvict(cacheNames = "course")
    public R save(@RequestBody CourseEntity course) {
        courseService.save(course);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CourseEntity course) {
        courseService.updateById(course);
        return R.ok();
    }

    @RequestMapping("/updateVideo")
    @CacheEvict(cacheNames = "course")
    public R updateVideo(@RequestBody Map<String,Object> params) {
        System.out.println(params);
        Integer id = (Integer) params.get("id");
        CourseEntity courseEntity = new CourseEntity();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        courseEntity.setUpdateTime(time);
        courseEntity.setId(id);
        String videoUrl =  gson.toJson(params.get("videoUrl"));
        courseEntity.setVideoUrl(videoUrl);
        courseService.updateById(courseEntity);
        return R.ok();
    }

    /**
     * 删除课程 (判断是否存在订单) --Geralt
     */
    @RequestMapping("/delete")
    @CacheEvict(cacheNames = "course")

    public R delete(@RequestBody Integer[] ids) {

        String msg = "";
        List<Integer> fault = new ArrayList<>();
        List<Integer> idList = Arrays.asList(ids);

        idList.forEach(x -> {
            if (courseOrderService.getBaseMapper().selectCount(
                    new QueryWrapper<CourseOrderEntity>()
                            .eq("course_id", x).ne("status", GwyConstant.OrderStatus.REFUNDED.getCode())
                            .eq("course_type", GwyConstant.CourseType.RECORDED.getType())) > 0) {
                fault.add(x);

            } else {
                courseService.removeById(x);
            }
        });

        if(fault.size()==0){
            return R.ok();
        }
        else {
            msg="ID为";
            msg+=fault.toString()+"的课程存在订单,无法删除!!";
            return R.error(msg);
        }
    }

    /**
     * 返回所有的数据,其中包括课程列表展示时需要的数据和老师的数据（分页）,
     * 还能够根据排序的类型进行相应的排序
     * ——Reborn
     */
    @RequestMapping("/lessonList")

    public R lessonList(@RequestParam Map<String, Object> params) {
        //得到所有课程的数量，用以给前端分页
        Integer total = courseService.count();
        //得到分页查寻后的所需的课程信息与老师信息
        ArrayList<CourseTeacherVO> list = courseService.queryLessonList(params, total);
        return R.ok().put("allCourses", list).put("total", total).put("currentPage", params.get("page"));
    }

    /**
     * 返回课程所需的详细信息（课程，老师）
     */
    @RequestMapping("/lessonDetail")
    public R lessonDetail(@RequestParam("id") Long id) {

        CourseEntity data = courseService.getById(id);
        Integer teacherId = data.getTeacherId();
        UserEntity teacher = userService.getById(teacherId);
        return R.ok().put("data", data).put("teacher", teacher);
    }

    @RequestMapping("/recommend")
    public R queryRecommendCourse() {
        Integer total = 10;
        Map<String, Object> map = new HashMap<String, Object>(5);
        map.put("page", "1");
        map.put("limit", "10");
        map.put("type", "1");
        map.put("sort", "3");
        ArrayList<CourseTeacherVO> list = courseService.queryLessonList(map, total);
        return R.ok().put("recommend", list);

    }



}
