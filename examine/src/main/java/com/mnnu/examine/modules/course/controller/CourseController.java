package com.mnnu.examine.modules.course.controller;


import com.google.gson.Gson;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;
import com.mnnu.examine.modules.course.vo.LessonUrlNeedsVO;
import com.mnnu.examine.modules.course.vo.PurchaseQualificationVO;
import com.mnnu.examine.modules.course.vo.VideoVo;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.oss.utils.OSSUtils;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseOrderService courseOrderService;

    @Autowired
    private LiveService liveService;

    @Autowired
    Gson gson;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = courseService.queryPage(params);

        return R.ok().put("page", page);
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

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        courseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 返回所有的数据,其中包括课程列表展示时需要的数据和老师的数据（分页）,
     * 还能够根据排序的类型进行相应的排序
     * ——Reborn
     */
    @RequestMapping("/lessonList")

//    @Cacheable(value = "course", key = "#root.methodName+'_'+#params.get('page')+'_'+#params.get('sort')+'_'+#params.get('key')")
    public R lessonList(@RequestParam Map<String, Object> params) {

        //得到所有课程的数量，用以给前端分页(会根据关键词来判断总个数)
        String key = params.get("key") == null ? "" : (String) params.get("key");
        Integer total = courseService.count(key);
        //得到分页查寻后的所需的课程信息与老师信息
        ArrayList<CourseTeacherVO> list = courseService.queryLessonList(params, total);
        return R.ok().put("allCourses", list).put("total", total).put("currentPage", params.get("page"));
    }

    /**
     * 返回课程所需的详细信息（课程，老师）
     */
    @RequestMapping("/lessonDetail")
//    @Cacheable(key = "'details_'+#id", value = "course")
    public R lessonDetail(@RequestParam("id") Long id) throws Throwable {
        if (courseService.getById(id) == null) {
            throw new GwyException("课程不存在", "课程不存在", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());

        } else {
            CourseEntity data = courseService.getById(id);
            if (data.getIsPublic()==0){
                throw new GwyException("课程暂未公开", "课程暂未公开", GwyConstant.BizCode.LESSON_NOT_PUBLIC.getCode());

            }
            Integer teacherId = data.getTeacherId();
            UserEntity teacher = userService.getById(teacherId);
            //如果没有课程地址，那么就返回空的list，有就得到相应的地址
            String url =  (data.getVideoUrl());
            if(StringUtils.isBlank(url)){
                return R.ok().put("data", data).put("teacher", teacher).put("courseList", "");
            }
            //这里得到的是json格式的字符串

            //利用gson将json格式的字符串切割称VideoVo数组
            VideoVo[] a=gson.fromJson(url, VideoVo[].class);
            List<VideoVo> videoVoList = Arrays.asList(a);
            //得到符合前端需求的url(地址)数组
            ArrayList list=new ArrayList();
            for (VideoVo v:videoVoList){
                list.add(v.getUrl());
            }
            return R.ok().put("data", data).put("teacher", teacher).put("courseList", list);
        }

    }

    @RequestMapping("/recommend")
//    @Cacheable(value = "course", key = "#root.methodName")
    public R queryRecommendCourse() {
        Integer total = 10;
        Map<String, Object> map = new HashMap<>(5);
        map.put("page", "1");
        map.put("limit", "10");
        map.put("type", "1");
        map.put("sort", "3");
        map.put("key", "");
        ArrayList<CourseTeacherVO> list = courseService.queryLessonList(map, total);
        return R.ok().put("recommend", list);

    }

    /**
     * 根据用户的id以及传入的课程id来判断用户是否购买了该课程
     * id是课程，userId是用户的id
     * 前端会判断传入的课程id是否是数字类型，所以后端不用再判断
     * 后端逻辑：先判断id是否是空，空就是课程不存在，不空就查课程表,看是否有该课程
     * @return {@link R }
     * @author Reborn
     */
    @RequestMapping("/isBuy")
    public R isBuy(@Validated @RequestBody PurchaseQualificationVO p) throws Throwable {
        Integer id= Integer.valueOf(p.getId());

        Integer type= Integer.valueOf(p.getType());
        if(courseService.isBuy(id,type)){
            return R.ok();
        }else {
            return R.error();
        }
    }
    @Resource
    OSSUtils ossUtils;
    /**
     * 根据课程id和课程集数来得到视频列表以及当前要播放的视频，之后就直接在前端进行视频url的切换，不会再调用接口了
     * id是课程，episode是集数
     * 前端会判断传入的课程id已经课程集数episode是否是数字类型
     *
     * @return {@link R }
     * @author Reborn
     */
    @RequestMapping("/lessonUrl")
    public R getLessonUrl(@Valid @RequestBody LessonUrlNeedsVO lessonUrlNeedsVO) throws Throwable {
        Integer id= Integer.valueOf(lessonUrlNeedsVO.getId());
        Integer type= Integer.valueOf(lessonUrlNeedsVO.getType());
        Integer episode= Integer.valueOf(lessonUrlNeedsVO.getEpisode());
        if(courseService.isBuy(id,type)) {
            if (type == 0) {//根据不同的type来判断课程类型
                CourseEntity courseEntity = courseService.getById(id);
                if (courseEntity == null) {
                    throw new GwyException("课程不存在", "课程不存在", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());
                }

                String url =  (courseEntity.getVideoUrl());
                ArrayList list=new ArrayList();
                if(url.equals("")){
                     list=null;
                }else{
                    //这里得到的是json格式的字符串

                    //利用gson将json格式的字符串切割称VideoVo数组
                    VideoVo[] a=gson.fromJson(url, VideoVo[].class);
                    List<VideoVo> videoVoList = Arrays.asList(a);
                    //得到符合前端需求的url(地址)数组

                    for (VideoVo v:videoVoList){
                        list.add(ossUtils.authentication(v.getUrl()));
                    }
                }


                if(episode>list.size()||episode<=0){
                    throw new GwyException("没有这一集", "没有这一集", GwyConstant.BizCode.LESSON_EPISODE_NOT_EXIST.getCode());
                }
                episode = episode - 1;//因为前端传进来的会是第几集，即下标从1开始
                String currentLessonUrl = (String) list.get(episode);
                return R.ok().put("urlList", list).put("currentLessonUrl", currentLessonUrl);
            } else if (type == 1) {
                if (liveService.getById(id) == null) {
                    throw new GwyException("课程不存在", "课程不存在", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());
                }


                LiveEntity liveEntity = liveService.getById(id);

                String url =  (liveEntity.getVideoUrl());
                ArrayList list=new ArrayList();
                if(url.equals("")){
                    list=null;
                }else{
                    //这里得到的是json格式的字符串

                    //利用gson将json格式的字符串切割称VideoVo数组
                    VideoVo[] a=gson.fromJson(url, VideoVo[].class);
                    List<VideoVo> videoVoList = Arrays.asList(a);
                    //得到符合前端需求的url(地址)数组

                    for (VideoVo v:videoVoList){
                        list.add(ossUtils.authentication(v.getUrl()));
                    }
                }

                if(episode>list.size()){
                    throw new GwyException("没有这一集", "没有这一集", GwyConstant.BizCode.LESSON_EPISODE_NOT_EXIST.getCode());
                }
                episode = episode - 1;//因为前端传进来的会是第几集，即下表从1开始
                String currentLessonUrl = (String) list.get(episode);

                return R.ok().put("urlList", list).put("currentLessonUrl", currentLessonUrl);
            } else {
                throw new GwyException("没有这种类型的课程", "没有这种类型的课程", GwyConstant.BizCode.LESSON_KIND_NOT_EXIST.getCode());
            }
        }else {
            return R.error();
        }

    }

    /**
     * 查找老师所拥有的课程
     * @param params
     * @return
     * @author JOJO
     */
    @RequestMapping("/teachercourse")
    public R getTeacherCourse(@RequestParam Map<String, Object> params){
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        PageUtils page = courseService.getCoursesByTeacherId(params, id);

        return R.ok().put("page", page);
    }

    @RequestMapping("/buyLesson")
    public R buyLesson(@Validated @RequestBody PurchaseQualificationVO p) throws Throwable {
        Integer id= Integer.valueOf(p.getId());

        Integer type= Integer.valueOf(p.getType());
        Map map=courseService.Buy(id,type);

        return R.ok().put("result",map);

    }
    @RequestMapping("/upload")
    public R uploadVideo(@RequestParam Map<String, Object> params){
        PageUtils page = courseService.queryPage(params);
        return R.ok().put("page", page);



    }

    @RequestMapping("/updateVideo")
    @CacheEvict(cacheNames = "course")
    public R updateVideo(@RequestBody Map<String,Object> params) {
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


}
