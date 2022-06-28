package io.renren.common.utils.agora;

import io.renren.common.utils.agora.rtm.RtmTokenBuilder;

public class RtmBuilder {
    private static String appId = "9d005cec3baa4c359242c25fef069ef6";
    private static String appCertificate = "c5be3b0121034d8db4a6eb19f7dbaac8";
    private static int expireTimestamp = 0;

    public static String getRtmToken(String userId) {
        RtmTokenBuilder token = new RtmTokenBuilder();
        String result = null;
        try {
            result = token.buildToken(appId, appCertificate, userId, RtmTokenBuilder.Role.Rtm_User, expireTimestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
