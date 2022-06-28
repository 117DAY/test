package com.mnnu.examine.modules.polyv;
import com.mnnu.examine.modules.polyv.common.constant.*;
import net.polyv.live.v1.config.LiveGlobalConfig;
//import net.polyv.vclass.v1.config.VClassGlobalConfig;

public class PolyvInitiation {
    /**
     * 初始化配置并初始化 HTTP CLIENT 连接池超时时间和最大连接数配置   账号信息
     */

    public static void initPolyvLive(){

        Integer timeOut = 20000;  //HTTP CLIENT 连接池超时时间
        Integer maxClientNum = 100;  //HTTP CLIENT 最大连接数
        LiveGlobalConfig.init(Constant.APPID,  Constant.USERID,  Constant.APP_SECRET,  timeOut ,  maxClientNum);
//        log.debug("--初始化完成--");
    }
}
