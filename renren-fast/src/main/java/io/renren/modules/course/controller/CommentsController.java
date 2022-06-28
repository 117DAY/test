package io.renren.modules.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.course.entity.CommentsEntity;
import io.renren.modules.course.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @RequestMapping("/pass")
    public R pass(@RequestBody Long[] ids){
        for (Long l:
             ids) {
            CommentsEntity commentsEntity=new CommentsEntity();
            commentsEntity.setId(l);
            commentsEntity.setIsPublic(1);

            commentsService.updateById(commentsEntity);
        }

        return R.ok();
    }

}
