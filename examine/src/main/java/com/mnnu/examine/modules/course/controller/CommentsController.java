package com.mnnu.examine.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.entity.CommentsEntity;
import com.mnnu.examine.modules.course.service.CommentsService;
import com.mnnu.examine.modules.course.vo.CommentsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-25 15:55:18
 */
@RestController
@RequestMapping("course/comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CommentsEntity comments = commentsService.getById(id);

        return R.ok().put("comments", comments);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CommentsEntity comments){
		commentsService.save(comments);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CommentsEntity comments){
		commentsService.updateById(comments);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		commentsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/comment")
    public R getComment(@RequestParam("id") Integer id, @RequestParam("type") Integer type,@RequestParam("page") Integer page,@RequestParam("limit") Integer limit)  throws Throwable {

        Map list=commentsService.getCommentsWithRootAndSon(id,type,page,limit);
        return R.ok().put("data", list);
    }

    @RequestMapping("/insertComment")
    public R insertComment(@RequestParam("id") Long id, @RequestParam("type") Integer type,@RequestParam("content") String content) throws Throwable {
//        ArrayList<CommentsVo> list=commentsService.getCommentsWithRootAndSon(id,type);
        if (commentsService.insertLessonComment(id,type,content)>0){
            return R.ok();
        }else {
            return R.error();
        }

    }

    @RequestMapping("/insertCommentWithReply")
    public R insertCommentWithReply(@RequestParam Map<String,Object> params) throws Throwable {

        if (commentsService.insertLessonCommentWithReply(params)>0){
            return R.ok();
        }else {
            throw new GwyException("评论时出现异常，请重新评论", "评论时出现异常，请重新评论", GwyConstant.BizCode.COMMENT_ERROR.getCode());

        }

    }

}
