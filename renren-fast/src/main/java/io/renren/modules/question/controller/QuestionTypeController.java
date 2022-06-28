package io.renren.modules.question.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.renren.modules.question.service.QuestionService;
import io.renren.modules.question.service.QuestionTypeService;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
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

    @Autowired
    private QuestionService questionService;

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
        QuestionEntity questionEntity = questionService.getOne(
                new QueryWrapper<QuestionEntity>().eq("type_id", questionType.getParentId()).last("limit 1"));
        if (questionEntity != null) {
            return R.error("该分类下已有试题,不能添加子分类!");
        }
        questionTypeService.save(questionType);
        return R.ok();
    }

    /**
     * 更新顺序
     *
     * @param questionTypeEntities
     * @return
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody QuestionTypeEntity[] questionTypeEntities) {
        questionTypeService.updateBatchById(Arrays.asList(questionTypeEntities));
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody @Valid QuestionTypeEntity questionType) {
        questionTypeService.updateById(questionType);
        return R.ok();
    }


    /**
     * 批量删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        if (ids.length == 0) {
            return R.error("请先选择分类!");
        }
        QuestionEntity questionEntity = questionService.getOne(
                new QueryWrapper<QuestionEntity>().in("type_id", ids).last("limit 1"));
        if (questionEntity != null) {
            return R.error("不能删除已包含试题的分类!");
        }
        questionTypeService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 根据typeId(1为行测,2为申论,3,4为面试,5为综合,6为写作),将试题类型转为树形结构
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/tree/{typeId}")
    public R getQuestionTypeAsTree(@PathVariable Integer typeId) {
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(typeId);
        return R.ok().put("questionTypeOptions", questionTypeAsTree);
    }

}
