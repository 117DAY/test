package io.renren.modules.exam.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.exception.RRException;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.exam.entity.ExamRecordEntity;
import io.renren.modules.exam.vo.ExamRecordVO;
import io.renren.modules.exam.service.ExamRecordService;
import io.renren.modules.exam.service.ExamService;
import io.renren.modules.exam.utils.WordParseUtil;
import io.renren.modules.exam.vo.ExamArgumentVO;
import io.renren.modules.exam.vo.ExamInterViewVO;
import io.renren.modules.exam.vo.ExamLineTestVO;
import io.renren.modules.question.vo.QuestionArgumentVO;
import io.renren.modules.question.vo.QuestionLineTestVO;
import io.renren.modules.oss.cloud.OSSFactory;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.QuestionTextRelationEntity;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.service.QuestionService;
import io.renren.modules.question.service.QuestionTextRelationService;
import io.renren.modules.question.service.QuestionTypeService;
import io.renren.modules.question.service.TextService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
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
    private TextService textService;

    @Autowired
    private QuestionTextRelationService questionTextRelationService;

    @Autowired
    private QuestionTypeService questionTypeService;

    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = examService.queryPage(params);

        return R.ok().put("page", page);
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
        List<Integer> questionIds = questions.stream().map(
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
        return R.ok();
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping("/upload/picture")
    public R uploadPicture(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RRException("上传文件不能为空");
        }
        //上传文件
        String suffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        String url = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getBytes(), suffix);
        return R.ok().put("url", url);
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

        //1.查询试卷
        ExamEntity examEntity = examService.getById(id);
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
            List<Integer> collect = path.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());

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
        return R.ok("查询试卷成功!").put("examData", examLineTestVO);
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
        return R.ok("查询试卷成功!").put("examData", examArgumentVO);

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
        BeanUtils.copyProperties(examEntity, examArgumentVO);

        //2.查询试题及其对应的试题列表
        List<QuestionEntity> questionWithTexts = questionService.getQuestionWithTexts(id);

        //3.查询所有试题类型
        List<QuestionTypeEntity> typeList = questionTypeService.list();

        questionWithTexts.forEach(q -> {
            QuestionArgumentVO questionArgumentVO = new QuestionArgumentVO();
            BeanUtils.copyProperties(q, questionArgumentVO);

            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().get();

            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);

            List<Integer> collect = path.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());

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
        //查询所有试题分类
        List<QuestionTypeEntity> typeList = questionTypeService.list();
        BeanUtils.copyProperties(examEntity, examInterViewVO);
        //查询试题
        List<QuestionEntity> questionList = questionService.list(new QueryWrapper<QuestionEntity>().eq("exam_id", id));
        questionList.forEach(q -> {
            QuestionTypeEntity typeEntity = typeList.stream().filter(
                    type -> type.getId().equals(q.getTypeId())).findFirst().get();
            List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), typeList, typeEntity);
            Collections.reverse(path);
            List<Integer> collect = path.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());
            q.setPath(collect);
        });
        List<QuestionEntity> collect = questionList.stream().peek(e -> e.setAnalysis(e.getSort() + "")).collect(Collectors.toList());
        examInterViewVO.setQuestions(collect);
        return R.ok("查询试卷成功!").put("examData", examInterViewVO);
    }

    /**
     * 初始化行测试卷数据
     *
     * @param examLineTestVO
     * @return
     */
    @RequestMapping("/init/linetest")
    public R initLineTest(@RequestBody ExamLineTestVO examLineTestVO) {
        List<QuestionTypeEntity> list = questionTypeService.list();
        examLineTestVO.getGroupedQuestions().forEach(e -> {
            QuestionTypeEntity typeEntity = list.stream().filter(type -> type.getTypeName().equals(e.getRootName())).findFirst().get();
            //获取分类树
            List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(list, typeEntity.getId());
            //获取默认路径
            List<QuestionTypeEntity> defaultPath = questionTypeService.getDefaultPath(new ArrayList<>(), questionTypeAsTree.get(0));
            List<Integer> collect = defaultPath.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());
            e.setTypeList(questionTypeAsTree);
            e.setDefaultPath(collect);
        });
        return R.ok().put("examData", examLineTestVO);
    }

    /**
     * 上传行测试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/linetest")
    public R uploadLineTest(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamLineTestVO examLineTestVO = WordParseUtil.getLineTest(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            examLineTestVO.setPath(path);
            List<QuestionTypeEntity> list = questionTypeService.list();
            examLineTestVO.getGroupedQuestions().forEach(e -> {
                QuestionTypeEntity typeEntity = list.stream().filter(type -> type.getTypeName().equals(e.getRootName())).findFirst().get();
                //获取分类树
                List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(list, typeEntity.getId());
                //获取默认路径
                List<QuestionTypeEntity> defaultPath = questionTypeService.getDefaultPath(new ArrayList<>(), questionTypeAsTree.get(0));
                List<Integer> collect = defaultPath.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());
                e.setTypeList(questionTypeAsTree);
                e.setDefaultPath(collect);
            });
            return R.ok("试卷提取成功!").put("examData", examLineTestVO);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }
    }

    /**
     * 上传申论试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/argument")
    public R uploadArgument(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamArgumentVO examArgumentVO = WordParseUtil.getArgument(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            examArgumentVO.setPath(path);
            return R.ok("试卷提取成功!").put("examData", examArgumentVO);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }
    }

    /**
     * 上传材料面试试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/textinterview")
    public R uploadTextInterview(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamArgumentVO textInterView = WordParseUtil.getTextInterView(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            textInterView.setPath(path);
            return R.ok("试卷提取成功!").put("examData", textInterView);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }

    }

    /**
     * 上传问题面试试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/interview")
    public R uploadInterview(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamInterViewVO examInterViewVO = WordParseUtil.getInterView(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            examInterViewVO.setPath(path);
            return R.ok("试卷提取成功!").put("examData", examInterViewVO);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }

    }

    /**
     * 上传综合试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/synthesis")
    public R uploadSynthesis(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamLineTestVO examLineTestVO = WordParseUtil.getSynthesis(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            examLineTestVO.setPath(path);
            List<QuestionTypeEntity> list = questionTypeService.list();
            examLineTestVO.getGroupedQuestions().forEach(e -> {
                QuestionTypeEntity typeEntity = list.stream().filter(type -> type.getTypeName().equals(e.getRootName())).findFirst().get();
                //获取分类树
                List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(list, typeEntity.getId());
                //获取默认路径
                List<QuestionTypeEntity> defaultPath = questionTypeService.getDefaultPath(new ArrayList<>(), questionTypeAsTree.get(0));
                List<Integer> collect = defaultPath.stream().map(QuestionTypeEntity::getId).collect(Collectors.toList());
                e.setTypeList(questionTypeAsTree);
                e.setDefaultPath(collect);
            });
            return R.ok("试卷提取成功!").put("examData", examLineTestVO);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }

    }

    /**
     * 上传写作试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/upload/writing")
    public R uploadWriting(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return R.error("上传文件不能为空!");
        }
        try {
            ExamArgumentVO examWritingVO = WordParseUtil.getWriting(file.getInputStream());
            //试卷合法，上传并保存试卷
            String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(file.getInputStream(), ".docx");
            examWritingVO.setPath(path);
            return R.ok("试卷提取成功!").put("examData", examWritingVO);
        } catch (RRException e) {
            return R.error(e.getMsg());
        } catch (Exception e) {
            return R.error("未知试卷格式错误!");
        }
    }

    /**
     * 保存行测、综合试卷
     *
     * @param examLineTestVO
     * @return
     * @author jljy
     */
    @RequestMapping("/save/linetest")
    public R saveLineTest(@Valid @RequestBody ExamLineTestVO examLineTestVO) {
        ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(examLineTestVO, examEntity);
        List<QuestionEntity> list = new ArrayList<>();
        examLineTestVO.getGroupedQuestions().forEach(e -> {
            list.addAll(e.getQuestions());
        });
        examService.save(examEntity);
        List<QuestionEntity> collect = list.stream().peek(
                e -> e.setExamId(examEntity.getId())).collect(Collectors.toList());
        questionService.saveBatch(collect);
        return R.ok("保存成功");
    }

    /**
     * 保存申论、材料面试、写作试卷
     *
     * @param examArgumentVO
     * @return
     * @author jljy
     */
    @RequestMapping("/save/argument")
    public R saveArgument(@Valid @RequestBody ExamArgumentVO examArgumentVO) {
        ExamEntity examEntity = new ExamEntity();
        List<TextEntity> textList = new ArrayList<>();
        List<QuestionEntity> questionList = new ArrayList<>();
        List<QuestionTextRelationEntity> relationList = new ArrayList<>();

        BeanUtils.copyProperties(examArgumentVO, examEntity);

        examService.save(examEntity);

        examArgumentVO.getTexts().forEach(text -> {
            TextEntity textEntity = new TextEntity();
            BeanUtils.copyProperties(text, textEntity);
            textList.add(textEntity);
        });

        textService.saveBatch(textList);

        examArgumentVO.getQuestions().forEach(question -> {
            QuestionEntity questionEntity = new QuestionEntity();
            BeanUtils.copyProperties(question, questionEntity);
            questionEntity.setExamId(examEntity.getId());
            questionList.add(questionEntity);
        });
        questionService.saveBatch(questionList);
        examArgumentVO.getQuestions().forEach(q -> {
            examArgumentVO.getTexts().forEach(t -> {
                if (q.getTextList().contains(t.getName())) {
                    Integer questionId = questionList.get(q.getSort() - 1).getId();
                    Integer questionTextId = textList.get(t.getSort() - 1).getId();
                    QuestionTextRelationEntity relation = new QuestionTextRelationEntity();
                    relation.setQuestionId(questionId);
                    relation.setQuestionTextId(questionTextId);
                    relationList.add(relation);
                }
            });
        });
        questionTextRelationService.saveBatch(relationList);
        return R.ok("保存成功!");
    }

    /**
     * 保存面试试卷
     *
     * @param examInterViewVO
     * @return
     * @author jljy
     */
    @RequestMapping("/save/interview")
    public R saveInterView(@Valid @RequestBody ExamInterViewVO examInterViewVO) {

        ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(examInterViewVO, examEntity);

        examService.save(examEntity);

        List<QuestionEntity> collect = examInterViewVO.getQuestions().stream().peek(e -> {
            e.setAnalysis(null);
            e.setExamId(examEntity.getId());
        }).collect(Collectors.toList());

        questionService.saveBatch(collect);

        return R.ok("保存成功");
    }

    /**
     * 更新行测、综合试卷
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/update/linetest")
    public R updateLineTest(@Valid @RequestBody ExamLineTestVO examLineTestVO) {
        ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(examLineTestVO, examEntity);

        //更新试卷信息
        examService.updateById(examEntity);
        List<QuestionEntity> list = new ArrayList<>();
        examLineTestVO.getGroupedQuestions().forEach(e -> {
            list.addAll(e.getQuestions());
        });
        //更新试题信息
        questionService.updateBatchById(list);

        //更新相应做题记录信息(如果未提交更新标题，如果提交更新标题和分数)
        List<ExamRecordEntity> entityList = examRecordService.list(
                new QueryWrapper<ExamRecordEntity>().eq("exam_id", examEntity.getId()));
        //重新批改试卷
        for (ExamRecordEntity examRecordEntity : entityList) {
            examRecordEntity.setExamTitle(examEntity.getExamTitle());
            //未提交
            if (StringUtils.isBlank(examRecordEntity.getCommitTime())) {
                examRecordService.updateById(examRecordEntity);
            }
            //提交过
            else {
                ExamRecordVO examRecordVO = new ExamRecordVO();
                BeanUtils.copyProperties(examRecordEntity, examRecordVO);
                examRecordVO.setUserAnswers(Arrays.asList(examRecordEntity.getUserAnswers().split("&")));
                examRecordVO.setScore(new BigDecimal("0.00"));
                examRecordService.gradingExams(examRecordVO, list);
            }
        }
        return R.ok("更新成功!");
    }

    /**
     * 更新申论、材料面试、写作、试卷
     *
     * @param examArgumentVO
     * @return
     */
    @RequestMapping("/update/argument")
    public R updateArgument(@Valid @RequestBody ExamArgumentVO examArgumentVO) {
        ExamEntity examEntity = new ExamEntity();
        List<TextEntity> textList = new ArrayList<>();
        List<QuestionEntity> questionList = new ArrayList<>();
        List<QuestionTextRelationEntity> relationList = new ArrayList<>();

        BeanUtils.copyProperties(examArgumentVO, examEntity);
        //更新试卷信息
        examService.updateById(examEntity);

        //得到新的材料列表
        examArgumentVO.getTexts().forEach(text -> {
            TextEntity textEntity = new TextEntity();
            BeanUtils.copyProperties(text, textEntity);
            textList.add(textEntity);
        });
        textService.updateBatchById(textList);

        //得到新的试题列表
        examArgumentVO.getQuestions().forEach(question -> {
            QuestionEntity questionEntity = new QuestionEntity();
            BeanUtils.copyProperties(question, questionEntity);
            questionEntity.setExamId(examEntity.getId());
            questionList.add(questionEntity);
        });
        questionService.updateBatchById(questionList);
        List<Integer> questionIds = examArgumentVO.getQuestions().stream().map(
                QuestionArgumentVO::getId).collect(Collectors.toList());
        questionTextRelationService.remove(
                new QueryWrapper<QuestionTextRelationEntity>().in("question_id", questionIds));
        //获取新的中间关系信息
        examArgumentVO.getQuestions().forEach(q -> {
            examArgumentVO.getTexts().forEach(t -> {
                if (q.getTextList().contains(t.getName())) {
                    Integer questionId = questionList.get(q.getSort() - 1).getId();
                    Integer questionTextId = textList.get(t.getSort() - 1).getId();
                    QuestionTextRelationEntity relation = new QuestionTextRelationEntity();
                    relation.setQuestionId(questionId);
                    relation.setQuestionTextId(questionTextId);
                    relationList.add(relation);
                }
            });
        });
        //保存中间关系信息
        questionTextRelationService.saveBatch(relationList);
        return R.ok("更新成功!");
    }

    /**
     * 更新面试试卷
     *
     * @param examInterViewVO
     * @return
     */
    @RequestMapping("/update/interview")
    public R updateInterView(@Valid @RequestBody ExamInterViewVO examInterViewVO) {
        ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(examInterViewVO, examEntity);
        examService.updateById(examEntity);
        List<QuestionEntity> collect = examInterViewVO.getQuestions().stream()
                .peek(q -> q.setAnalysis("")).collect(Collectors.toList());
        questionService.updateBatchById(collect);
        return R.ok("更新成功!");
    }

    /**
     * 根据题目数量定制行测试卷
     *
     * @param limits
     * @return
     */
    @RequestMapping("/get/linetest/custom")
    public R getExamByCustom(@RequestBody List<Integer> limits) {
        List<QuestionTypeEntity> tree = questionTypeService.getQuestionTypeAsTree(0);
        tree = tree.get(0).getChildren();
        List<ExamEntity> exams = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", GwyConstant.ExamType.LT.getType()));
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        List<QuestionEntity> allQuestions = new ArrayList<>();
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        examLineTestVO.setExamTitle(String.format("随机练习(常识判断%d+言语理解与表达%d+数量关系%d+判断推理%d+资料分析%d)",
                limits.get(0), limits.get(1), limits.get(2), limits.get(3), limits.get(4)));
        examLineTestVO.setType(GwyConstant.ExamType.LT.getType());
        examLineTestVO.setLimitTime(120);
        examLineTestVO.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
        examLineTestVO.setTotalScore(new BigDecimal("0.00"));
        for (int i = 0; i < tree.size(); i++) {
            int limit = limits.get(i);
            QuestionTypeEntity node = tree.get(i);
            List<Integer> leaves = new ArrayList<>();
            List<QuestionEntity> questions = new ArrayList<>();
            questionTypeService.getLeave(node.getChildren(), leaves);
            if (!"资料分析".equals(node.getTypeName())) {
                String typeIds = "'" + org.apache.commons.lang.StringUtils.join(leaves, "','") + "'";
                String examIds = "'" + org.apache.commons.lang.StringUtils.join(examIdList, "','") + "'";
                questions = questionService.getQuestionsByTypesRandom(typeIds, examIds, limit);
            } else {
                //获取资料分析题的总数
                int total = questionService.count(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves));
                int start = new Random().nextInt((total - limit) / 5 + 1);
                questions = questionService.list(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves).last(String.format("limit %d,%d", start * 5, limit)));
            }
            if (questions.size() != 0) {
                questions.forEach(e -> {
                    QuestionEntity q = new QuestionEntity();
                    BeanUtils.copyProperties(e, q);
                    e.setAnswer("");
                    e.setAnalysis("");
                    q.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
                    q.setId(null);
                    q.setCreateTime("");
                    q.setUpdateTime("");
                    q.setSort(allQuestions.size() + 1);
                    allQuestions.add(q);
                });
                examLineTestVO.getGroupedQuestions().add(
                        new QuestionLineTestVO().setRootName(node.getTypeName()).setScore(questions.get(0).getScore()).setQuestions(questions));
                BigDecimal sum = questions.stream().map(
                        QuestionEntity::getScore).reduce(examLineTestVO.getTotalScore(), BigDecimal::add);
                examLineTestVO.setTotalScore(sum);
            }
        }
        ExamEntity examEntity = new ExamEntity();
        BeanUtils.copyProperties(examLineTestVO, examEntity);
//        examService.save(examEntity);
        List<QuestionEntity> collect = allQuestions.stream().peek(
                e -> e.setExamId(examEntity.getId())
        ).collect(Collectors.toList());
//        questionService.saveBatch(collect);
        return R.ok().put("examData", examLineTestVO).put("examEntity", examEntity).put("questions", collect);
    }


    /**
     * 根据题类的id和题目数量获得一组题目
     *
     * @param typeId
     * @param limit
     * @return
     */
    @RequestMapping("/get/random")
    public R getExamByRandom(Integer typeId, Integer limit) {
//        //查询所有试题分类
//        List<QuestionTypeEntity> list = questionTypeService.list();
//
//        QuestionTypeEntity typeEntity = list.stream().filter(
//                e -> e.getId().equals(typeId)).findFirst().get();
//        //将需要查询的根节点组装为树
//        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(list, typeEntity.getId());
//        List<Integer> leaves = new ArrayList<>();
//        //获取树的叶子节点
//        questionTypeService.getLeave(questionTypeAsTree, leaves);
//        String ids = "'" + StringUtils.join(leaves, "','") + "'";
//        List<QuestionEntity> questionsByTypesRandom = questionService.getQuestionsByTypesRandom(ids, limit);
//
//
//        BigDecimal sum = questionsByTypesRandom.stream().map(
//                QuestionEntity::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        return R.ok();
    }
}
