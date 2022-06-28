package io.renren.modules.live.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.live.entity.LiveEntity;
import io.renren.modules.live.entity.RoomEntity;
import io.renren.modules.live.vo.LiveWithTeacherVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface LiveService extends IService<LiveEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 查询销量前几的直播课
     * @param num 销量前几
     * @return 推荐直播列表
     * ---JOJO
     */
//    List<LiveWithTeacherVO> queryRecommendLive(int num);

    /**
     * 通过直播课id查找直播课和相对应老师信息
     *
     * @param teacherId 老师
     * @param liveId    直播课id
     * @return 直播课和相对应老师信息
     * ---JOJO
     */
//    List<LiveWithTeacherVO> queryLiveWithTeacherById(@Param("teacherId") long teacherId, @Param("liveId") long liveId);

    /*

     */
/**
 *
 *
 * @description: 获取Rtm token
 * @param userId
 * @return:
 * @author: Geralt
 * @time: 2021/11/26 16:31
 *//*

    String getRtmByUserId(String userId);

    */
//*
// *
// *
// * @description: 获取房间信息
// * @param [roomName] (仅在教师时作用)
// * @return: roomEntity
// * @author: Geralt
// * @time: 2021/11/26 16:29
//

//    Map<String,Object> getRoomInfo(@RequestBody Map<String,Object> form, Long id);

//*
// *
// *
// * @description: 获取Redis中活跃的直播间
// * @return: 分页对象
// * @author: Geralt
// * @time: 2021/11/29 16:57
//

    List<RoomEntity> getLiveRoomList(Map<String, Object> params);
    boolean stopLiveRoom(Long liveId);


}

