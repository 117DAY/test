package com.mnnu.examine.modules.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.exam.entity.ExamEntity;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.service.ExamRecordService;
import com.mnnu.examine.modules.exam.service.ExamService;
import com.mnnu.examine.modules.exam.vo.ExamLineTestVO;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import com.mnnu.examine.modules.exam.vo.ExamRecordVO;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;
import com.mnnu.examine.modules.question.service.QuestionService;
import com.mnnu.examine.modules.question.service.QuestionTypeService;
import com.mnnu.examine.modules.question.vo.QuestionLineTestVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@RestController
@RequestMapping("exam/examrecord")
public class ExamRecordController {
    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionTypeService questionTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = examRecordService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamRecordEntity examRecord = examRecordService.getById(id);
        return R.ok().put("examRecord", examRecord);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamRecordEntity examRecord) {
        examRecordService.save(examRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamRecordEntity examRecord) {
        examRecordService.updateById(examRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        examRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 获取排行
     *
     * @param examId
     * @return
     */
    @RequestMapping("/rank/{examId}")
    public R getRank(@PathVariable Long examId) {
        List<ExamRankVO> ranksByExam = examRecordService.getRanksByExam(examId);
        return R.ok().put("rankList", ranksByExam);
    }

    /**
     * 查询行测试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/get/record/{recordId}")
    public R getLineTest(@PathVariable Integer recordId) {
        ExamRecordEntity examRecordEntity = examRecordService.getById(recordId);
        ExamRecordVO examRecordVO = new ExamRecordVO();
        BeanUtils.copyProperties(examRecordEntity, examRecordVO);
        String userAnswers = examRecordEntity.getUserAnswers();
        examRecordVO.setUserAnswers(Arrays.asList(userAnswers.split("&")));

        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        //1.查询试卷
        ExamEntity examEntity = examService.getById(examRecordEntity.getExamId());
        //2.查询试卷对应的试题列表
        List<QuestionEntity> questionList = questionService.list(
                new QueryWrapper<QuestionEntity>().eq("exam_id", examEntity.getId()));
        //3.将试题列表按照sort排序,以及type_id分类(必须保持原有的顺序)
        Map<Integer, List<QuestionEntity>> groupedQuestions = questionList.stream().sorted(
                Comparator.comparingInt(QuestionEntity::getSort)).collect(
                Collectors.groupingBy(QuestionEntity::getTypeId, LinkedHashMap::new, Collectors.toList()));
        //4.查询所有试题类型
        List<QuestionTypeEntity> typeList = questionTypeService.list();

        //5.设置暂存map,用于保存最终分类的题目信息
        Map<QuestionTypeEntity, List<QuestionEntity>> map = new LinkedHashMap<>();
        for (Integer integer : groupedQuestions.keySet()) {
            //6.遍历分好类的map,在所有的试题分类中查找出一个试题分类的id为试题的公共id
            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(integer)).findFirst().get();
            //7.获取分类的路径以及根分类
            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);
            QuestionTypeEntity root = path.remove(0);
            List<Object> collect = path.stream().map(QuestionTypeEntity::getTypeName).collect(Collectors.toList());

            //8.将路径赋予各问题
            List<QuestionEntity> list = groupedQuestions.get(integer).stream().peek(e -> e.setPath(collect)).collect(Collectors.toList());

            //9.若根不存在,则加入暂存map中
            if (map.get(root) == null) {
                map.put(root, list);
            }
            //9.根存在则追加list元素
            else {
                map.get(root).addAll(list);
            }
        }
        //10.将数据赋给VO
        BeanUtils.copyProperties(examEntity, examLineTestVO);
        for (QuestionTypeEntity typeEntity : map.keySet()) {
            //11.查询根对应的分类树
            List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(typeList, typeEntity.getId());
            //12.排序并给vo赋值
            List<QuestionEntity> sortedList = map.get(typeEntity).stream().sorted(
                    Comparator.comparingInt(QuestionEntity::getSort)).collect(Collectors.toList());
            BigDecimal score = sortedList.get(0).getScore();
            examLineTestVO.getGroupedQuestions().add(new QuestionLineTestVO()
                    .setScore(score).setRootName(typeEntity.getTypeName()).setQuestions(sortedList).setTypeList(questionTypeAsTree));
        }
        return R.ok().put("examData", examLineTestVO).put("examRecord", examRecordVO);
    }


}
