package io.renren.modules.question.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.question.entity.QuestionTextRelationEntity;
import io.renren.modules.question.service.QuestionTextRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 材料和(申论)试题关系表
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-01 22:30:37
 */
@RestController
@RequestMapping("modules/questiontextrelation")
public class QuestionTextRelationController {
    @Autowired
    private QuestionTextRelationService questionTextRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = questionTextRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		QuestionTextRelationEntity questionTextRelation = questionTextRelationService.getById(id);

        return R.ok().put("questionTextRelation", questionTextRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QuestionTextRelationEntity questionTextRelation){
		questionTextRelationService.save(questionTextRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QuestionTextRelationEntity questionTextRelation){
		questionTextRelationService.updateById(questionTextRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		questionTextRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
