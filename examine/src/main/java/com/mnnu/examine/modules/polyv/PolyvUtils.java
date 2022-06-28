package com.mnnu.examine.modules.polyv;

import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.entity.RoomEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.polyv.util.HttpUtil;
import com.mnnu.examine.modules.polyv.util.LiveSignUtil;
import com.mnnu.examine.modules.polyv.util.VodSignUtil;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import net.polyv.common.v1.exception.PloyvSdkException;
import net.polyv.live.v1.constant.LiveConstant;
import net.polyv.live.v1.entity.channel.operate.LiveChannelSettingRequest;
import net.polyv.live.v1.entity.channel.operate.LiveCreateChannelTokenRequest;
import net.polyv.live.v1.entity.channel.playback.*;
import net.polyv.live.v1.entity.quick.QuickCreateChannelResponse;
import net.polyv.live.v1.entity.quick.QuickCreatePPTChannelRequest;
import net.polyv.live.v1.entity.web.auth.LiveUpdateChannelAuthRequest;
import net.polyv.live.v1.service.channel.impl.LiveChannelOperateServiceImpl;
import net.polyv.live.v1.service.channel.impl.LiveChannelPlaybackServiceImpl;
import net.polyv.live.v1.service.channel.impl.LiveChannelStateServiceImpl;
import net.polyv.live.v1.service.quick.impl.LiveChannelQuickCreatorServiceImpl;
//import net.polyv.vclass.v1.constant.VClassConstant;
//import net.polyv.vclass.v1.entity.lesson.VClassAddLessonRequest;
//import net.polyv.vclass.v1.entity.lesson.VClassAddLessonResponse;
//import net.polyv.vclass.v1.service.lesson.impl.VClassLessonServiceImpl;
import com.google.gson.Gson;
import net.polyv.live.v1.service.web.impl.LiveWebAuthServiceImpl;
import net.polyv.live.v2.entity.channel.state.LiveListChannelStreamStatusV2Request;
import net.polyv.live.v2.entity.channel.state.LiveListChannelStreamStatusV2Response;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mnnu.examine.modules.polyv.common.constant.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class PolyvUtils {

    @Autowired
    Gson gson;
    @Autowired
    LiveService liveService;
    public QuickCreateChannelResponse createRoom(UserEntity userEntity, RoomEntity roomEntity) {

        try {



            LiveEntity course = liveService.getBaseMapper().selectById(roomEntity.getLiveId());
            if(course==null)return  null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date startTime = format.parse(format.format(roomEntity.getStartTime()+180000));



            QuickCreatePPTChannelRequest quickCreatePPTChannelRequest = new QuickCreatePPTChannelRequest();
            QuickCreateChannelResponse quickCreateChannelResponse;
            quickCreatePPTChannelRequest.setName(roomEntity.getRoomName())
                    //频道相关基础设置-频道密码
                    .setChannelPasswd(userEntity.getPhone()+getRandomString(4))
                    .setLinkMicLimit(5)
                    .setPublisher(userEntity.getUsername())
                    //频道相关基础设置-是否无延迟
                    .setPureRtcEnabled(LiveConstant.Flag.YES.getFlag())
                    //频道相关基础设置-开播时间
                    .setStartTime(roomEntity.getStartTime()+180000)
                    //频道初始化设置-频道图标地址
                    .setCoverImg(course.getCoverUrl())
                    //频道初始化设置-引导图地址
                    .setSplashImg(course.getCoverUrl())
                    .setCoverImage(course.getCoverUrl())
                    //聊天室讲师信息-昵称
                    .setDesc(course.getDetails())
                    .setNickname(userEntity.getUsername())
                    .setActor("老师")
                    //聊天室讲师信息-讲师头像
                    .setAvatar(userEntity.getHeadPortrait())
                    ;

            quickCreateChannelResponse = new LiveChannelQuickCreatorServiceImpl().quickCreatePPTSence(quickCreatePPTChannelRequest);
            if (quickCreateChannelResponse  == null) {
                throw new Exception();
            }


            return quickCreateChannelResponse;



//            //封装API请求对象
//            VClassAddLessonRequest vClassAddLessonRequest = VClassAddLessonRequest.builder()
//                    .name(roomEntity.getRoomName())
//                    .startTime(startTime)
//                    .duration(Integer.parseInt(String.valueOf(roomEntity.getDuration() / 60)))
////                    .teacherId(teacherIdHash(roomEntity.getUserId()))
//                    .teacherId(userEntity.getPhone())
//                    .teacherMobile(userEntity.getPhone())
//                    .teacherName(userEntity.getUsername())
//                    .cover(course.getCoverUrl())
////                    .code(roomEntity.getRoomUuid().substring(0,16))
//                    .linkNumber(5)
//                    .watchCondition(VClassConstant.AuthType.DIRECT.getCode())
//                    .build();
//            //调用SDK请求保利威服务器
//            VClassAddLessonResponse vClassAddLessonResponse = new VClassLessonServiceImpl().addLesson(
//                    vClassAddLessonRequest);
//            if (vClassAddLessonResponse == null) {
//                throw new Exception();
//            }
//            ;
            //正常返回做B端正常的业务逻辑
            //to do something ......
//            System.out.println("课节创建成功 {}" + gson.toJson(vClassAddLessonResponse));
//            System.out.println(
//                    "老师上课地址：https://s.vclass.com/live-web-hi-class/teacher/login?lessonId={}， 老师登录账号：{}， 老师登录密码：{}" +
//                            vClassAddLessonResponse.getLessonId() + vClassAddLessonResponse.getTeacherMobile() +
//                            vClassAddLessonResponse.getTeacherPasswd());
//            System.out.println("学生上课地址：https://s.vclass.com/live-web-hi-class/student/login?lessonId={}" +
//                    vClassAddLessonResponse.getLessonId());


//            return vClassAddLessonResponse;
//
        }  catch (Exception e) {
            System.out.println("SDK调用异常" + e);

        }
        return null;
    }

    //讲师设置登录token 返回url
    public String tokenLogin(UserEntity userEntity, String channelId) throws Exception {
        LiveCreateChannelTokenRequest liveCreateChannelTokenRequest = new LiveCreateChannelTokenRequest();
        String teacherId=String.valueOf(userEntity.getPhone());

        Boolean liveCreateChannelTokenResponse;
        try {
            //准备测试数据
//            String channelId = super.createChannel();
            String token=LiveSignUtil.generateUUID();
            liveCreateChannelTokenRequest.setChannelId(channelId).setToken(token);
            liveCreateChannelTokenResponse = new LiveChannelOperateServiceImpl().createChannelToken(
                    liveCreateChannelTokenRequest);
//            Assert.assertNotNull(liveCreateChannelTokenResponse);
            String redirectUrl= URLEncoder.encode(String.format("https://live.polyv.net/web-start/?channelId=%s",channelId), "UTF-8");
            if (liveCreateChannelTokenResponse) {
                String res = String.format("https://live.polyv.net/teacher/auth-login?channelId=%s&token=%s&redirect=%s", channelId, token, redirectUrl);
                return res;
                //to do something ......
//                log.debug("设置频道单点登录token成功");
            }else {
                return null;
            }
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage(),B
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }


//        String teacherId=String.valueOf(userEntity.getPhone());
//        //公共参数,填写自己的实际参数
//        String appId = Constant.APPID;
//        String appSecret = Constant.APP_SECRET;
//        String timestamp = String.valueOf(System.currentTimeMillis());
//
//        //业务参数
//        String url = "http://api.polyv.net/hi-class-api/open/teach/v1/set-token";
////        String teacherId = "g3fdl05syf";
//        String token = LiveSignUtil.getRandomString(20);
//
//        //http 调用逻辑
//        Map<String, String> requestMap = new HashMap<>();
//        requestMap.put("appId", appId);
//        requestMap.put("timestamp", timestamp);
//        requestMap.put("sign", LiveSignUtil.getSign(requestMap, appSecret));
//
//        Map<String, Object> jsonMap = new HashMap<>();
//        jsonMap.put("teacherId", teacherId);
//        jsonMap.put("token", token);
//
//        url = HttpUtil.appendUrl(url, requestMap);
//        String response = HttpUtil.postJsonBody(url, gson.toJson(jsonMap), null);
//        System.out.println("测试讲师单点登录成功：{}" + response);
//        String res = String.format("https://s.vclass.com/live-web-hi-class/teacher/tokenLogin?teacherId=%s&token=%s&lessonId=%s", teacherId, token, lessonId);
////        String res = String.format("https://s.vclass.com/live-web-hi-class/teacher/tokenLogin?teacherId=%s&token=%s", teacherId, token);
//
//        return res;
        //do somethings
    }

    public String getStudentUrl(UserEntity userEntity, String lessonId,String secretKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String time =String.valueOf(System.currentTimeMillis()) ;
        String name = new Base64().encodeToString(userEntity.getUsername().getBytes());
        String userId=String.valueOf(userEntity.getId());
        String sign = "";
        sign=LiveSignUtil.md5Hex(secretKey + userId + secretKey + time);
       return String.format("https://live.polyv.cn/watch/%s?&userid=%s&ts=%s&sign=%s&nickname=%s", lessonId, userId, time, sign, name);
//        return String.format("https://live.polyv.cn/watch/%s?&userid=%s&ts=%s&sign=%s&nickname=%s&avatar=%s", lessonId, userId, time, sign, name,userEntity.getHeadPortrait());


//        return String.format("https://s.vclass.com/live-web-hi-class/student/tokenLogin?lessonId=%s&viewerId=%s&timestamp=%s&sign=%s&name=%s", lessonId, userId, time, sign, name);


    }
    public static String getRandomString(int length) {
        char [] ch= new char[length];
        for(int i =0;i<length;i++) {
            while(true) {
                char c = (char)(Math.random()*'z');
                if(Character.isDigit(c) || (Character.isLetter(c))) {
                    ch[i] = c;
                    break;//跳出自己所在的那层循环
                }else {
                    continue;//结束本次循环,会继续执行
                }
            }
        }
        String s = new String(ch);
        return s;
    }
    private  String teacherIdHash(String originId){
        return String.valueOf(Long.parseLong(originId)+20000000000L);
    }
    public String updateChannelAuth(String channelId) throws Exception {
        String SecretKey=getRandomString(32);
        LiveUpdateChannelAuthRequest liveUpdateChannelAuthRequest = new LiveUpdateChannelAuthRequest();
        Boolean liveUpdateChannelAuthResponse;
        try {
            LiveChannelSettingRequest.AuthSetting authSetting = new LiveChannelSettingRequest.AuthSetting().setAuthType(
                            LiveConstant.AuthType.DIRECT.getDesc())
                    .setRank(1)
                    .setEnabled("Y")
                    .setDirectKey(SecretKey);
//                    .setAuthCode("123456")
//                    .setQcodeTips("提示文案测试2")
//                    .setQcodeImg("https://live.polyv.net/static/images/live-header-logo.png");
            List<LiveChannelSettingRequest.AuthSetting> authSettings =
                    new ArrayList<LiveChannelSettingRequest.AuthSetting>();
            authSettings.add(authSetting);
            liveUpdateChannelAuthRequest.setChannelId(channelId)
                    .setAuthSettings(authSettings);
            liveUpdateChannelAuthResponse = new LiveWebAuthServiceImpl().updateChannelAuth(
                    liveUpdateChannelAuthRequest);
//            Assert.assertNotNull(liveUpdateChannelAuthResponse);
            if (liveUpdateChannelAuthResponse) {
//                log.debug("测试设置观看条件成功");
                return SecretKey;
            }else {
                return null;
            }
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage()
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }
    }

    public List<LiveListChannelStreamStatusV2Response> getLiveStatus(String[] ids) throws Exception, NoSuchAlgorithmException {
        LiveListChannelStreamStatusV2Request liveListChannelStreamStatusV2Request = new LiveListChannelStreamStatusV2Request();
        List<LiveListChannelStreamStatusV2Response> liveListChannelStreamStatusV2Respons;
        try {
            //准备测试数据
//            String channelIds = String.format("%s,%s",);
            StringBuffer channelIdsBuffer=new StringBuffer("");
            for (int i=0;i<ids.length-1;i++){
                channelIdsBuffer.append(ids[i]+",");
            }
            channelIdsBuffer.append(ids[ids.length]);
            String channelIds=channelIdsBuffer.toString();
            liveListChannelStreamStatusV2Request.setChannelIds(channelIds);
            liveListChannelStreamStatusV2Respons = new LiveChannelStateServiceImpl().listChannelLiveStreamV2(
                    liveListChannelStreamStatusV2Request);
//            Assert.assertNotNull(liveListChannelStreamStatusV2Respons);
            if (liveListChannelStreamStatusV2Respons != null) {
                return liveListChannelStreamStatusV2Respons;
                //to do something ......
//                log.debug("测试批量查询频道直播状态成功:{}", JSON.toJSONString(liveListChannelStreamStatusV2Respons));
            }
            else return null;
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage()
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }
    }
    //获取视频库列表
    public static LiveListChannelVideoLibraryResponse listChannelVideoLibrary(String channelId,int currentPage) throws Exception, NoSuchAlgorithmException {
        LiveListChannelVideoLibraryRequest liveListChannelVideoLibraryRequest =
                new LiveListChannelVideoLibraryRequest();
        LiveListChannelVideoLibraryResponse liveListChannelVideoLibraryResponse;
        try {
            //playback-回放列表,vod-点播列表;默认普通直播场景为vod，三分屏为playback
            liveListChannelVideoLibraryRequest.setChannelId(channelId)
                    .setListType("vod")
                    .setCurrentPage(currentPage);

            liveListChannelVideoLibraryResponse = new LiveChannelPlaybackServiceImpl().listChannelVideoLibrary(
                    liveListChannelVideoLibraryRequest);
//            Assert.assertNotNull(liveListChannelVideoLibraryResponse);
            if (liveListChannelVideoLibraryResponse != null) {
                return liveListChannelVideoLibraryResponse;
                //to do something ......
//                log.debug("测试查询视频库列表成功{}", JSON.toJSONString(liveListChannelVideoLibraryResponse));
            }
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage()
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }
        return null;
    }

    //查询频道录制视频信息
    public static LiveChannelVideoListResponse listChannelVideo(String channelId) throws Exception, NoSuchAlgorithmException {
        LiveChannelVideoListRequest liveChannelVideoListRequest = new LiveChannelVideoListRequest();
        LiveChannelVideoListResponse liveChannelVideoListResponse;
        try {
            liveChannelVideoListRequest.setChannelId(channelId);
//                    .setStartDate(getDate(2020, 1, 1))
//                    .setEndDate(getDate(2020, 10, 14))
//                    .setSessionIds("fs4j2z8wcb,fs8p8eso44");
            liveChannelVideoListResponse = new LiveChannelPlaybackServiceImpl().listChannelVideo(
                    liveChannelVideoListRequest);
//            Assert.assertNotNull(liveChannelVideoListResponse);
            if (liveChannelVideoListResponse != null) {
                //to do something ......
//                log.debug("查询频道录制视频信息成功{}", JSON.toJSONString(liveChannelVideoListResponse));
            }
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage()
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }
        return null;
    }

    //频道回放设置
    public Boolean updateChannelPlaybackSetting(String channelId) throws Exception, NoSuchAlgorithmException {
        LiveChannelPlaybackSettingRequest liveChannelPlaybackSettingRequest;
        Boolean liveChannelPlaybackSettingResponse;
        try {

            //videoId可通过new LiveChannelPlaybackServiceImpl().listChannelVideoLibrary()获取
//            List<String> videoIds = listChannelVideoIds(channelId);
            liveChannelPlaybackSettingRequest = new LiveChannelPlaybackSettingRequest();
            liveChannelPlaybackSettingRequest.setChannelId(channelId)
                    .setPlaybackEnabled("Y")
                    .setType("list")
                    .setOrigin("playback");
//                    .setVideoId(videoIds.get(0));
            liveChannelPlaybackSettingResponse = new LiveChannelPlaybackServiceImpl().updateChannelPlaybackSetting(
                    liveChannelPlaybackSettingRequest);
//            Assert.assertNotNull(liveChannelPlaybackSettingResponse);
            if (liveChannelPlaybackSettingResponse) {
                //to do something ......
//                log.debug("设置频道回放设置成功");
                return liveChannelPlaybackSettingResponse;
            }
        } catch (PloyvSdkException e) {
            //参数校验不合格 或者 请求服务器端500错误，错误信息见PloyvSdkException.getMessage()
//            log.error(e.getMessage(), e);
            // 异常返回做B端异常的业务逻辑，记录log 或者 上报到ETL 或者回滚事务
            throw e;
        } catch (Exception e) {
//            log.error("SDK调用异常", e);
            throw e;
        }
        return false;
    }

        public static String getResponseFromPolyv(Map<String, String> requestMap ) throws Exception, NoSuchAlgorithmException {
            //公共参数,填写自己的实际参数
            String appSecret = Constant.APP_SECRET;
            String userId = Constant.USERID;
            String appId=Constant.APPID;;
            String timestamp=String.valueOf(System.currentTimeMillis());

            //业务参数
            //http 调用逻辑
            requestMap.put("appId", appId);
            requestMap.put("timestamp",timestamp);
            requestMap.put("sign",LiveSignUtil.getSign(requestMap, appSecret));
            if(requestMap.get("method").equals("post")||requestMap.get("method").equals("POST"))
            return  HttpUtil.postFormBody(requestMap.get("url"), requestMap);
            else return  HttpUtil.get(requestMap.get("url"), requestMap);

        }
}

