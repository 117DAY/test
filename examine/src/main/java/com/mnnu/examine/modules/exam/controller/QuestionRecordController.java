package com.mnnu.examine.modules.exam.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.exam.entity.QuestionRecordEntity;
import com.mnnu.examine.modules.exam.service.QuestionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
@RestController
@RequestMapping("exam/questionrecord")
public class QuestionRecordController {
    @Autowired
    private QuestionRecordService questionRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		QuestionRecordEntity questionRecord = questionRecordService.getById(id);

        return R.ok().put("questionRecord", questionRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QuestionRecordEntity questionRecord){
		questionRecordService.save(questionRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QuestionRecordEntity questionRecord){
		questionRecordService.updateById(questionRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		questionRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
