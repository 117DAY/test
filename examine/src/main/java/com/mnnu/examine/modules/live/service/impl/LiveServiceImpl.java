package com.mnnu.examine.modules.live.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.AesUtils;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.common.utils.RedisUtils;
import com.mnnu.examine.common.utils.agora.RtmBuilder;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.entity.ViewRecordEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;
import com.mnnu.examine.modules.live.dao.LiveDao;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.entity.RoomEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.live.vo.LiveWithTeacherVO;
import com.mnnu.examine.modules.polyv.PolyvRoomVO;
import com.mnnu.examine.modules.polyv.PolyvUtils;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.sys.service.UserService;
import com.mnnu.examine.modules.sys.service.impl.UserServiceImpl;
import net.polyv.live.v1.entity.quick.QuickCreateChannelResponse;
//import net.polyv.vclass.v1.entity.lesson.VClassAddLessonResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("liveService")
public class LiveServiceImpl extends ServiceImpl<LiveDao, LiveEntity> implements LiveService {
    @Autowired
    CourseOrderService courseOrderService;
    @Autowired
    RoleUserRelationService roleUserRelationService;
    @Autowired
    UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LiveEntity> page = this.page(
                new Query<LiveEntity>().getPage(params),
                new QueryWrapper<LiveEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询销量前几的直播课
     *
     * @param num 销量前几
     * @return ---JOJO
     */
    @Override
    public List<LiveWithTeacherVO> queryRecommendLive(int num) {
        List<LiveEntity> liveEntities = getBaseMapper().queryRecommendLiveEntity(num);
        List<Long> collect = liveEntities.stream().map(LiveEntity::getTeacherId).collect(Collectors.toList());
        if (collect.size() != 0) {
            List<UserEntity> userEntities = userService.getBaseMapper().selectBatchIds(collect);
            HashMap<Long, UserEntity> longUserEntityHashMap = new HashMap<Long, UserEntity>();
            for (UserEntity user : userEntities) {
                longUserEntityHashMap.put(user.getId(), user);
            }
            List<LiveWithTeacherVO> liveWithTeacherVOS = new ArrayList<LiveWithTeacherVO>();
            for (LiveEntity live : liveEntities) {
                LiveWithTeacherVO liveWithTeacherVO = new LiveWithTeacherVO();
                UserEntity userEntity = longUserEntityHashMap.get(live.getTeacherId());
                BeanUtils.copyProperties(userEntity, liveWithTeacherVO);
                BeanUtils.copyProperties(live, liveWithTeacherVO);

                liveWithTeacherVOS.add(liveWithTeacherVO);
            }
            return liveWithTeacherVOS;
        } else {
            return null;
        }

    }

    @Override
    public List<LiveWithTeacherVO> queryLiveWithTeacherById(long teacherId, long liveId) {
        return getBaseMapper().queryLiveWithTeacherById(teacherId, liveId);
    }


    @Override
    public String getRtmByUserId(String userId) {
        try {
            return RtmBuilder.getRtmToken(userId);

        } catch (Exception e) {
            return null;
        }
    }


    @Autowired
    RedisUtils redisUtils;

    @Autowired
    PolyvUtils polyvUtils;

    @Autowired
    Gson gson;

    @Override
    public Map<String, Object> getRoomInfo(@RequestBody Map<String, Object> form, Long id) {
        Map<String, Object> res = new HashMap<>();
        String userId = String.valueOf(id);
        UserEntity userEntity = userService.getById(id);
        if (userEntity == null) return null;
        String liveId = (String) form.get("liveId");
        int isCreate = (int) form.get("isCreate");
        if (userId == null || liveId == null) return null;
        int role = roleUserRelationService.getUserRoleByUserId(userId);
        LiveEntity liveEntity = getBaseMapper().selectOne(new QueryWrapper<LiveEntity>().eq("id", liveId));

//        int role=5;
        if (role == -1) return null;
        else if (role == 5 && isCreate == 1) {//创建直播间
            //判断此用户是否是此课程老师
            if (!String.valueOf(liveEntity.getTeacherId()).equals(userId))
                return null;
            Long startTime = (Long) form.get("startTime");
            String roomUuid = AesUtils.encrypt(liveId + startTime, AesUtils.SALT).replaceAll("/", "");
            String beginTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            Long endTime = (Long) form.get("endTime");
            if (startTime == null || endTime == null) return null;
            Long duration = Math.abs((endTime - startTime) / (1000));//单位秒
            Long t = System.currentTimeMillis();
//            String roomName = (String) form.get("roomName");
            RoomEntity room = new RoomEntity();
            room.setStartTime(startTime);
            room.setDuration(duration);
            room.setUserId(userId);
            room.setRoomName(liveEntity.getCourseTitle());
            room.setRoomUuid(roomUuid);
            room.setBeginTime(beginTime);
            room.setLiveId(liveId);
            String teachUrl = "";
            String channelId = "";
            //判断是否已经创建频道
            if (liveEntity.getChannelId() == null || liveEntity.getChannelId().equals("")) {

                QuickCreateChannelResponse quickCreateChannelResponse = polyvUtils.createRoom(userEntity, room);
                if (quickCreateChannelResponse != null) {
                    channelId = quickCreateChannelResponse.getLiveChannelBasicInfoResponse().getChannelId();
                    try {
                        //设置授权模式
                        String SecretKey = polyvUtils.updateChannelAuth(channelId);
                        //设置开启回放列表
                        System.out.println(polyvUtils.updateChannelPlaybackSetting(channelId));
                        if (SecretKey == null || SecretKey.equals("")) {
                            throw new Exception();
                        }
                        ;
//                        room.setPolyvSecretKey(SecretKey);

                        //频道信息存入数据库------
                        liveEntity.setChannelId(channelId);
                        liveEntity.setSecretKey(SecretKey);
                        this.getBaseMapper().updateById(liveEntity);
                        //---------

                    } catch (Exception e) {

                    }

                } else {
                    return null;
                }

            } else {
                channelId = liveEntity.getChannelId();
            }

            try {
                teachUrl = polyvUtils.tokenLogin(userEntity, channelId);
            } catch (Exception e) {

            }
            room.setPolyvLessonId(Long.parseLong(channelId));


            res.put("url", teachUrl);
            // room.
//            res.put(vClassAddLessonResponse.
            if (teachUrl != null && !teachUrl.equals(""))
                redisUtils.getRedisTemplate().opsForValue().set("live" + liveId, room, (endTime - t) / 1000, TimeUnit.SECONDS);

            //隐藏SecretKey
            room.setPolyvSecretKey("");
            res.put("room", room);
            return res;
        } else {
            //获取直播间
            //   是否是此此课程的老师
            if (String.valueOf(liveEntity.getTeacherId()).equals(userId)) {
                RoomEntity roomEntity = (RoomEntity) redisUtils.getRedisTemplate().opsForValue().get("live" + liveId);
//                RoomEntity roomEntity=gson.fromJson(oRoom.toString(),RoomEntity.class);
                String teachUrl = "";
                //隐藏SecretKey-----
                if (roomEntity != null) {
                    roomEntity.setPolyvSecretKey("");
                    res.put("room", roomEntity);
                }
                //------------
                try {

                    teachUrl = polyvUtils.tokenLogin(userEntity, String.valueOf(liveEntity.getChannelId()));
                } catch (Exception e) {

                }
                res.put("url", teachUrl);
                return res;

            }
            //判断用户是否购买此直播
            else if (courseOrderService.isBuyLiveCourse(userId, liveId)) {
                RoomEntity roomEntity = (RoomEntity) redisUtils.getRedisTemplate().opsForValue().get("live" + liveId);
//                RoomEntity roomEntity=gson.fromJson(oRoom.toString(),RoomEntity.class);
                String studentUrl = "";

                try {
                    studentUrl = polyvUtils.getStudentUrl(userEntity, String.valueOf(liveEntity.getChannelId()), liveEntity.getSecretKey());
                } catch (Exception e) {

                }
                //隐藏SecretKey
                if (roomEntity != null) {
                    roomEntity.setPolyvSecretKey("");
                    res.put("room", roomEntity);
                }
                res.put("url", studentUrl);
                return res;
            }
        }
        return null;
    }

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
        Set<String> keys = redisUtils.getRedisTemplate().keys("live*");
        if (keys != null)
            roomEntityList = keys.stream().map(c -> ((RoomEntity) Objects.requireNonNull(redisUtils.getRedisTemplate().opsForValue().get(c))).getNoIdEntity()).collect(Collectors.toList());
        else roomEntityList = new ArrayList<>();
        return roomEntityList;


    }

    @Override
    public PageUtils getTeacherLiveListByUserId(Map<String, Object> params, Long id) {

        QueryWrapper<LiveEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", id);
        IPage<LiveEntity> page = this.page(
                new Query<LiveEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);


    }

    @Override
    public boolean isLiveRoomByLiveId(Long liveId) {
        return Boolean.TRUE.equals(redisUtils.getRedisTemplate().hasKey("live" + liveId));
    }

    @Override
    public boolean stopLiveRoom(Long liveId, Long userId) {
        if (!String.valueOf(getBaseMapper().selectOne(new QueryWrapper<LiveEntity>().eq("id", liveId)).getTeacherId()).equals(String.valueOf(userId)))
            return false;
        return redisUtils.getRedisTemplate().delete("live" + liveId);
    }

    @Override
    public ArrayList<LiveWithTeacherVO> queryLiveWithTeacherList(Map<String, Object> params, Integer total) {
        //为分页做准备

        int page = Integer.parseInt(params.get("page") == null ? "1" : (String) params.get("page"));
        int limit = Integer.parseInt(params.get("limit") == null ? "16" : (String) params.get("limit"));
        if (page <= 0) {
            page = 1;
        }
        int totalPage = 1;
        if (total % limit != 0) {
            totalPage = total / limit + 1;
        } else {
            totalPage = total / limit;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Page<LiveWithTeacherVO> p = new Page<>(page, limit);

        ArrayList<LiveWithTeacherVO> list;
        String sort = params.get("sort") == null ? "0" : (String) params.get("sort");
        //根据排序的类型来判断是否相应的排序方法，并做好分页
        if (sort.equals("0")) {
            //这里写默认排序
            System.out.println(getBaseMapper().queryLiveWithTeacher(p, 0, (String) params.get("key")));
            list = getBaseMapper().queryLiveWithTeacher(p, 0, (String) params.get("key")).size() == 0 ? new ArrayList<>() : (ArrayList<LiveWithTeacherVO>) getBaseMapper().queryLiveWithTeacher(p, 0, (String) params.get("key"));
        } else if (sort.equals("1")) {
            //这里写按时间排序
            list = getBaseMapper().queryLiveWithTeacher(p, 1, (String) params.get("key")).size() == 0 ? new ArrayList<>() : (ArrayList<LiveWithTeacherVO>) getBaseMapper().queryLiveWithTeacher(p, 1, (String) params.get("key"));
        } else if (sort.equals("2")) {
            //这里写按价格排序
            list = getBaseMapper().queryLiveWithTeacher(p, 2, (String) params.get("key")).size() == 0 ? new ArrayList<>() : (ArrayList<LiveWithTeacherVO>) getBaseMapper().queryLiveWithTeacher(p, 2, (String) params.get("key"));
        } else {
            //这里写按销量排序
            list = getBaseMapper().queryLiveWithTeacher(p, 3, (String) params.get("key")).size() == 0 ? new ArrayList<>() : (ArrayList<LiveWithTeacherVO>) getBaseMapper().queryLiveWithTeacher(p, 3, (String) params.get("key"));

        }

        if (params.containsKey("page")) {
            params.replace("page", page);
        } else {
            params.put("page", page);
        }


        return list;
    }

    @Override
    public Integer count(String key) {
        Page<CourseTeacherVO> p = new Page<>(1, 999);
        ArrayList list = (ArrayList) getBaseMapper().queryLiveWithTeacher(null, 0, key);

        return list.size();
    }

}
