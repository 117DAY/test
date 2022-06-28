package com.mnnu.examine.modules.course.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Wrapper;
import java.util.ArrayList;

import java.util.List;

import java.util.ArrayList;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Mapper
public interface CourseDao extends BaseMapper<CourseEntity> {
    /*
    * 查询课程列表所需要的展示信息（课程、老师），包括分页和排序功能
    * Reborn
    * */
    public ArrayList<CourseTeacherVO> selectCourseTeacher(Page page, @Param("sort") int sort,@Param("key") String key) ;

    /**
     * 查询推荐课程
     * @param num 销量前几名
     * @return 推荐课程列表
     * @author JOJO
     */
	List<CourseEntity> queryRecommendCourseEntity(@Param("num") int num);


}
