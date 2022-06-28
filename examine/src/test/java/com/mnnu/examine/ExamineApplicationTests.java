package com.mnnu.examine;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.AesUtils;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.exam.entity.ExamCategoryEntity;
import com.mnnu.examine.modules.exam.entity.ExamEntity;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.service.ExamCategoryService;
import com.mnnu.examine.modules.exam.service.ExamRecordService;
import com.mnnu.examine.modules.exam.service.ExamService;
import com.mnnu.examine.modules.exam.vo.*;
import com.mnnu.examine.modules.oss.utils.OSSUtils;
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
import com.mnnu.examine.modules.sys.entity.CatchphraseEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.*;
import com.mnnu.examine.modules.sys.vo.RegisterUserVO;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamineApplicationTests {

    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Resource
    BCryptPasswordEncoder passwordEncoder;

    @Resource
    ServiceService service;

    @Test
    public void contextLoads() {
        System.out.println(service.getById(2));
    }

    @Resource
    CourseService courseService;

    @Test
    public void test() {
        redisTemplate.opsForValue().set("name", "jack");
        System.out.println(redisTemplate.opsForValue().get("name"));

    }

    @Test
    public void password() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Value("${tokenKey}")
    private String key;

    @Test
    public void jwt() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_YEAR, 7);
        String token = JWT.create().setExpiresAt(instance.getTime()
        ).setPayload("id", "1").setKey(key.getBytes(StandardCharsets.UTF_8)).sign();
        System.out.println(token);
        System.out.println(JWTUtil.verify(token, key.getBytes(StandardCharsets.UTF_8)));
        System.out.println(JWTValidator.of(token).validateDate(new Date()));
    }

    @Test
    public void code() {
        String key = "127.0.0.1" + ":code";
        String rawCode = (String) redisTemplate.opsForValue().get(key);
        System.out.println(rawCode);
    }

    @Resource
    CodeService codeService;

    @Test
    public void createCodeWithIP() {
        String s1 = "/course/course/recommend%0A";
        String s2 = "/course/course/recommend";
        System.out.println(s1.contains(s2));
    }

    @Test
    public void validateCode() {
        System.out.println(codeService.validateCode("127.0.0.1", "eypw"));
    }

    @Resource
    ExamCategoryService examCategoryService;


    @Test
    public void initExamCategory() {
    }

    @Autowired
    RoleUserRelationService roleUserRelationService;

    @Test
    public void isBuyLive() {

//       System.out.println( roleUserRelationService.getUserRoleByUserId("1"));
        System.out.println(AesUtils.encrypt("123", AesUtils.SALT));
        System.out.println(AesUtils.decrypt(AesUtils.encrypt("123", AesUtils.SALT), AesUtils.SALT));
    }

    @Autowired
    UserService userService;

    @Test
    public void registerUser() {
        RegisterUserVO registerUserVO = new RegisterUserVO();
        registerUserVO.setUsername("yalieyalie");
        registerUserVO.setPassword("123456");
        registerUserVO.setPhone("15759639666");

        System.out.println(userService.registerUser(registerUserVO));
    }

    @Test
    public void testRegex() {
        boolean matches = "16712031658".matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");
        boolean matches1 = "Zheng1281202895".matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

        System.out.println(matches);
    }

    @Resource
    StringRedisTemplate template;

    @Test
    public void head() {
        redisTemplate.opsForValue().set("defaultHeadPicUrl", "https://img.mukewang.com/5882f5f70001525e01000100-200-200.jpg");
    }

    @Resource
    OSSUtils ossUtils;

    @Test
    public void find() {
        String videoName = "杜比全景声Amaze.mp4";
        try {
            System.out.println(ossUtils.authentication(videoName));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetAllPath() {
        List<String> path = new ArrayList<>();
        List<List<String>> allPath = new ArrayList<>();
        List<ExamCategoryVO> examCategoryVOS = examCategoryService.withTree();

        //设置根节点
        List<ExamCategoryVO> list = new ArrayList<>();
        ExamCategoryVO examCategoryVO = new ExamCategoryVO();
        examCategoryVO.setId(0L);
        examCategoryVO.setCategoryName("所有分类");
        examCategoryVO.setParentId(-1L);
        examCategoryVO.setSort(0);
        examCategoryVO.setChildren(examCategoryVOS);
        list.add(examCategoryVO);

        for (ExamCategoryVO categoryVO : list) {
            examCategoryService.getAllPath(categoryVO, path, allPath);
        }

        Map<String, List<String>> map = new LinkedHashMap<>();

        allPath.forEach(e -> {
            String remove = e.remove(e.size() - 1);
            String root = String.join(" / ", e);
            if (map.get(root) == null) {
                List<String> children = new ArrayList<>();
                children.add(remove);
                map.put(root, children);

            } else {
                map.get(root).add(remove);
            }
        });
        List<ExamCategoryShowVO> showVOList = new ArrayList<>();
        ExamCategoryShowVO rootVO = new ExamCategoryShowVO();
//        rootVO.setDirection("全部");
//        rootVO.getData().add("全部");

        ExamCategoryShowVO childVO = new ExamCategoryShowVO();
//        childVO.setDirection("二级");
        List<Object> childVOList = new ArrayList<>();

//        ExamCategoryShowVO defaultChildVO = new ExamCategoryShowVO();
//        defaultChildVO.setDirection("全部");
//        List<Object> defaultData = new ArrayList<>();
//        defaultData.add("不限");
//        defaultChildVO.setData(defaultData);
//        childVOList.add(defaultChildVO);

        for (String s : map.keySet()) {
            rootVO.getData().add(s);

            ExamCategoryShowVO showVO = new ExamCategoryShowVO();
            showVO.setDirection(s);
            List<Object> data = new ArrayList<>(map.get(s));
            showVO.setData(data);
            childVOList.add(showVO);
        }
        childVO.setData(childVOList);
        showVOList.add(rootVO);
        showVOList.add(childVO);

        for (ExamCategoryShowVO showVO : showVOList) {
            System.out.println(showVO);
        }

    }

    @Resource
    QuestionTypeService questionTypeService;

    @Test
    public void testGetRoot() {
        List<QuestionTypeEntity> list = questionTypeService.list();
        QuestionTypeEntity typeEntity = questionTypeService.getById(15);
        QuestionTypeEntity root = questionTypeService.getRoot(list, typeEntity);
        System.out.println(root);

    }


    @Test
    public void d() {
        String s = "dwa,dddd";
        System.out.println(s.split(",")[1]);
    }

    @Resource
    ExamRecordService examRecordService;

    @Test
    public void testGetExamRecord() {
        ExamRecordEntity recordEntity = examRecordService.getOne(
                new QueryWrapper<ExamRecordEntity>()
                        .eq("user_id", 60).eq("exam_id", 19)
                        .eq("commit_time", ""));
        System.out.println(recordEntity);

    }

    @Test
    public void getRoot() {
        ExamCategoryEntity categoryEntity = examCategoryService.getOne(
                new QueryWrapper<ExamCategoryEntity>().eq("category_name", "福建"));
        List<ExamCategoryEntity> list = examCategoryService.list();
        ExamCategoryEntity root = examCategoryService.getRoot(list, categoryEntity);
        System.out.println(root);
    }

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamService examService;

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
    public void testGetSpecialized() {
        Integer questionTypeId = 29;
        Integer num = 15;
        Integer typeId = 1;
        String questionTypeName = "增长率";
        //查询所有试卷分类
        List<QuestionTypeEntity> questionTypeList = questionTypeService.list();
        //找出组卷的分类
        QuestionTypeEntity typeEntity = questionTypeList.stream().filter(
                e -> e.getId().equals(questionTypeId)).findFirst().get();
        //获取分类对应的分类树
        List<QuestionTypeEntity> questionTypeAsTree = questionTypeService.getQuestionTypeAsTree(questionTypeList, questionTypeId);
        QuestionTypeEntity root = questionTypeService.getRoot(questionTypeList, typeEntity);
        List<ExamEntity> exams = examService.list(
                new QueryWrapper<ExamEntity>().eq("type", typeId));
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
            typeIds = "'" + questionTypeId + "'";
        }
        if (!GwyConstant.ExamType.DATA_ANALYSIS.getName().equals(root.getTypeName())) {
            specialized = questionService.getQuestionsByTypesRandom(typeIds, examIds, num);
        } else {
            //获取资料分析题的总数
            List<Integer> list = leaves.size() != 0 ? leaves : Collections.singletonList(questionTypeId);
            int total = questionService.count(
                    new QueryWrapper<QuestionEntity>().in("type_id", list)
                            .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType()));
            if (total < num) {
                return;
            }
            int start = new Random().nextInt((total - num) / 5 + 1);
            specialized = questionService.list(
                    new QueryWrapper<QuestionEntity>().in("type_id", list)
                            .in("exam_id", examIdList).eq("is_custom", GwyConstant.ExamType.NORMAL.getType())
                            .last(String.format("limit %d,%d", start * 5, num)));
        }
        if (!num.equals(specialized.size())) {
            System.out.println("题库题目数量不足!");
        }
        examEntity.setExamTitle("专项练习(" + questionTypeName + ")");
        examEntity.setType(typeId);
        examEntity.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
        examEntity.setLimitTime(60);
        examEntity.setTotalScore(new BigDecimal("0.00"));
        BigDecimal sum = specialized.stream().map(
                QuestionEntity::getScore).reduce(examEntity.getTotalScore(), BigDecimal::add);
        examEntity.setTotalScore(sum);
        AtomicInteger sort = new AtomicInteger(1);
        List<QuestionEntity> collect = specialized.stream().peek(e -> {
            e.setExamId(examEntity.getId());
            e.setIsCustom(GwyConstant.ExamType.CUSTOM.getType());
            e.setId(null);
            e.setSort(sort.getAndIncrement());
            e.setCreateTime("");
            e.setUpdateTime("");
        }).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    @Autowired
    QuestionTextRelationService questionTextRelationService;

    @Autowired
    TextService textService;

    @Test
    public void testCount() {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(10);
        QueryWrapper<ExamEntity> wrapper = new QueryWrapper<ExamEntity>().eq("type", 6)
                .eq("is_custom", GwyConstant.ExamType.NORMAL.getType());
        if (!CollectionUtils.isEmpty(categoryIds)) {
            wrapper.in("category_id", categoryIds);
        }
        List<ExamEntity> exams = examService.list(wrapper);
        List<Long> examIdList = exams.stream().map(
                ExamEntity::getId).collect(Collectors.toList());
        if (examIdList.size() == 0) {
            System.out.println("该分类条件下暂无试卷!");
        }
        int count = questionService.count(
                new QueryWrapper<QuestionEntity>().in("exam_id", examIdList));
        if (count == 0) {
            System.out.println("题库题目数量不足!");
        }
        int random = new Random().nextInt(count);

        QuestionEntity question = questionService.getOne(
                new QueryWrapper<QuestionEntity>().in("exam_id", examIdList).last(String.format("limit %d,%d", random, 1)));

        List<QuestionTextRelationEntity> relationList = questionTextRelationService.list(
                new QueryWrapper<QuestionTextRelationEntity>().eq("question_id", question.getId()));

        List<Integer> textIds = relationList.stream().map(
                QuestionTextRelationEntity::getQuestionTextId).collect(Collectors.toList());

        List<TextEntity> texts = textService.listByIds(textIds);

        ExamArgumentVO examArgumentVO = new ExamArgumentVO();

        examArgumentVO.setExamTitle("单题练习(申论)");
        examArgumentVO.setType(GwyConstant.ExamType.AT.getType());
        examArgumentVO.setLimitTime(60);
        AtomicInteger sort = new AtomicInteger(1);
        List<TextEntity> collect = texts.stream().peek(e -> {
            e.setName(sort.get() + "");
            e.setSort(sort.getAndIncrement());
        }).collect(Collectors.toList());

        QuestionArgumentVO questionArgumentVO = new QuestionArgumentVO();
        BeanUtils.copyProperties(question, questionArgumentVO);
        questionArgumentVO.setSort(1);
        questionArgumentVO.setName("1");

        examArgumentVO.setQuestions(Collections.singletonList(questionArgumentVO));
        examArgumentVO.setTexts(collect);
    }

    @Test
    public void getInterviewQuestions() {
        List<QuestionEntity> questions = questionService.getQuestionsByExamTypeRandom(4, 15);
        questions.forEach(System.out::println);
    }

    @Test
    public void getTree() {
        List<ExamCategoryVO> categoryVOList = examCategoryService.withTree(true);
        categoryVOList.forEach(e -> {
            ExamCategoryVO lt = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.LT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.LT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO at = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.AT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.AT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO it = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.TITORIT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.TITORIT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO st = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.ST.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.ST.getName())
                    .setParentId(GwyConstant.ExamType.SYDW.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO wt = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.WT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.WT.getName())
                    .setParentId(GwyConstant.ExamType.SYDW.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            if (GwyConstant.ExamType.GWY.getType().longValue() == e.getId()) {
                lt.setChildren(e.getChildren());
                at.setChildren(e.getChildren());
                it.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(lt, at, it));
            } else if (GwyConstant.ExamType.SYDW.getType().longValue() == e.getId()) {
                st.setChildren(e.getChildren());
                wt.setChildren(e.getChildren());
                it.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(st, wt, it));
            } else {
                lt.setChildren(e.getChildren());
                at.setChildren(e.getChildren());
                it.setChildren(e.getChildren());
                st.setChildren(e.getChildren());
                wt.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(lt, at, st, wt, it));
            }
        });
        categoryVOList.forEach(System.out::println);
    }

    @Autowired
    CatchphraseService catchphraseService;

    @Test
    public void testGetWords() {
        List<ExamCategoryVO> categoryVOList = examCategoryService.withTree(true);
        categoryVOList.forEach(System.out::println);
    }

    @Test
    public void testGetRank() {
        List<ExamRankVO> ranksByExam = examRecordService.getRanksByExam(19L);
        ranksByExam.forEach(System.out::println);
    }

}
