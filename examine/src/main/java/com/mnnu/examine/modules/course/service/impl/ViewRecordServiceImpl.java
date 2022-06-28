package com.mnnu.examine.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.course.dao.ViewRecordDao;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.entity.ViewRecordEntity;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.course.service.ViewRecordService;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service("viewRecordService")
public class ViewRecordServiceImpl extends ServiceImpl<ViewRecordDao, ViewRecordEntity> implements ViewRecordService {
    @Autowired
    CourseService courseService;
    @Autowired
    LiveService liveService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ViewRecordEntity> page = this.page(
                new Query<ViewRecordEntity>().getPage(params),
                new QueryWrapper<ViewRecordEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *
     * @description: 获取用户观看历史记录
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/26 17:47
     */
    @Override
    public PageUtils getViewRecordListByUserId(Map<String, Object> params, Long userId) {
        QueryWrapper<ViewRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        IPage<ViewRecordEntity> page = this.page(
                new Query<ViewRecordEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }


    /**
     * 插入观看记录
     *
     * @param map 地图
     * @return int
     */
    @Override
    public int insertViewRecord(Map<String, Object> map) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Long courseId = Long.parseLong((String) map.get("id"));
        Long type = Long.parseLong((String) map.get("type"));
        String second = (String) map.get("time");
        Integer episode = Integer.parseInt((String) map.get("episode"));

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time = sdf.format(date);

        String courseCoverUrl = "";
        String courseTitle = "";
        if (type == 0) {
            CourseEntity c = courseService.getById(courseId);
            courseCoverUrl = c.getCoverUrl();
            courseTitle = c.getCourseTitle();
        } else {
            LiveEntity l = liveService.getById(courseId);
            courseCoverUrl = l.getCoverUrl();
            courseTitle = l.getCourseTitle();
        }
        ViewRecordEntity viewRecordEntity = new ViewRecordEntity();
        viewRecordEntity.setUserId(id);
        viewRecordEntity.setCourseId(courseId);
        viewRecordEntity.setCourseType(type);
        viewRecordEntity.setViewTime(time);
        // 观看的集数
        viewRecordEntity.setViewPercentage(new BigDecimal(episode));
        viewRecordEntity.setViewSecond(second);
        viewRecordEntity.setCourseCoverUrl(courseCoverUrl);
        viewRecordEntity.setCourseTitle(courseTitle);
        viewRecordEntity.setShowStatus(1);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", id);
        wrapper.eq("course_id", courseId);
        wrapper.eq("course_type", type);
        //一种课程只能有一个观看历史
        ArrayList<ViewRecordEntity> list = (ArrayList<ViewRecordEntity>) getBaseMapper().selectList(wrapper);
        if (list.size() != 0) {//已有观看记录
            int result = getBaseMapper().update(viewRecordEntity, wrapper);
            return result;
        } else {//没有记录就插入观看历史
            int result = getBaseMapper().insert(viewRecordEntity);
            return result;
        }

    }

    @Override
    public String getTime(Map<String, Object> params) {

        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Long courseId = Long.parseLong((String) params.get("id"));
        Long type = Long.parseLong((String) params.get("type"));
        Integer episode = Integer.parseInt((String) params.get("episode"));
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_id", id);
        wrapper.eq("course_id", courseId);
        wrapper.eq("course_type", type);
        wrapper.eq("view_percentage", episode);
        ViewRecordEntity v = getBaseMapper().selectOne(wrapper);
        if (v == null) {
            return "0";
        }
        return v.getViewSecond();
    }


}
