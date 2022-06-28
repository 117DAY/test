package com.mnnu.examine.modules.live.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.teacher.entity.TeacherInfoEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Data
public class RoomEntity {
    String roomUuid;
    String userId;//老师ID
    String beginTime;
    String roomName;
    String LiveId;//直播课程ID
    Long startTime;
    Long duration;
    String teacherName;
    Long polyvLessonId;
    String polyvSecretKey;

    @JsonIgnore
    public RoomEntity getNoIdEntity(){
        this.setRoomUuid("");
        return this;
    }

    public RoomEntity() {
    }
}
