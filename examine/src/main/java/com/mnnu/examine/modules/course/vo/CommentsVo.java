package com.mnnu.examine.modules.course.vo;

import com.mnnu.examine.modules.course.entity.CommentsEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentsVo {
    /**
     * 根评论
     */
    private CommentsEntity root;
    /**
     * 子评论
     */
    private ArrayList<CommentsEntity> son;
}
