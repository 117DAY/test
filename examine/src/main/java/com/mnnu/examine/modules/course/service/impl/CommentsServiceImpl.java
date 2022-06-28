package com.mnnu.examine.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.course.dao.CommentsDao;
import com.mnnu.examine.modules.course.entity.CommentsEntity;
import com.mnnu.examine.modules.course.service.CommentsService;
import com.mnnu.examine.modules.course.vo.CommentsVo;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qiaoh
 */
@Service("commentsService")
public class CommentsServiceImpl extends ServiceImpl<CommentsDao, CommentsEntity> implements CommentsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentsEntity> page = this.page(
                new Query<CommentsEntity>().getPage(params),
                new QueryWrapper<CommentsEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map getCommentsWithRootAndSon(Integer id,Integer type,Integer page,Integer limit) {
        //设置查寻条件
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("course_id",id);
        wrapper.eq("course_type",type);
        wrapper.eq("root_comment",0);
        wrapper.eq("is_public",1);
        wrapper.orderByDesc("time");

        //分页
        Page<CommentsEntity> p = new Page<>(page, limit);
        Page<CommentsEntity> page1=getBaseMapper().selectPage(p,wrapper);
        //得到父评论
        ArrayList<CommentsEntity> list= page1.getRecords().size()==0?new ArrayList<CommentsEntity>():(ArrayList<CommentsEntity>) page1.getRecords();

        //父评论+子评论
        ArrayList list1=new ArrayList();
        //查子评论
        for (CommentsEntity c:list) {
            CommentsVo commentsVo=new CommentsVo();
            commentsVo.setRoot(c);
            ArrayList list2=new ArrayList();
            Long rootId=c.getId();
            QueryWrapper wrapper1=new QueryWrapper();
            wrapper1.eq("course_id",id);
            wrapper1.eq("course_type",type);
            wrapper1.eq("root_comment",rootId);
            wrapper1.eq("is_public",1);
            wrapper1.orderByDesc("time");
            list2.addAll(getBaseMapper().selectList(wrapper1));
            commentsVo.setSon(list2);
            list1.add(commentsVo);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("data",list1);
        map.put("total",page1.getTotal());
        map.put("current",page1.getCurrent());

        return map;

    }

    @Override
    public int insertLessonComment(Long id, Integer type, String content) {

        content=content.trim();
        if(content.length()==0){
            throw new GwyException("评论内容不能为空", "评论内容不能为空", GwyConstant.BizCode.COMMENT_NO_EXIST.getCode());
        }
        Long commentId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String commentName=((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time=sdf.format(date);
        CommentsEntity commentsEntity=new CommentsEntity();
        commentsEntity.setCourseId(id);
        commentsEntity.setCourseType(type);
        commentsEntity.setCommentId(commentId);
        commentsEntity.setCommentName(commentName);
        commentsEntity.setContent(content);
        commentsEntity.setTime(time);
        commentsEntity.setShowStatus(1);
        commentsEntity.setIsPublic(0);
        commentsEntity.setRootComment(0L);
        int success=getBaseMapper().insert(commentsEntity);
        return success;
    }

    @Override
    public int insertLessonCommentWithReply(Map<String, Object> params) {
        Long id=Long.parseLong((String) params.get("id"));
        Integer type= Integer.valueOf((String) params.get("type"));
        String content= (String) params.get("content");
        content=content.trim();
        if(content.length()==0){
            throw new GwyException("评论内容不能为空", "评论内容不能为空", GwyConstant.BizCode.COMMENT_NO_EXIST.getCode());
        }
        Long replyId=Long.parseLong((String) params.get("replyId"));
        String replyName= (String) params.get("replyName");
        Long rootComment=Long.parseLong((String) params.get("rootComment"));

        Long commentId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String commentName=((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time=sdf.format(date);
        CommentsEntity commentsEntity=new CommentsEntity();
        commentsEntity.setCourseId(id);
        commentsEntity.setCourseType(type);
        commentsEntity.setCommentId(commentId);
        commentsEntity.setReplyId(replyId);
        commentsEntity.setCommentName(commentName);
        commentsEntity.setReplyName(replyName);
        commentsEntity.setContent(content);
        commentsEntity.setTime(time);
        commentsEntity.setShowStatus(1);
        commentsEntity.setIsPublic(0);
        commentsEntity.setRootComment(rootComment);
        int success=getBaseMapper().insert(commentsEntity);
        return success;
    }

}
