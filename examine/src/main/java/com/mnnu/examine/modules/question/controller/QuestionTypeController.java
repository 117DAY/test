package com.mnnu.examine.modules.question.controller;


import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;
import com.mnnu.examine.modules.question.service.QuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@RestController
@RequestMapping("question/questiontype")
public class QuestionTypeController {
    @Autowired
    private QuestionTypeService questionTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = questionTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        QuestionTypeEntity questionType = questionTypeService.getById(id);

        return R.ok().put("questionType", questionType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody QuestionTypeEntity questionType) {
        questionTypeService.save(questionType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody QuestionTypeEntity questionType) {
        questionTypeService.updateById(questionType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        questionTypeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 根据typeId(1为行测,2为申论,3为面试,4为综合,5为写作),将试题类型转为树形结构
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/tree/{typeId}")
    public R getQuestionTypeAsTree(@PathVariable Integer typeId) {
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(typeId);
        if (typeId.equals(0)) {
            //设置根节点
            List<QuestionTypeEntity> list = new ArrayList<>();
            QuestionTypeEntity questionType = new QuestionTypeEntity();
            questionType.setId(0);
            questionType.setTypeName("所有分类");
            questionType.setParentId(-1);
            questionType.setSort(0);
            questionType.setChildren(questionTypeAsTree);
            list.add(questionType);
            return R.ok().put("questionTypeOptions", list);
        }
        return R.ok().put("questionTypeOptions", questionTypeAsTree);
    }

}
