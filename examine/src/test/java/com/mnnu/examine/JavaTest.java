package com.mnnu.examine;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.AesUtils;
import com.mnnu.examine.modules.live.entity.RoomEntity;
import com.mnnu.examine.modules.live.service.impl.LiveServiceImpl;
import com.mnnu.examine.modules.polyv.PolyvInitiation;
import com.mnnu.examine.modules.polyv.PolyvUtils;
import net.polyv.live.v1.config.LiveGlobalConfig;
import net.polyv.live.v1.constant.LiveConstant;
import net.polyv.live.v1.entity.channel.operate.LiveCreateSonChannelListRequest;
import net.polyv.live.v1.entity.channel.playback.LiveChannelVideoListResponse;
import net.polyv.live.v1.entity.channel.playback.LiveListChannelVideoLibraryResponse;
import net.polyv.live.v1.entity.channel.viewdata.LiveListChannelViewlogRequest;
import net.polyv.live.v1.entity.quick.QuickCreateChannelResponse;
import net.polyv.live.v1.entity.quick.QuickCreatePPTChannelRequest;
import net.polyv.live.v1.service.quick.impl.LiveChannelQuickCreatorServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class JavaTest {
    @Test
    public void test() {
        BigDecimal bigDecimal = new BigDecimal("0.21");
        System.out.println("value=" + bigDecimal);
    }

    @Test
    public void test1() {
        Gson gson = new Gson();
        video video = new video();
        video.setName("huawei");
        video.setIndex("2");
        String[] strings = new String[2];
        strings[0] = "huawie";
        strings[1] = "xiaomi";
        video.setUrl(strings);
        System.out.println(video.toString());
        System.out.println(gson.toJson(video));
    }

    /**
     * 快速创建带子频道的三分屏频道，适用于直播教学场景
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException 系统异常
     */
//    @Test
//    public void testQuickCreatePPTAndSonChannel() throws IOException, NoSuchAlgorithmException {
//        //初始化系统，请配置自己的账号信息，正式使用时请全局统一设置
//        String userId = "xxx";
//        String appId = "xxx";
//        String appSecret = "xxx";
//        LiveGlobalConfig.init(appId, userId, appSecret);
//        log.debug("--初始化完成--");
//
//        QuickCreatePPTChannelRequest quickCreatePPTChannelRequest = new QuickCreatePPTChannelRequest();
//        QuickCreateChannelResponse quickCreateChannelResponse;
////        String path = LiveChannelQuickCreatorTest.class.getResource("/file/PPT.pptx").getPath();
////        Calendar instance = Calendar.getInstance();
////        instance.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH) + 1);
//        //创建频道
//        //频道相关基础设置-频道名
//        quickCreatePPTChannelRequest.setName("带子频道的直播教学场景")
//                //频道相关基础设置-频道密码
////                .setChannelPasswd(getRandomString(6))
//                //频道相关基础设置-连麦人数
//                .setLinkMicLimit(5)
//                //频道相关基础设置-主持人名称
//                .setPublisher("thomas教授")
//                //频道相关基础设置-是否无延迟
//                .setPureRtcEnabled(LiveConstant.Flag.YES.getFlag())
//                //频道相关基础设置-开播时间
//                .setStartTime(instance.getTime().getTime())
//                //==========================================
//                //频道初始化设置-频道图标地址
//                .setCoverImg("https://wwwimg.polyv.net/assets/dist/images/v2020/page-home/brand-advantage/row-2-3.svg")
//                //频道初始化设置-引导图地址
//                .setSplashImg(
//                        "https://wwwimg.polyv.net/assets/dist/images/v2020/news-info-md/product-dynamic-bg_v3.jpg")
//                //频道初始化设置-频道描述
//                .setDesc("POLYV保利威是广州易方信息科技股份有限公司旗下拥有自主知识产权的视频云计算服务平台，其中包含 云点播 、云直播 " +
//                        "和其它视频服务，提供API、SDK技术支持，并拥有国家专利级别的PlaySafe®视频版权保护技术及三套CDN加速，致力为用户提供稳定、安全、快速的企业级视频云服务。")
//                //频道初始化设置-设置暖场图
//                .setCoverImage("https://s1.videocc.net/live-watch/assets/img/default-splash-img.07657078.jpg")
//                //频道初始化设置-点击暖场图跳转的地址
//                .setCoverHref("http://www.baidu.com")
//                //频道初始化设置-设置暖场视频
////              .setWarmUpFlv("http://www.w3school.com.cn/example/html5/mov_bbb.mp4")
//                //==========================================
//                //聊天室讲师信息-昵称
//                .setNickname("thomas-gogo")
//                //聊天室讲师信息-讲师头衔
//                .setActor("刘老师")
//                //聊天室讲师信息-讲师头像
//                .setAvatar(
//                        "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2069606413,3553249962&fm=26&gp=0" +
//                                ".jpg")
//                //==========================================
//                // 讲课文档设置-讲课文档，当前支持DOC、PPT、PDF
//                .setFile(new File(path))
//                //讲课PPT设置-转换类型（‘common’：转普通图片， ‘animate’：转动画效果）
//                .setType("common")
//                //讲课PPT设置-文档名称
//                .setDocName("直播教学课件");
////                //讲课PPT设置-文档转换完成后的回调地址，不需要不传
////                .setCallbackUrl("http://www.baidu.com/callback")
//
//
//        LiveCreateSonChannelListRequest liveCreateSonChannelListRequest = new LiveCreateSonChannelListRequest();
//        List<LiveCreateSonChannelListRequest.SonChannel> sonChannels = new ArrayList<LiveCreateSonChannelListRequest.SonChannel>();
//        sonChannels.add(setSonChannelsInfo1());
//        sonChannels.add(setSonChannelsInfo2());
//        liveCreateSonChannelListRequest.setSonChannels(sonChannels);
//
//        quickCreateChannelResponse = new LiveChannelQuickCreatorServiceImpl().quickCreatePPTSence(quickCreatePPTChannelRequest,
//                liveCreateSonChannelListRequest);
//        Assert.assertNotNull(quickCreateChannelResponse);
//        log.debug("快速创建三分屏频道成功，{}", JSON.toJSONString(quickCreateChannelResponse));
//        log.debug("网页开播地址：https://live.polyv.net/web-start/login?channelId={}  , 登录密码： {}",
//                quickCreateChannelResponse.getLiveChannelBasicInfoResponse().getChannelId(),
//                quickCreatePPTChannelRequest.getChannelPasswd());
//        log.debug("网页观看地址：https://live.polyv.cn/watch/{} ",
//                quickCreateChannelResponse.getLiveChannelBasicInfoResponse().getChannelId());
//        log.debug("嘉宾进入直播间地址：http://live.polyv.net/web-start/guest?channelId={} ,登录密码： {} ",
//                quickCreateChannelResponse.getSonChannelInfos().get(0).getAccount(),
//                sonChannels.get(0).getPasswd());
//        log.debug("助教进入直播间地址：https://live.polyv.net/teacher.html , 登录频道: {}, 登录密码： {}",
//                quickCreateChannelResponse.getSonChannelInfos().get(1).getAccount(),
//                sonChannels.get(1).getPasswd());
//        /**
//         * todo : B端客户的业务逻辑，将quickCreateChannelResponse的相关信息保持到自己的DB中组织业务逻辑
//         */
//
//        /**
//         * todo : 采用网页开播或者客户端开播，直播结束后 ，可以拉取用户观看直播的观看数据，对观看效果做进一步的分析，改进直播流程和细节
//         */
//        //打印观看日志
//        printViewLog(quickCreateChannelResponse.getLiveChannelBasicInfoResponse().getChannelId());
//    }

//    /**
//     * 设置子频道信息-嘉宾
//     * @return 子频道列表
//     */
//    private LiveCreateSonChannelListRequest.SonChannel setSonChannelsInfo1() {
//        LiveCreateSonChannelListRequest.SonChannel sonChannel1 = new LiveCreateSonChannelListRequest.SonChannel();
//        //设置子频道信息，子频道代表助教、嘉宾信息
//        sonChannel1
//                //子频道角色-默认不传为助教，传Guest为嘉宾
//                .setRole("Guest")
//                //子频道昵称
//                .setNickname("嘉宾-陈先生")
//                //子频道登录密码
////                .setPasswd(getRandomString(10))
//                //子频道头衔
//                .setActor("教授")
//                //子频道头像
//                .setAvatar(
//                        "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2480846186,1530344&fm=15&gp=0.jpg");
//        return sonChannel1;
//    }
//
//    /**
//     * 设置子频道信息-助教
//     * @return 子频道列表
//     */
//    private LiveCreateSonChannelListRequest.SonChannel setSonChannelsInfo2() {
//        LiveCreateSonChannelListRequest.SonChannel sonChannel2 = new LiveCreateSonChannelListRequest.SonChannel();
//        sonChannel2.setRole(null)
//                .setNickname("助教-王小姐")
//                .setPasswd(getRandomString(10))
//                .setActor("王老师")
//                .setAvatar("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=356414612,1103487565&fm=15&gp=0" +
//                        ".jpg");
//        return sonChannel2;
//    }
//
//    /**
//     * 打印频道观看日志
//     * @param channelId
//     * @throws IOException
//     * @throws NoSuchAlgorithmException
//     */
//    private void printViewLog(String channelId) throws IOException, NoSuchAlgorithmException {
//        LiveListChannelViewlogRequest liveListChannelViewlogRequest = new LiveListChannelViewlogRequest();
//        LiveListChannelViewlogResponse liveListChannelViewlogResponse;
//        Calendar instance = Calendar.getInstance();
//        instance.set(Calendar.DAY_OF_MONTH, instance.get(Calendar.DAY_OF_MONTH) + 2);
//        //依据频道号和起止时间查询观看日志
//        liveListChannelViewlogRequest.setChannelId(channelId)
//                .setStartTime(new Date())
//                .setEndTime(instance.getTime());
//        liveListChannelViewlogResponse = new LiveChannelViewdataServiceImpl().listChannelViewlog(
//                liveListChannelViewlogRequest);
//        Assert.assertNotNull(liveListChannelViewlogResponse);
//        if (liveListChannelViewlogResponse != null) {
//            //to do something ......
//            log.debug("测试分页查询频道观看日志成功，{}", JSON.toJSONString(liveListChannelViewlogResponse));
//        }
//    }

    @Autowired
    LiveServiceImpl liveServiceImpl;
    @Test
    public void polyVtest() throws Exception {
        PolyvInitiation.initPolyvLive();
        PolyvUtils polyvUtils=new PolyvUtils();
        System.out.println(polyvUtils.updateChannelPlaybackSetting("2977665"));
//        LiveChannelVideoListResponse liveChannelVideoListResponse=polyvUtils.listChannelVideo("2977665");
//        LiveListChannelVideoLibraryResponse liveListChannelVideoLibraryResponse = polyvUtils.listChannelVideoLibrary("2977665", 1);
//        liveListChannelVideoLibraryResponse.getContents().forEach(c->{
//            System.out.println(c.getUrl());
//            System.out.println(c.getWatchUrl());
//
//            System.out.println(c.getFileUrl());
//                }
//
//
//        );
        //        RoomEntity room=new RoomEntity();
//        room.setDuration(600L);
//        room.setRoomName("test");
////        String time =String.valueOf() ;
//
//        room.setStartTime(System.currentTimeMillis()/1000);
//        room.setLiveId("12");
//        String roomUuid = AesUtils.encrypt(room.getLiveId()+room.getStartTime(), AesUtils.SALT).replaceAll("/", "");
//        room.setRoomUuid(roomUuid);
//        polyvUtils.createRoom(room);
//
//        Map<String,Object> par=new HashMap<String,Object>(){{
//            put("liveId","12");
//            put("isCreate",1);
//        }};
//        LiveServiceImpl liveServiceImpl=new LiveServiceImpl();
//        Map<String, Object>res= liveServiceImpl.getRoomInfo(par,59L);
//        System.out.println("ks");

    }
}

class video {
    String name;
    String index;
    String[] url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        video video = (video) o;
        return Objects.equals(name, video.name) && Objects.equals(index, video.index) && Arrays.equals(url, video.url);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, index);
        result = 31 * result + Arrays.hashCode(url);
        return result;
    }

    @Override
    public String toString() {
        return "video{" +
                "name='" + name + '\'' +
                ", index='" + index + '\'' +
                ", url=" + Arrays.toString(url) +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }
}

