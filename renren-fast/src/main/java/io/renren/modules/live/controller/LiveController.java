package io.renren.modules.live.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.entity.UserEntity;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.entity.CourseOrderEntity;
import io.renren.modules.course.service.CourseOrderService;
import io.renren.modules.live.entity.LiveEntity;
import io.renren.modules.live.entity.RoomEntity;
import io.renren.modules.live.service.LiveService;
import io.renren.modules.live.vo.LiveWithTeacherVO;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.entity.RoleUserRelationEntity;
import io.renren.modules.sys.service.GwyUserService;
import io.renren.modules.sys.service.RoleUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
@RestController
@RequestMapping("live/live")
public class LiveController {
    @Autowired
    private LiveService liveService;
    @Autowired
    private CourseOrderService courseOrderService;
    @Autowired
    private RoleUserRelationService roleUserRelationService;
    @Autowired
    private GwyUserService gwyUserService;
    @Autowired
    Gson gson;
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = liveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        LiveEntity live = liveService.getById(id);

        return R.ok().put("live", live);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LiveEntity live) {
        liveService.save(live);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody LiveEntity live) {
        liveService.updateById(live);

        return R.ok();
    }

    /**
     * 删除直播课程 需判断是否存在有效订单(非退款订单)
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        String msg = "";
        List<Long> fault = new ArrayList<>();
        List<Long> idList = Arrays.asList(ids);

        idList.forEach(x -> {
            if (courseOrderService.getBaseMapper().selectCount(
                    new QueryWrapper<CourseOrderEntity>()
                            .eq("course_id", x).ne("status", GwyConstant.OrderStatus.REFUNDED.getCode())
                            .eq("course_type", GwyConstant.CourseType.LIVE.getType())) > 0) {
                fault.add(x);

            } else {
                    liveService.removeById(x);
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
     * 从Redis中获取活跃的直播间
     *
     * @param params 参数个数
     * @return {@link R }
     * @author Geralt
     */
    @RequestMapping("/liveRoom")
    public R getLiveRoomList(Map<String, Object> params) {
        List<RoomEntity> roomList = liveService.getLiveRoomList(params);
        return R.ok().put("roomList", roomList);
    }

    /**
     * 停止直播房间
     *
     * @param form 形式
     * @return {@link R }
     * @author Geralt
     */
    @RequestMapping("/stopRoom")
    public R stopLiveRoom(@RequestBody Map<String, Object> form) {
        Long liveId = Long.valueOf((String) form.get("liveId"));
        boolean flag = liveService.stopLiveRoom(liveId);
        return flag ? R.ok() : R.error();
    }


    /**
     * 得到有直播权限的老师的信息
     *
     * @return {@link R }
     * @author Geralt
     */
    @RequestMapping("/getTeacherInfo")
    public R getTeacherInfo() {
        QueryWrapper<RoleUserRelationEntity> wrapper=new QueryWrapper<RoleUserRelationEntity>();
        wrapper.select("user_id");
        wrapper.eq("role_id",GwyConstant.RoleConstant.LIVE.getId());
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

    @RequestMapping("/updateVideo")
//    @CacheEvict(cacheNames = "course")
    public R updateVideo(@RequestBody Map<String,Object> params) {
        String id1 = params.get("id").toString();
        long id = Long.parseLong(id1);
//        CourseEntity courseEntity = new CourseEntity();
        LiveEntity liveEntity = new LiveEntity();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        liveEntity.setUpdateTime(time);
        liveEntity.setId(id);
        String videoUrl =  gson.toJson(params.get("videoUrl"));
        liveEntity.setVideoUrl(videoUrl);
        liveService.updateById(liveEntity);
        return R.ok();
    }
}
