package io.renren.modules.live.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.RedisUtils;
import io.renren.common.utils.agora.RtmBuilder;
import io.renren.modules.app.service.UserService;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.service.CourseOrderService;
import io.renren.modules.live.dao.LiveDao;
import io.renren.modules.live.entity.LiveEntity;
import io.renren.modules.live.entity.RoomEntity;
import io.renren.modules.live.service.LiveService;
import io.renren.modules.live.vo.LiveWithTeacherVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service("liveService")
public class LiveServiceImpl extends ServiceImpl<LiveDao, LiveEntity> implements LiveService {
    @Autowired
    CourseOrderService courseOrderService;
 /*   @Autowired
    RoleUserRelationService roleUserRelationService;*/

    @Autowired
    UserService userService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<LiveEntity> page = this.page(
//                new Query<LiveEntity>().getPage(params),
//                new QueryWrapper<LiveEntity>()
//        );
        IPage<LiveEntity> page;
        if(Integer.parseInt((String) params.get("sort"))==0){
            page = this.page(
                    new Query<LiveEntity>().getPage(params),
                    new QueryWrapper<LiveEntity>().like("course_title",params.get("key"))
            );
        }else if(Integer.parseInt((String) params.get("sort"))==1){
            page = this.page(
                    new Query<LiveEntity>().getPage(params),
                    new QueryWrapper<LiveEntity>().eq("is_public",1).like("course_title",params.get("key"))
            );
        }else {
            page = this.page(
                    new Query<LiveEntity>().getPage(params),
                    new QueryWrapper<LiveEntity>().eq("is_public",0).like("course_title",params.get("key"))
            );
        }

        return new PageUtils(page);
    }
    /*
     */

    /**
     * 查询销量前几的直播课
     *
     * @param num 销量前几
     * @return ---JOJO
     *//*
    @Override
    public List<LiveWithTeacherVO> queryRecommendLive(int num) {
        List<LiveEntity> liveEntities = getBaseMapper().queryRecommendLiveEntity(num);
        List<Long> collect = liveEntities.stream().map(LiveEntity::getTeacherId).collect(Collectors.toList());
        List<GwyUserEntity> userEntities = userService.getBaseMapper().selectBatchIds(collect);
        HashMap<Long, GwyUserEntity> longUserEntityHashMap = new HashMap<Long, GwyUserEntity>();
        for (GwyUserEntity user:userEntities) {
            longUserEntityHashMap.put(user.getId(),user);
        }
        List<LiveWithTeacherVO> liveWithTeacherVOS = new ArrayList<LiveWithTeacherVO>();
        for (LiveEntity live:liveEntities) {
            LiveWithTeacherVO liveWithTeacherVO = new LiveWithTeacherVO();
            GwyUserEntity userEntity = longUserEntityHashMap.get(live.getTeacherId());
            BeanUtils.copyProperties(live,liveWithTeacherVO);
            BeanUtils.copyProperties(userEntity,liveWithTeacherVO);
            liveWithTeacherVOS.add(liveWithTeacherVO);
        }
        return liveWithTeacherVOS;
    }*/
//    @Override
//    public List<LiveWithTeacherVO> queryLiveWithTeacherById(long teacherId, long liveId) {
//        return getBaseMapper().queryLiveWithTeacherById(teacherId, liveId);
//    }

    /**
     * @description: 从Redis中获取活跃的直播间
     * @param: [分页参数]
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/29 16:57
     */
    @Override
    public List<RoomEntity> getLiveRoomList(Map<String, Object> params) {
        List<RoomEntity> roomEntityList;
        Set<String> keys = redisTemplate.keys("live*");
        Gson gson = new Gson();
        if (keys != null) {
            roomEntityList = keys.stream().map(c -> {
                        String json = (String) redisTemplate.opsForValue().get(c);
                        int begin = json.indexOf("{");
                        int end = json.lastIndexOf("}");
                        String substring = json.substring(begin, end + 1);
                        RoomEntity roomEntity = gson.fromJson(substring, RoomEntity.class);
                        roomEntity.setRtmToken(getRtmByUserId(roomEntity.getUserId()));
                        return roomEntity;
                    }
            ).collect(Collectors.toList());
        } else {
            roomEntityList = new ArrayList<>();
        }
        return roomEntityList;
    }

    @Override
    public boolean stopLiveRoom(Long liveId) {
        return Boolean.TRUE.equals(redisTemplate.delete("live" + liveId));
    }

    public String getRtmByUserId(String userId) {
        try {
            return RtmBuilder.getRtmToken(userId);

        } catch (Exception e) {
            return null;
        }
    }

}
