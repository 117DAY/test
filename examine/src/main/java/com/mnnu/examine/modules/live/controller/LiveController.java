package com.mnnu.examine.modules.live.controller;


import cn.hutool.core.util.PageUtil;
import com.google.gson.Gson;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.AesUtils;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.vo.VideoVo;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.entity.RoomEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.live.vo.LiveWithTeacherVO;
import com.mnnu.examine.modules.polyv.PolyvUtils;
import com.mnnu.examine.modules.polyv.common.constant.Constant;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.UserService;
import net.polyv.live.v1.entity.channel.playback.LiveChannelVideoListResponse;
import net.polyv.live.v1.entity.channel.playback.LiveListChannelVideoLibraryResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    private UserService userService;

    @Autowired
    private Gson gson;

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
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        liveService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @RequestMapping("/rtm")
    public R getRtmByUserId(@RequestBody Map<String, Object> form) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String res = liveService.getRtmByUserId(String.valueOf(id));
        if (res != null) {
            return R.ok().put("rtm", res);
        } else {
            return R.error();
        }

    }

    @RequestMapping("/rtm0")
    public R getRtm0ByUserId() {
        String res = liveService.getRtmByUserId(String.valueOf(0));
        if (res != null) {
            return R.ok().put("rtm", res);
        } else {
            return R.error();
        }

    }

    @RequestMapping("/room")
    public R getRoomInfo(@RequestBody Map<String, Object> form) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Map<String, Object> res = liveService.getRoomInfo(form, id);
        if (res != null) {
            return R.ok().put("data", res);
        } else {
            return R.error();
        }
    }

    /**
     * 查询推荐直播信息
     * ---JOJO
     */
    @RequestMapping("/recommend")
    public R recommendLives() {
        List<LiveWithTeacherVO> liveWithTeacherVOS = liveService.queryRecommendLive(10);
        return R.ok().put("recommend", liveWithTeacherVOS);
//        return R.ok();
    }

    /**
     * 从Redis中获取活跃的直播间
     *
     * @param params 参数个数
     * @return {@link R }
     * @author Geralt
     */
    @RequestMapping("liveRoom")
    public R getLiveRoomList(Map<String, Object> params) {
        List<RoomEntity> roomList = liveService.getLiveRoomList(params);
        return R.ok().put("roomList", roomList);
    }

    @RequestMapping("/teacherLive")
    public R getTeacherLiveListByUserId(@RequestParam Map<String, Object> params) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        PageUtils page = liveService.getTeacherLiveListByUserId(params, id);

        return R.ok().put("page", page);

    }


    /**
     * 房间是否活跃
     *
     * @param id id
     * @return {@link R }
     * @author Geralt
     */
    @RequestMapping("/isLive/{id}")
    public R isLiveRoomByLiveId(@PathVariable("id") Long id) {
        boolean flag = liveService.isLiveRoomByLiveId(id);
        return R.ok().put("flag", flag);
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
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        Long liveId = Long.valueOf((String) form.get("liveId"));
        boolean flag = liveService.stopLiveRoom(liveId, id);
        return flag ? R.ok() : R.error();
    }

    @RequestMapping("/liveList")

//    @Cacheable(value = "course", key = "#root.methodName+'_'+#params.get('page')+'_'+#params.get('sort')+'_'+#params.get('key')")
    public R liveList(@RequestParam Map<String, Object> params) {

        //得到所有课程的数量，用以给前端分页(会根据关键词来判断总个数)
        String key = params.get("key") == null ? "" : (String) params.get("key");
        Integer total = liveService.count(key);
        //得到分页查寻后的所需的课程信息与老师信息
        ArrayList<LiveWithTeacherVO> list = liveService.queryLiveWithTeacherList(params, total);
        return R.ok().put("allCourses", list).put("total", total).put("currentPage", params.get("page"));
    }

    /**
     * 返回课程所需的详细信息（课程，老师）
     */
    @RequestMapping("/liveDetail")
//    @Cacheable(key = "'details_'+#id", value = "course")
    public R lessonDetail(@RequestParam("id") Long id) throws Throwable {
        if (liveService.getById(id) == null) {
            throw new GwyException("课程不存在", "课程不存在", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());

        } else {
            LiveEntity data = liveService.getById(id);
            if (data.getIsPublic() == 0) {
                throw new GwyException("课程暂未公开", "课程暂未公开", GwyConstant.BizCode.LESSON_NOT_PUBLIC.getCode());

            }
            Long teacherId = data.getTeacherId();
            UserEntity teacher = userService.getById(teacherId);


            //如果没有课程地址，那么就返回空的list，有就得到相应的地址
            String url = (data.getVideoUrl());
            if (StringUtils.isBlank(url)) {
                return R.ok().put("data", data).put("teacher", teacher).put("courseList", "");
            }
            //这里得到的是json格式的字符串

            //利用gson将json格式的字符串切割称VideoVo数组
            VideoVo[] a = gson.fromJson(url, VideoVo[].class);
            List<VideoVo> videoVoList = Arrays.asList(a);
            //得到符合前端需求的url(地址)数组
            ArrayList list = new ArrayList();
            for (VideoVo v : videoVoList) {
                list.add(v.getUrl());
            }
            return R.ok().put("data", data).put("teacher", teacher).put("courseList", list);
        }

    }

//    @RequestMapping("/polyv")
//    public R polyv(@RequestBody Map<String, String> params){
//        try {
//            return R.ok().put( "response",PolyvUtils.getResponseFromPolyv(params));
//        }catch (Exception e){
//            return R.error();
//        }
//
//    }
//    @RequestMapping("/listChannelVideo")
//    public R listChannelVideo(@RequestBody Map<String, String> params){
//        try {
//
//            LiveChannelVideoListResponse liveChannelVideoListResponse= PolyvUtils.listChannelVideo(params.get("channelId"));
//            return R.ok().put( "response",liveChannelVideoListResponse);
//        }catch (Exception e){
//            return R.error();
//        }
//
//    }
    @RequestMapping("/listChannelVideoLibrary")
    public R listChannelVideoLibrary(@RequestBody Map<String, String> params){
        try {
            LiveListChannelVideoLibraryResponse liveListChannelVideoLibraryResponse=PolyvUtils.listChannelVideoLibrary(params.get("channelId"),Integer.parseInt(params.get("page")));

            liveListChannelVideoLibraryResponse.getContents().forEach(c->{
                c.setFileUrl("");
                c.setWatchUrl("");
                c.setUrl("");
                c.setEnFileUrl("");

            });
            return R.ok().put( "response",liveListChannelVideoLibraryResponse);
        }catch (Exception e){
            return R.error();
        }

    }

    @RequestMapping("/config")
    public R polyvConfig(@RequestBody Map<String, String> params) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Map<String,String> res=new HashMap<>();
        res.put("app_secret", AesUtils.polyvAesEncrypt(Constant.APP_SECRET,AesUtils.SALT));
        res.put("appid",AesUtils.polyvAesEncrypt(Constant.APPID,AesUtils.SALT));
        return R.ok().put("config",res);
    }

}
