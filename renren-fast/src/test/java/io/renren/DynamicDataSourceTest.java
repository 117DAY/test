/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.common.utils.GwyConstant;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.exam.entity.QuestionTypeEnum;
import io.renren.modules.exam.service.ExamCategoryService;
import io.renren.modules.exam.service.ExamService;
import io.renren.modules.exam.utils.WordParseUtil;
import io.renren.modules.exam.vo.ExamArgumentVO;
import io.renren.modules.exam.vo.ExamInterViewVO;
import io.renren.modules.exam.vo.ExamLineTestVO;
import io.renren.modules.oss.cloud.CloudStorageService;
import io.renren.modules.oss.cloud.OSSFactory;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.service.QuestionService;
import io.renren.modules.question.service.QuestionTextRelationService;
import io.renren.modules.question.service.QuestionTypeService;
import io.renren.modules.question.service.TextService;
import io.renren.modules.question.vo.QuestionArgumentVO;
import io.renren.modules.question.vo.QuestionLineTestVO;
import io.renren.service.DynamicDataSourceTestService;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 多数据源测试
 *
 * @author Mark sunlightcs@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {
    @Autowired
    private DynamicDataSourceTestService dynamicDataSourceTestService;

    @Autowired
    private QuestionTypeService questionTypeService;

    @Autowired
    private ExamCategoryService examCategoryService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionTextRelationService questionTextRelationService;

    @Autowired
    private TextService textService;


    @Test
    public void test() {
        Long id = 1L;

        dynamicDataSourceTestService.updateUser(id);
        dynamicDataSourceTestService.updateUserBySlave1(id);
        dynamicDataSourceTestService.updateUserBySlave2(id);
    }

    @Test
    public void testGetLineTest() {
        try {
            ExamLineTestVO lineTest = WordParseUtil.getLineTest(new FileInputStream("C:\\Users\\zheng\\Desktop\\1.docx"));
            for (QuestionLineTestVO groupedQuestion : lineTest.getGroupedQuestions()) {
                System.out.println(groupedQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getPath(List<Integer> idList, QuestionTypeEntity questionTypeEntity) {
        idList.add(questionTypeEntity.getId());
        if (questionTypeEntity.getChildren().size() == 0) {
            return idList;
        }
        return getPath(idList, questionTypeEntity.getChildren().get(0));
    }

    @Test
    public void testGetType() {
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(GwyConstant.ExamType.LT.getType());
        System.out.println(questionTypeAsTree);
    }

    @Test
    public void testGetQuestionTypeAsTree() {
        int limit = 15;
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        ObjectMapper mapper = new ObjectMapper();
        //表示为行测试卷
        examLineTestVO.setType(GwyConstant.ExamType.LT.getType());
        examLineTestVO.setLimitTime(120);
        List<ExamEntity> exams = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", GwyConstant.ExamType.LT.getType()));
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(GwyConstant.ExamType.LT.getType());
        questionTypeAsTree.forEach(e -> {
            List<Integer> leaves = new ArrayList<>();
            questionTypeService.getLeave(e.getChildren(), leaves);
            if (!"资料分析".equals(e.getTypeName())) {
                String ids = "'" + StringUtils.join(leaves, "','") + "'";
                String examIds = "'" + org.apache.commons.lang.StringUtils.join(examIdList, "','") + "'";
                List<QuestionEntity> questionsByTypesRandom = questionService.getQuestionsByTypesRandom(ids, examIds, limit);
                examLineTestVO.getGroupedQuestions().add(
                        new QuestionLineTestVO().setRootName(e.getTypeName()).setScore(questionsByTypesRandom.get(0).getScore()).setQuestions(questionsByTypesRandom));
            } else {
                //获取资料分析题的总数
                int total = questionService.count(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves));
                int start = new Random().nextInt((total - limit) / 5 + 1);
                List<QuestionEntity> questions = questionService.list(
                        new QueryWrapper<QuestionEntity>().in("type_id", leaves).last(String.format("limit %d,%d", start * 5, limit)));
                examLineTestVO.getGroupedQuestions().add(
                        new QuestionLineTestVO().setRootName(e.getTypeName()).setScore(questions.get(0).getScore()).setQuestions(questions));
            }
        });
        try {
            String s = mapper.writeValueAsString(examLineTestVO);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUploadArgument() throws IOException {
        ExamArgumentVO argument = null;
        try {
            argument = WordParseUtil.getArgument(new FileInputStream("D:\\others\\试卷\\申论\\2020年国家公务员考试申论试卷（副省级）.docx"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInterView() throws IOException {
        ExamInterViewVO examInterViewVO = WordParseUtil.getInterView(new FileInputStream("D:\\others\\test07.docx"));
        System.out.println(examInterViewVO);
        for (QuestionEntity question : examInterViewVO.getQuestions()) {
            System.out.println(question);
        }
    }

    @Test
    public void testGetTypeRoot() {
        List<QuestionTypeEntity> list = questionTypeService.list();
        QuestionTypeEntity typeEntity = questionTypeService.getById(10);
        List<QuestionTypeEntity> path = questionTypeService.getPath(new ArrayList<>(), list, typeEntity);
        Collections.reverse(path);
        QuestionTypeEntity remove = path.remove(0);
        System.out.println("root" + remove);
        System.out.println(path);

    }

    @Test
    public void testUploadLineTest() throws IOException {
        ExamLineTestVO lineTest = WordParseUtil.getLineTest(new FileInputStream("C:\\Users\\zheng\\Desktop\\2.docx"));
        System.out.println(lineTest);
    }

    @Test
    public void testGetCategoryRoot() {
        ExamEntity examEntity = examService.getById(2);
        ExamCategoryEntity categoryEntity = examCategoryService.getOne(
                new QueryWrapper<ExamCategoryEntity>().eq("id", examEntity.getCategoryId()));
        List<ExamCategoryEntity> list = examCategoryService.list();
        List<Long> rootExamCategory = examCategoryService.getPathById(new ArrayList<>(), list, categoryEntity);
        System.out.println(rootExamCategory);
        Collections.reverse(rootExamCategory);
        System.out.println(rootExamCategory);
    }

    @Test
    public void getLineTest() {
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        Integer id = 1;
        //1.查询试卷
        ExamEntity examEntity = examService.getById(id);
        //2.查询试卷对应的试题列表
        List<QuestionEntity> questionList = questionService.list(
                new QueryWrapper<QuestionEntity>().eq("exam_id", examEntity.getId()));

        //3.将试题列表按照sort排序,以及type_id分类
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
            List<Integer> collect = path.stream().map(e -> e.getId()).collect(Collectors.toList());
            //8.将路径赋予各问题
            List<QuestionEntity> list = groupedQuestions.get(integer).stream().map(e -> {
                e.setPath(collect);
                return e;
            }).collect(Collectors.toList());

            //9.若根不存在,则加入暂存map中
            if (map.get(root) == null) {
                map.put(root, list);
            }
            //9.根存在则追加list元素
            else {
                map.get(root).addAll(list);
            }
        }
        BeanUtils.copyProperties(examEntity, examLineTestVO);
        for (QuestionTypeEntity typeEntity : map.keySet()) {
            //11.查询根对应的分类树
            List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(typeList, typeEntity.getId());
            List<QuestionEntity> sortedList = map.get(typeEntity).stream().sorted(
                    Comparator.comparingInt(QuestionEntity::getSort)).collect(Collectors.toList());
            BigDecimal score = sortedList.get(0).getScore();
            examLineTestVO.getGroupedQuestions().add(new QuestionLineTestVO().setScore(score).setTypeList(typeList).setQuestions(sortedList));
        }
        System.out.println(examLineTestVO);

    }

    @Test
    public void testGetArgument() {
        ExamArgumentVO examArgumentVO = new ExamArgumentVO();
        ExamEntity examEntity = examService.getById(2);
        BeanUtils.copyProperties(examEntity, examArgumentVO);
        List<QuestionEntity> questionWithTexts = questionService.getQuestionWithTexts(2);
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
        System.out.println(examArgumentVO);
        examArgumentVO.getTexts().stream().distinct().collect(Collectors.toList()).forEach(System.out::println);
        examArgumentVO.getQuestions().forEach(System.out::println);
    }

    @Test
    public void testGetQuestionByRandom() {
        //查询所有试题分类
        List<QuestionTypeEntity> list = questionTypeService.list();

        QuestionTypeEntity typeEntity = list.stream().filter(
                questionTypeEntity -> "常识判断".equals(questionTypeEntity.getTypeName())).findFirst().get();

        //将需要查询的根节点组装为树
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(list, typeEntity.getId());


        List<Integer> leaves = new ArrayList<>();

        //获取树的叶子节点
        questionTypeService.getLeave(questionTypeAsTree, leaves);

        String ids = "'" + StringUtils.join(leaves, "','") + "'";

        System.out.println(ids);


//        List<QuestionEntity> questionsByTypesRandom = questionService.getQuestionsByTypesRandom(ids, 15);
//
//        for (QuestionEntity questionEntity : questionsByTypesRandom) {
//            System.out.println(questionEntity.getId() + " " + questionEntity.getTypeId());
//        }
    }

    @Test
    public void testGet() {
        List<Integer> limits = new ArrayList<>();
        limits.add(20);
        limits.add(40);
        limits.add(10);
        limits.add(35);
        limits.add(25);
        List<QuestionTypeEntity> tree = questionTypeService.getQuestionTypeAsTree(0);
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();
        List<ExamEntity> exams = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", GwyConstant.ExamType.LT.getType()));
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        tree = tree.get(0).getChildren();
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
                questions = questions.stream().peek(
                        questionEntity -> {
                            questionEntity.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
                            questionEntity.setId(null);
                            questionEntity.setExamId(null);
                            questionEntity.setAnalysis("");
                            questionEntity.setAnswer("");
                        }
                ).collect(Collectors.toList());
                examLineTestVO.getGroupedQuestions().add(
                        new QuestionLineTestVO().setRootName(node.getTypeName()).setScore(questions.get(0).getScore()).setQuestions(questions));
                BigDecimal sum = questions.stream().map(
                        QuestionEntity::getScore).reduce(examLineTestVO.getTotalScore(), BigDecimal::add);
                examLineTestVO.setTotalScore(sum);
            }
        }
        System.out.println(examLineTestVO);
    }

    @Test
    public void testUploadExam() {
        List<ExamEntity> list = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", 6));
        for (ExamEntity examEntity : list) {
            String examTitle = "D:\\others\\试卷\\写作\\" + examEntity.getExamTitle() + ".docx";
            try {
                String path = Objects.requireNonNull(OSSFactory.build()).uploadSuffix(new FileInputStream(examTitle), ".docx");
                examEntity.setPath(path);
                examService.updateById(examEntity);
            } catch (FileNotFoundException e) {
                System.out.println("文件不存在");
            }
        }
    }

}
