package com.mnnu.examine.modules.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.exam.entity.ExamCategoryEntity;
import com.mnnu.examine.modules.exam.entity.ExamEntity;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.service.ExamCategoryService;
import com.mnnu.examine.modules.exam.service.ExamRecordService;
import com.mnnu.examine.modules.exam.service.ExamService;
import com.mnnu.examine.modules.exam.vo.*;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import com.mnnu.examine.modules.question.entity.QuestionTextRelationEntity;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;
import com.mnnu.examine.modules.question.entity.TextEntity;
import com.mnnu.examine.modules.question.service.QuestionService;
import com.mnnu.examine.modules.question.service.QuestionTextRelationService;
import com.mnnu.examine.modules.question.service.QuestionTypeService;
import com.mnnu.examine.modules.question.service.TextService;
import com.mnnu.examine.modules.question.vo.QuestionArgumentVO;
import com.mnnu.examine.modules.question.vo.QuestionLineTestVO;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:58
 */
@RestController
@RequestMapping("exam/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamRecordService examRecordService;

    @Autowired
    private QuestionTypeService questionTypeService;

    @Autowired
    private ExamCategoryService examCategoryService;

    @Autowired
    private QuestionTextRelationService questionTextRelationService;

    @Autowired
    private TextService textService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        String categoryName = (String) params.get("categoryName");
        if (StringUtils.hasText(categoryName)) {
            ExamCategoryEntity categoryEntity = examCategoryService.getOne(
                    new QueryWrapper<ExamCategoryEntity>().eq("category_name", categoryName));
            params.remove("categoryName");
            params.put("categoryId", categoryEntity.getId());
        }
        PageUtils page = examService.queryPage(params);
        List<ExamEntity> list = (List<ExamEntity>) page.getList();
        List<Long> states = new ArrayList<>();
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();


        List<ExamRecordEntity> recordList = examRecordService.list(
                new QueryWrapper<ExamRecordEntity>().eq("user_id", userId));

        //如果不存在做题记录,则全部返回0
        if (CollectionUtils.isEmpty(recordList)) {
            for (int i = 0; i < list.size(); i++) {
                states.add(0L);
            }
        }
        //如果存在做题记录,则判断是未完成还是已完成
        else {
            for (ExamEntity examEntity : list) {
                //在用户做题记录中找出关于该试卷的做题记录
                List<ExamRecordEntity> collect = recordList.stream().filter(
                        recordEntity -> Objects.equals(recordEntity.getExamId(), examEntity.getId())).collect(Collectors.toList());
                //如果不存在,认定为没有做过该试卷
                if (CollectionUtils.isEmpty(collect)) {
                    states.add(0L);
                }
                //否则,在该用户该试卷的做题记录中找出一个提交时间为空的记录
                else {
                    ExamRecordEntity entity = collect.stream().filter(
                            recordEntity -> !StringUtils.hasText(recordEntity.getCommitTime())).findFirst().orElse(null);
                    //如果找到了记录,认定为未完成
                    if (Objects.nonNull(entity)) {
                        states.add(1L);
                    }
                    //否则认定为已完成
                    else {
                        states.add(2L);
                    }
                }
            }
        }
        return R.ok().put("page", page).put("states", states);
    }

    /**
     * 根据题目数量生成试卷
     *
     * @param params
     * @return
     */
    @RequestMapping("/get/linetest/custom")
    public R getLineTestByCustom(@RequestParam Map<String, Object> params) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        int count = examRecordService.count(
                new QueryWrapper<ExamRecordEntity>().eq("user_id", userId).eq("is_custom", GwyConstant.ExamType.CUSTOM.getType()));
        if (count >= 10) {
            return R.error(GwyConstant.BizCode.EXAM_LIMIT_EXCEED.getCode(),
                    GwyConstant.BizCode.EXAM_LIMIT_EXCEED.getMessage());
        }
        Integer num = Integer.parseInt((String) params.get("num"));
        Integer typeId = Integer.parseInt((String) params.get("typeId"));
        List<QuestionTypeEntity> tree = questionTypeService.getQuestionTypeAsTree(0);
        tree = tree.get(0).getChildren();
        //查询普通试卷(非定制化)
        List<ExamEntity> exams = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", typeId)
                        .eq("is_custom", GwyConstant.ExamType.NORMAL.getType()));
        if (CollectionUtils.isEmpty(exams)) {
            return R.error("该分类条件下暂无试卷!");
        }
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        List<QuestionEntity> allQuestions = new ArrayList<>();
        for (QuestionTypeEntity node : tree) {
            List<Integer> leaves = new ArrayList<>();
            List<QuestionEntity> questions;
            questionTypeService.getLeave(node.getChildren(), leaves);
            if (!GwyConstant.ExamType.DATA_ANALYSIS.getName().equals(node.getTypeName())) {
                String typeIds = "'" + org.apache.commons.lang.StringUtils.join(leaves, "','") + "'";
                String examIds = "'" + org.apache.commons.lang.StringUtils.join(examIdList, "','") + "'";
                questions = questionService.getQuestionsByTypesRandom(typeIds, examIds, num);
            } else {
                //获取资料分析题的总数
                int total = questionService.count(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves)
                                .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType()));
                if (total < num) {
                    return R.error("题库题目数量不足!");
                }
                int start = new Random().nextInt((total - num) / 5 + 1);
                questions = questionService.list(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves)
                                .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType())
                                .last(String.format("limit %d,%d", start * 5, num)));
            }
            if (!num.equals(questions.size())) {
                return R.error("题库题目数量不足!");
            }
            allQuestions.addAll(questions);
        }
        ExamEntity examEntity = new ExamEntity();
        examEntity.setExamTitle("随机练习(常识判断" + num + "言语理解与表达" + num + "数量关系" + num + "判断推理" + num + "资料分析" + num + ")");
        examEntity.setType(typeId);
        examEntity.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
        examEntity.setLimitTime(60);
        examEntity.setTotalScore(new BigDecimal("0.00"));
        BigDecimal sum = allQuestions.stream().map(
                QuestionEntity::getScore).reduce(examEntity.getTotalScore(), BigDecimal::add);
        examEntity.setTotalScore(sum);
        examService.save(examEntity);
        AtomicInteger sort = new AtomicInteger(1);
        List<QuestionEntity> collect = allQuestions.stream().peek(e -> {
            e.setExamId(examEntity.getId());
            e.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
            e.setId(null);
            e.setSort(sort.getAndIncrement());
            e.setCreateTime("");
            e.setUpdateTime("");
        }).collect(Collectors.toList());
        questionService.saveBatch(collect);
        return R.ok("试卷生成成功!").put("examId", examEntity.getId());
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamEntity exam = examService.getById(id);

        return R.ok().put("exam", exam);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamEntity exam) {
        examService.save(exam);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamEntity exam) {
        examService.updateById(exam);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        //查询试卷对应的试题列表
        List<QuestionEntity> questions = questionService.list(
                new QueryWrapper<QuestionEntity>().in("exam_id", Arrays.asList(ids)));
        //得到试题列表的id列表
        List<Long> questionIds = questions.stream().map(
                QuestionEntity::getId).collect(Collectors.toList());
        //根据试题id列表查询试卷对应的关系表
        List<QuestionTextRelationEntity> questionTextRelations = questionTextRelationService.list(
                new QueryWrapper<QuestionTextRelationEntity>().in("question_id", questionIds));
        //得到材料列表的id列表
        List<Integer> textIds = questionTextRelations.stream().map(
                QuestionTextRelationEntity::getQuestionTextId).collect(Collectors.toList());
        //删除试卷
        examService.removeByIds(Arrays.asList(ids));
        //删除试题
        questionService.removeByIds(questionIds);
        //删除试题-材料关系
        questionTextRelationService.removeByIds(
                questionTextRelations.stream().map(QuestionTextRelationEntity::getId).collect(Collectors.toList()));
        //删除材料
        textService.removeByIds(textIds);
        //删除做题记录
        examRecordService.remove(
                new QueryWrapper<ExamRecordEntity>().in("exam_id", Arrays.asList(ids)));
        return R.ok("删除成功!");
    }

    /**
     * 保存做题信息
     *
     * @return
     */
    @RequestMapping("/save/record")
    public R saveExamRecord(@RequestBody ExamRecordVO record) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        record.setUserId(userId);
        ExamEntity examEntity = examService.getById(record.getExamId());
        if (Objects.isNull(examEntity)) {
            return R.error(GwyConstant.BizCode.EXAM_NOT_EXIT.getCode(),
                    GwyConstant.BizCode.EXAM_NOT_EXIT.getMessage());
        }
        //如果存在提交时间,认定为手动提交
        if (StringUtils.hasText(record.getCommitTime())) {

            //若之前存在做题记录,删除之前的做题记录,重新批改试卷
            examRecordService.removeById(record.getId());
            record.setId(null);
            record.setScore(new BigDecimal("0.00"));
            List<QuestionEntity> list = questionService.list(
                    new QueryWrapper<QuestionEntity>().eq("exam_id", record.getExamId()));
            List<QuestionEntity> collect = list.stream()
                    .sorted(Comparator.comparingInt(QuestionEntity::getSort))
                    .collect(Collectors.toList());

            ExamRecordEntity recordEntity = new ExamRecordEntity();
            BeanUtils.copyProperties(record, recordEntity);

            //先保存,异步更新
            examRecordService.save(recordEntity);
            record.setId(recordEntity.getId());
            examRecordService.gradingExams(record, collect);
            return R.ok("提交成功!");
        }
        //若不存在提交时间认定为未完成
        else {
            ExamRecordEntity recordEntity = new ExamRecordEntity();
            BeanUtils.copyProperties(record, recordEntity);
            recordEntity.setUserAnswers(String.join("&", record.getUserAnswers()));
            examRecordService.saveOrUpdate(recordEntity);
            return R.ok();
        }
    }


    /**
     * 查询行测试卷
     *
     * @param id
     * @return
     * @author jljy
     */
    @RequestMapping("/get/linetest/{id}")
    public R getLineTest(@PathVariable Integer id) {
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        ExamRecordVO examRecordVO = new ExamRecordVO();

        //1.查询试卷
        ExamEntity examEntity = examService.getById(id);

        if (Objects.isNull(examEntity)) {
            return R.error(GwyConstant.BizCode.EXAM_NOT_EXIT.getCode(), GwyConstant.BizCode.EXAM_NOT_EXIT.getMessage());
        }

        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();


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
            QuestionTypeEntity root = questionTypeService.getRoot(typeList, typeEntity);

            List<QuestionEntity> list = groupedQuestions.get(integer).stream()
                    .peek(questionEntity -> {
                        questionEntity.setAnalysis(null);
                        questionEntity.setAnswer(null);
                    }).collect(Collectors.toList());

            //7.若根不存在,则加入暂存map中
            if (map.get(root) == null) {
                map.put(root, list);
            }
            //8.根存在则追加list元素
            else {
                map.get(root).addAll(list);
            }
        }
        //9.将数据赋给VO
        BeanUtils.copyProperties(examEntity, examLineTestVO);
        for (QuestionTypeEntity typeEntity : map.keySet()) {
            List<QuestionEntity> sortedList = map.get(typeEntity).stream().sorted(
                    Comparator.comparingInt(QuestionEntity::getSort)).collect(Collectors.toList());
            BigDecimal score = sortedList.get(0).getScore();
            examLineTestVO.getGroupedQuestions().add(new QuestionLineTestVO()
                    .setScore(score).setRootName(typeEntity.getTypeName()).setQuestions(sortedList));
        }
        //查询用户做题记录
        ExamRecordEntity recordEntity = examRecordService.getOne(
                new QueryWrapper<ExamRecordEntity>()
                        .eq("user_id", userId)
                        .eq("exam_id", id)
                        .eq("commit_time", ""));
        //表示查到做题记录
        if (Objects.nonNull(recordEntity)) {
            BeanUtils.copyProperties(recordEntity, examRecordVO);
            String[] split = recordEntity.getUserAnswers().split("&");
            examRecordVO.setUserAnswers(Arrays.asList(split));
            return R.ok().put("examData", examLineTestVO).put("examRecord", examRecordVO);
        }
        return R.ok().put("examData", examLineTestVO);
    }

    /**
     * 查询写作试卷
     *
     * @param id
     * @return
     * @author jljy
     */
    @RequestMapping("/get/writing/{id}")
    public R getWriting(@PathVariable Integer id) {
        ExamArgumentVO examArgumentVO = new ExamArgumentVO();
        //1.查询试卷
        ExamEntity examEntity = examService.getById(id);
        BeanUtils.copyProperties(examEntity, examArgumentVO);

        //2.查询试题及其对应的试题列表
        List<QuestionEntity> questionWithTexts = questionService.getQuestionWithTexts(id);

        questionWithTexts.forEach(q -> {
            QuestionArgumentVO questionArgumentVO = new QuestionArgumentVO();
            BeanUtils.copyProperties(q, questionArgumentVO);

            List<String> textList = new ArrayList<>();
            q.getTextList().forEach(t -> {
                t.setName(t.getSort() + "");
                textList.add(t.getSort() + "");
            });
            BeanUtils.copyProperties(q, questionArgumentVO);
            examArgumentVO.getTexts().addAll(q.getTextList());
            examArgumentVO.getQuestions().add(
                    questionArgumentVO.setTextList(textList).setName(q.getSort() + ""));
        });
        List<TextEntity> collect = examArgumentVO.getTexts()
                .stream().distinct().sorted(Comparator.comparingInt(TextEntity::getSort))
                .collect(Collectors.toList());
        examArgumentVO.setTexts(collect);
        return R.ok().put("examData", examArgumentVO);

    }

    /**
     * 查询申论、带材料面试试卷
     *
     * @param id
     * @return
     * @author jljy
     */
    @RequestMapping("/get/argument/{id}")
    public R getArgument(@PathVariable Integer id) {
        ExamArgumentVO examArgumentVO = new ExamArgumentVO();
        //1.查询试卷
        ExamEntity examEntity = examService.getById(id);
        if (Objects.isNull(examEntity)) {
            return R.error(GwyConstant.BizCode.EXAM_NOT_EXIT.getCode(), GwyConstant.BizCode.EXAM_NOT_EXIT.getMessage());
        }

        BeanUtils.copyProperties(examEntity, examArgumentVO);

        //2.查询试题及其对应的试题列表
        List<QuestionEntity> questionWithTexts = questionService.getQuestionWithTexts(id);

        //3.查询所有试题类型
        List<QuestionTypeEntity> typeList = questionTypeService.list();

        questionWithTexts.forEach(q -> {
            QuestionArgumentVO questionArgumentVO = new QuestionArgumentVO();
            BeanUtils.copyProperties(q, questionArgumentVO);

            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().orElse(null);

            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);

            List<Object> collect = path.stream().map(QuestionTypeEntity::getTypeName).collect(Collectors.toList());

            List<String> textList = new ArrayList<>();
            q.getTextList().forEach(t -> {
                t.setName(t.getSort() + "");
                textList.add(t.getSort() + "");
            });
            BeanUtils.copyProperties(q, questionArgumentVO);
            examArgumentVO.getTexts().addAll(q.getTextList());
            examArgumentVO.getQuestions().add(
                    questionArgumentVO.setTextList(textList).setName(q.getSort() + "").setPath(collect));
        });
        List<TextEntity> collect = examArgumentVO.getTexts()
                .stream().distinct().sorted(Comparator.comparingInt(TextEntity::getSort))
                .collect(Collectors.toList());

        examArgumentVO.setTexts(collect);

        return R.ok("查询试卷成功!").put("examData", examArgumentVO);

    }

    /**
     * 获取专项练习试卷
     *
     * @param examSpecializedVO
     * @return
     */
    @RequestMapping("/get/linetest/specialized")
    public R getLineTestBySpecialized(@RequestBody ExamSpecializedVO examSpecializedVO) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        int count = examRecordService.count(
                new QueryWrapper<ExamRecordEntity>().eq("user_id", userId).eq("is_custom", GwyConstant.ExamType.CUSTOM.getType()));
        if (count >= 10) {
            return R.error(GwyConstant.BizCode.EXAM_LIMIT_EXCEED.getCode(),
                    GwyConstant.BizCode.EXAM_LIMIT_EXCEED.getMessage());
        }
        //查询所有试卷分类
        List<QuestionTypeEntity> questionTypeList = questionTypeService.list();
        //找出组卷的分类
        QuestionTypeEntity typeEntity = questionTypeList.stream().filter(
                e -> e.getId().equals(examSpecializedVO.getQuestionTypeId())).findFirst().orElse(null);
        //获取分类对应的分类树
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(questionTypeList, examSpecializedVO.getQuestionTypeId());
        QuestionTypeEntity root = questionTypeService.getRoot(questionTypeList, typeEntity);
        QueryWrapper<ExamEntity> wrapper = new QueryWrapper<ExamEntity>().eq("type", examSpecializedVO.getTypeId())
                .eq("is_custom", GwyConstant.ExamType.NORMAL.getType());
        if (!CollectionUtils.isEmpty(examSpecializedVO.getCategoryIds())) {
            wrapper.in("category_id", examSpecializedVO.getCategoryIds());
        }
        List<ExamEntity> exams = examService.list(wrapper);
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        String examIds = "'" + org.apache.commons.lang.StringUtils.join(examIdList, "','") + "'";
        String typeIds = "";
        List<QuestionEntity> specialized;
        List<Integer> leaves = new ArrayList<>();
        ExamEntity examEntity = new ExamEntity();
        //获取树的叶子节点
        questionTypeService.getLeave(questionTypeAsTree, leaves);
        if (leaves.size() != 0) {
            typeIds = "'" + org.apache.commons.lang.StringUtils.join(leaves, "','") + "'";
        } else {
            typeIds = "'" + examSpecializedVO.getQuestionTypeId() + "'";
        }
        if (!GwyConstant.ExamType.DATA_ANALYSIS.getName().equals(root.getTypeName())) {
            specialized = questionService.getQuestionsByTypesRandom(typeIds, examIds, examSpecializedVO.getNum());
        } else {
            //获取资料分析题的总数
            List<Integer> list = leaves.size() != 0 ? leaves : Collections.singletonList(examSpecializedVO.getQuestionTypeId());
            int total = questionService.count(
                    new QueryWrapper<QuestionEntity>().in("type_id", list)
                            .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType()));
            if (total < examSpecializedVO.getNum()) {
                return R.error("题库题目数量不足!");
            }
            int start = new Random().nextInt((total - examSpecializedVO.getNum()) / 5 + 1);
            specialized = questionService.list(
                    new QueryWrapper<QuestionEntity>().in("type_id", list)
                            .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType())
                            .last(String.format("limit %d,%d", start * 5, examSpecializedVO.getNum())));
        }
        if (!examSpecializedVO.getNum().equals(specialized.size())) {
            return R.error("题库题目数量不足!");
        }
        examEntity.setExamTitle("专项练习(" + examSpecializedVO.getQuestionTypeName() + ")");
        examEntity.setType(examSpecializedVO.getTypeId());
        examEntity.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
        examEntity.setLimitTime(60);
        examEntity.setTotalScore(new BigDecimal("0.00"));
        BigDecimal sum = specialized.stream().map(
                QuestionEntity::getScore).reduce(examEntity.getTotalScore(), BigDecimal::add);
        examEntity.setTotalScore(sum);
        examService.save(examEntity);
        AtomicInteger sort = new AtomicInteger(1);
        List<QuestionEntity> collect = specialized.stream().peek(e -> {
            e.setExamId(examEntity.getId());
            e.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
            e.setId(null);
            e.setSort(sort.getAndIncrement());
            e.setCreateTime("");
            e.setUpdateTime("");
        }).collect(Collectors.toList());
        questionService.saveBatch(collect);
        return R.ok("试卷生成成功!").put("examId", examEntity.getId());
    }


    /**
     * 查询不带材料的面试试卷
     *
     * @param id
     * @return
     * @author jljy
     */
    @RequestMapping("/get/interview/{id}")
    public R getInterView(@PathVariable Integer id) {
        ExamInterViewVO examInterViewVO = new ExamInterViewVO();
        //查询试卷
        ExamEntity examEntity = examService.getById(id);
        if (Objects.isNull(examEntity)) {
            return R.error(GwyConstant.BizCode.EXAM_NOT_EXIT.getCode(), GwyConstant.BizCode.EXAM_NOT_EXIT.getMessage());
        }
        //查询所有试题分类
        List<QuestionTypeEntity> typeList = questionTypeService.list();
        BeanUtils.copyProperties(examEntity, examInterViewVO);
        //查询试题
        List<QuestionEntity> questionList = questionService.list(
                new QueryWrapper<QuestionEntity>().eq("exam_id", id));
        questionList.forEach(q -> {
            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().orElse(null);
            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);
            List<Object> collect = path.stream().map(QuestionTypeEntity::getTypeName).collect(Collectors.toList());
            q.setPath(collect);
        });
        List<QuestionEntity> collect = questionList.stream().peek(e -> e.setAnalysis(e.getSort() + "")).collect(Collectors.toList());
        examInterViewVO.setQuestions(collect);
        return R.ok("试卷生成成功!").put("examData", examInterViewVO);
    }

    /**
     * 获取试卷对应的类型
     *
     * @param id
     * @return
     */
    @RequestMapping("/type/{id}")
    public R getExamType(@PathVariable("id") Long id) {
        ExamEntity examEntity = examService.getById(id);
        return R.ok().put("type", examEntity.getType());
    }

    /**
     * 获取申论定制试卷
     *
     * @param examSpecializedVO
     * @return
     */
    @RequestMapping("/get/argument/specialized")
    public R getArgumentBySpecialized(@RequestBody ExamSpecializedVO examSpecializedVO) {
        QueryWrapper<ExamEntity> wrapper = new QueryWrapper<ExamEntity>().eq("type", examSpecializedVO.getTypeId())
                .eq("is_custom", GwyConstant.ExamType.NORMAL.getType());
        if (!CollectionUtils.isEmpty(examSpecializedVO.getCategoryIds())) {
            wrapper.in("category_id", examSpecializedVO.getCategoryIds());
        }
        List<ExamEntity> exams = examService.list(wrapper);
        if (CollectionUtils.isEmpty(exams)) {
            return R.error("该分类条件下暂无试卷!");
        }
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        int count = questionService.count(
                new QueryWrapper<QuestionEntity>().in("exam_id", examIdList));
        if (count == 0) {
            return R.error("题库题目数量不足!");
        }
        int random = new Random().nextInt(count);

        QuestionEntity question = questionService.getOne(
                new QueryWrapper<QuestionEntity>().in("exam_id", examIdList).last(String.format("limit %d,%d", random, 1)));

        List<QuestionTextRelationEntity> relationList = questionTextRelationService.list(
                new QueryWrapper<QuestionTextRelationEntity>().eq("question_id", question.getId()));

        List<Integer> textIds = relationList.stream().map(
                QuestionTextRelationEntity::getQuestionTextId).collect(Collectors.toList());

        List<TextEntity> texts = textService.listByIds(textIds);

        List<QuestionTypeEntity> typeList = questionTypeService.list();

        ExamArgumentVO examArgumentVO = new ExamArgumentVO();

        if (GwyConstant.ExamType.AT.getType().equals(examSpecializedVO.getTypeId())) {
            examArgumentVO.setExamTitle("单题练习(" + GwyConstant.ExamType.AT.getName() + ")");
        } else {
            examArgumentVO.setExamTitle("单题练习(" + GwyConstant.ExamType.WT.getName() + ")");
        }
        examArgumentVO.setType(GwyConstant.ExamType.AT.getType());
        examArgumentVO.setLimitTime(60);
        AtomicInteger sort = new AtomicInteger(1);
        List<TextEntity> collect = texts.stream().peek(e -> {
            e.setName(sort.get() + "");
            e.setSort(sort.getAndIncrement());
        }).collect(Collectors.toList());

        QuestionTypeEntity typeEntity = typeList.stream().filter(
                type -> type.getId().equals(question.getTypeId())).findFirst().orElse(null);

        QuestionArgumentVO questionArgumentVO = new QuestionArgumentVO();
        BeanUtils.copyProperties(question, questionArgumentVO);
        questionArgumentVO.setSort(1);
        questionArgumentVO.setName("1");

        if (Objects.nonNull(typeEntity)) {
            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);

            List<Object> questionPath = path.stream().map(
                    QuestionTypeEntity::getTypeName).collect(Collectors.toList());

            questionArgumentVO.setPath(questionPath);
        }

        examArgumentVO.setQuestions(Collections.singletonList(questionArgumentVO));
        examArgumentVO.setTexts(collect);
        return R.ok("题目获取成功!").put("examData", examArgumentVO);
    }

    /**
     * 获取专项练习-面试
     *
     * @param examSpecializedVO
     * @return
     */
    @RequestMapping("/get/interview/specialized")
    public R getInterViewSpecialized(@RequestBody ExamSpecializedVO examSpecializedVO) {
        QueryWrapper<ExamEntity> wrapper = new QueryWrapper<ExamEntity>().eq("type", examSpecializedVO.getTypeId())
                .eq("is_custom", GwyConstant.ExamType.NORMAL.getType());
        if (!CollectionUtils.isEmpty(examSpecializedVO.getCategoryIds())) {
            wrapper.in("category_id", examSpecializedVO.getCategoryIds());
        }
        List<ExamEntity> exams = examService.list(wrapper);
        if (CollectionUtils.isEmpty(exams)) {
            return R.error("该分类条件下暂无试卷!");
        }
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        List<QuestionTypeEntity> typeList = questionTypeService.list();

        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(typeList, examSpecializedVO.getQuestionTypeId());

        List<Integer> leaves = new ArrayList<>();
        questionTypeService.getLeave(questionTypeAsTree, leaves);
        String examIds = "'" + org.apache.commons.lang.StringUtils.join(examIdList, "','") + "'";
        String typeIds = "'" + org.apache.commons.lang.StringUtils.join(leaves, "','") + "'";

        List<QuestionEntity> questions = questionService.getQuestionsByTypesRandom(typeIds, examIds, examSpecializedVO.getNum());

        if (questions.size() < examSpecializedVO.getNum()) {
            return R.error("题库题目数量不足!");
        }
        AtomicInteger sort = new AtomicInteger(1);
        questions.forEach(q -> {
            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().orElse(null);
            if (Objects.nonNull(typeEntity)) {
                List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
                Collections.reverse(path);
                List<Object> collect = path.stream().map(
                        QuestionTypeEntity::getTypeName).collect(Collectors.toList());
                q.setPath(collect);
            }
            q.setAnalysis(sort.get() + "");
            q.setSort(sort.getAndIncrement());
        });
        ExamInterViewVO examInterViewVO = new ExamInterViewVO();
        examInterViewVO.setLimitTime(60);
        examInterViewVO.setExamTitle("专项练习(" + GwyConstant.ExamType.TITORIT.getName() + ")");
        examInterViewVO.setQuestions(questions);
        return R.ok("题目获取成功!").put("examData", examInterViewVO);
    }

    /**
     * 获取随机刷题-面试
     *
     * @param params
     * @return
     */
    @RequestMapping("/get/interview/custom")
    public R getInterviewCustom(@RequestParam Map<String, Object> params) {
        Integer num = Integer.parseInt((String) params.get("num"));
        Integer typeId = Integer.parseInt((String) params.get("typeId"));
        List<QuestionEntity> questions = questionService.getQuestionsByExamTypeRandom(typeId, num);
        if (CollectionUtils.isEmpty(questions)) {
            return R.error("题库题目数量不足!");
        }
        //查询所有试题分类
        List<QuestionTypeEntity> typeList = questionTypeService.list();

        AtomicInteger sort = new AtomicInteger(1);
        questions.forEach(q -> {
            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().orElse(null);
            if (Objects.nonNull(typeEntity)) {
                List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
                Collections.reverse(path);
                List<Object> collect = path.stream().map(
                        QuestionTypeEntity::getTypeName).collect(Collectors.toList());
                q.setPath(collect);
            }
            q.setAnalysis(sort.get() + "");
            q.setSort(sort.getAndIncrement());
        });
        ExamInterViewVO examInterViewVO = new ExamInterViewVO();
        examInterViewVO.setLimitTime(60);
        examInterViewVO.setExamTitle("随机刷题(" + GwyConstant.ExamType.TITORIT.getName() + ")");
        examInterViewVO.setQuestions(questions);
        return R.ok("题目获取成功!").put("examData", examInterViewVO);
    }
}
