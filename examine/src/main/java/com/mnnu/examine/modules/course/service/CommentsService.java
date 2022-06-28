package com.mnnu.examine.modules.course.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.course.entity.CommentsEntity;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-25 15:55:18
 */
public interface CommentsService extends IService<CommentsEntity> {

    PageUtils queryPage(Map<String, Object> params);

     Map getCommentsWithRootAndSon(Integer id,Integer type,Integer page,Integer limit);

    int insertLessonComment(Long id,Integer type,String content);

    int insertLessonCommentWithReply(Map<String, Object> params);
}

