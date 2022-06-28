package com.mnnu.examine.modules.live.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.vo.LiveWithTeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@Mapper
public interface LiveDao extends BaseMapper<LiveEntity> {
    /**
     * 查询销量前几的直播课
     * @param num 销量前几名
     * @return 直播列表
     * ---JOJO
     */
    List<LiveEntity> queryRecommendLiveEntity(@Param("num") int num);

    /**
     * 通过直播课id查找直播课和相对应老师信息
     * @param teacherId 老师
     * @param liveId 直播课id
     * @return 直播课和相对应老师信息
     * ---JOJO
     */
    List<LiveWithTeacherVO> queryLiveWithTeacherById(@Param("teacherId") long teacherId,@Param("liveId") long liveId);

    List<LiveWithTeacherVO> queryLiveWithTeacher(Page page, @Param("sort") int sort, @Param("key") String key);
}
