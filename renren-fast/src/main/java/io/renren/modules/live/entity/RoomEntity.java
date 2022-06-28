package io.renren.modules.live.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class RoomEntity {
    String roomUuid;
    String userId;//老师ID
    String beginTime;
    String roomName;
    String LiveId;//直播课程ID
    Long startTime;
    Long duration;
    //后台特有
    String rtmToken;
    public RoomEntity() {
    }
}
