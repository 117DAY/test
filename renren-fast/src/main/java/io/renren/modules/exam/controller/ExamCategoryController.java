package io.renren.modules.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.exam.service.ExamCategoryService;
import io.renren.modules.exam.service.ExamService;
import io.renren.modules.exam.vo.ExamCategoryVO;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.service.QuestionService;
import io.renren.modules.question.service.QuestionTypeService;
import io.renren.modules.sys.entity.SysMenuEntity;
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
@RequestMapping("exam/examcategory")
public class ExamCategoryController {
    @Autowired
    private ExamCategoryService examCategoryService;

    @Autowired
    private ExamService examService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = examCategoryService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取试卷类型的树形结构
     *
     * @return
     */
    @RequestMapping("/tree")
    public R getCateAsTree(@RequestBody Long excludeId) {
        List<ExamCategoryEntity> list = examCategoryService.list();

        if (!excludeId.equals(-1L)) {
            list = list.stream().filter(
                    e -> !excludeId.equals(e.getId())).collect(Collectors.toList());
        }
        List<ExamCategoryEntity> collect = list.stream().sorted(
                Comparator.comparingInt(ExamCategoryEntity::getSort)).collect(Collectors.toList());
        List<ExamCategoryVO> categoryVOS = examCategoryService.withTree(collect);
        return R.ok().put("data", categoryVOS);
    }

    /**
     * 根据分类id获取根到该id的路径
     *
     * @param id
     * @return
     */
    @RequestMapping("/path/{id}")
    public R getPathById(@PathVariable Integer id) {
        List<ExamCategoryEntity> list = examCategoryService.list();
        ExamCategoryEntity categoryEntity = examCategoryService.getById(id);
        List<Long> path = examCategoryService.getPathById(new ArrayList<>(), list, categoryEntity);
        Collections.reverse(path);
        return R.ok().put("path", path);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamCategoryEntity examCategory = examCategoryService.getById(id);

        return R.ok().put("examCategory", examCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody @Valid ExamCategoryEntity examCategory) {
        ExamEntity examEntity = examService.getOne(
                new QueryWrapper<ExamEntity>().eq("category_id", examCategory.getParentId()).last("limit 1"));
        if (examEntity != null) {
            return R.error("该分类下已有试卷,不能添加子分类!");
        }
        examCategoryService.save(examCategory);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamCategoryEntity examCategory) {
        examCategoryService.updateById(examCategory);
        return R.ok();
    }

    /**
     * 更新顺序
     *
     * @param examCategoryEntities
     * @return
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody ExamCategoryEntity[] examCategoryEntities) {
        examCategoryService.updateBatchById(Arrays.asList(examCategoryEntities));
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
        ExamEntity examEntity = examService.getOne(
                new QueryWrapper<ExamEntity>().in("category_id", ids).last("limit 1"));
        if (examEntity != null) {
            return R.error("不能删除已包含试卷的分类!");
        }
        examCategoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
