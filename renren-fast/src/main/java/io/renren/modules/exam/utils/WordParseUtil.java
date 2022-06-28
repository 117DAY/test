package io.renren.modules.exam.utils;


import io.renren.common.exception.RRException;
import io.renren.common.utils.GwyConstant;
import io.renren.modules.exam.entity.QuestionTypeEnum;
import io.renren.modules.exam.vo.*;
import io.renren.modules.oss.cloud.CloudStorageService;
import io.renren.modules.oss.cloud.OSSFactory;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.vo.QuestionArgumentVO;
import io.renren.modules.question.vo.QuestionLineTestVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 获取试卷工具类
 *
 * @author jljy
 */
public class WordParseUtil {

    /**
     * 获取行测试卷
     *
     * @param in
     * @return
     */
    public static ExamLineTestVO getLineTest(InputStream in) throws IOException {
        ExamLineTestVO examLineTestVO = new ExamLineTestVO();

        //表示为行测试卷
        examLineTestVO.setType(GwyConstant.ExamType.LT.getType());

        examLineTestVO.setLimitTime(120);

        //读取试卷
        XWPFDocument doc = new XWPFDocument(in);
        List<IBodyElement> paragraphs = doc.getBodyElements();

        // 获取试卷标题，如:2021年福建省公务员录用考试《行测》卷
        String title = ((XWPFParagraph) paragraphs.get(0)).getParagraphText();

        //设置试卷标题
        examLineTestVO.setExamTitle(title);

        for (int i = 1; i < paragraphs.size(); i++) {
            //首先匹配大题(列如:一、常识判断（每题0.8分)
            String paragraphText = ((XWPFParagraph) paragraphs.get(i)).getParagraphText();
            boolean match = matchQuestionType(paragraphText);
            // 表示匹配到了大题，例如： 一、常识判断（每题0.8分）
            if (match) {
                //创建每个大题对应的试题列表
                List<QuestionEntity> list = new ArrayList<>();
                //提取分数
                BigDecimal score = getScore(paragraphText);
                //提取大题对应的名字
                String typeName = getQuestionTypeName(paragraphText);
                //获取每个大题对应的试题列表
                i = getChoiceQuestionList(i, typeName, score, paragraphs, list);
                //将提取到的信息保存VO
                examLineTestVO.getGroupedQuestions().add(new QuestionLineTestVO().setRootName(typeName).setScore(score).setQuestions(list));
            } else {
                //如果没有匹配到指定格式的大题,且内容不为空行,则认为大题格式错误
                if (StringUtils.isNotBlank(paragraphText)) {
                    throw new RRException("大题格式错误,请检查!");
                }
            }
        }
        //根据名字去重
        List<QuestionLineTestVO> collect = examLineTestVO.getGroupedQuestions().stream()
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(QuestionLineTestVO::getRootName))), ArrayList::new));

        if (collect.size() != 5) {
            throw new RRException("大题数量错误或大题名称重复,请检查!");
        }
        return examLineTestVO;
    }

    /**
     * 获取申论试卷
     *
     * @param in
     * @return
     */
    public static ExamArgumentVO getArgument(InputStream in) throws IOException {
        ExamArgumentVO examArgumentVO = new ExamArgumentVO();

        //表示为申论试卷
        examArgumentVO.setType(GwyConstant.ExamType.AT.getType());

        examArgumentVO.setLimitTime(180);

        examArgumentVO.setTexts(new ArrayList<>());
        examArgumentVO.setQuestions(new ArrayList<>());


        //读取试卷
        XWPFDocument doc = new XWPFDocument(in);
        List<IBodyElement> paragraphs = doc.getBodyElements();

        //获取标题,如：2020年0725公务员多省联考《申论》题（福建县级卷）
        String title = ((XWPFParagraph) paragraphs.get(0)).getParagraphText();
        examArgumentVO.setExamTitle(title);
        for (int i = 1; i < paragraphs.size(); i++) {
            //匹配到空行则返回
            String text = getContent(paragraphs.get(i), new StringBuilder());
            if (StringUtils.isBlank(text)) {
                continue;
            }
            //表示匹配到【材料】
            if (text.contains(QuestionTypeEnum.TEXT.getType())) {
                i++;
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.QUESTION.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    text += content;
                    i++;
                }
                String[] texts = text.split(QuestionTypeEnum.TEXT.getType());
                if (texts.length == 1) {
                    throw new RRException(QuestionTypeEnum.TEXT.getType() + "标识符格式错误,请检查!");
                }
                for (int j = 0; j < texts.length; j++) {
                    if (StringUtils.isNotBlank(texts[j])) {
                        TextEntity t = new TextEntity();
                        t.setTextContent(texts[j]);
                        t.setName(j + "");
                        examArgumentVO.getTexts().add(t);
                    }
                }
            }
            String question = getContent(paragraphs.get(i), new StringBuilder());

            //表示匹配到【问题】
            if (question.contains(QuestionTypeEnum.QUESTION.getType())) {
                i++;
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANSWER.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    question += content;
                    i++;
                }
            }
            //表示匹配到答案
            String answer = getContent(paragraphs.get(i), new StringBuilder());
            if (answer.contains(QuestionTypeEnum.ANSWER.getType())) {
                i++;
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.FINISH.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    answer += content;
                    i++;
                }
            }
            String[] questions = question.split(QuestionTypeEnum.QUESTION.getType());
            String[] answers = answer.split(QuestionTypeEnum.ANSWER.getType());

            if (questions.length == 1) {
                throw new RRException(QuestionTypeEnum.QUESTION.getType() + "标识符格式错误，请检查!");
            }
            if (answers.length == 1) {
                throw new RRException(QuestionTypeEnum.ANSWER.getType() + "标识符格式错误,请检查!");
            }
            if (questions.length != answers.length) {
                throw new RRException(QuestionTypeEnum.QUESTION.getType() + "数量必须与" + QuestionTypeEnum.ANSWER.getType() + "一致!");
            }
            for (int j = 0; j < questions.length; j++) {
                if (StringUtils.isNotBlank(questions[j]) && StringUtils.isNotBlank(answers[j])) {
                    QuestionArgumentVO q = new QuestionArgumentVO();
                    q.setQuestionTopic(questions[j]);
                    q.setAnswer(answers[j]);
                    q.setName(j + "");
                    examArgumentVO.getQuestions().add(q);
                }
            }
        }
        return examArgumentVO;
    }

    /**
     * 获取综合试卷
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ExamLineTestVO getSynthesis(InputStream in) throws IOException {
        ExamLineTestVO synthesis = getLineTest(in);
        synthesis.setType(GwyConstant.ExamType.ST.getType());
        return synthesis;
    }

    /**
     * 获取写作试卷
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ExamArgumentVO getWriting(InputStream in) throws IOException {
        ExamArgumentVO writing = getArgument(in);
        writing.setType(GwyConstant.ExamType.WT.getType());
        return writing;
    }

    /**
     * 获取材料面试试卷
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static ExamArgumentVO getTextInterView(InputStream in) throws IOException {
        ExamArgumentVO textinterview = getArgument(in);
        textinterview.setType(GwyConstant.ExamType.TIT.getType());
        return textinterview;
    }


    /**
     * 获取问题面试试卷
     *
     * @param in
     * @return
     */
    public static ExamInterViewVO getInterView(InputStream in) throws IOException {
        ExamInterViewVO examInterViewVO = new ExamInterViewVO();
        examInterViewVO.setQuestions(new ArrayList<>());

        //表示为面试试卷
        examInterViewVO.setType(GwyConstant.ExamType.IT.getType());
        examInterViewVO.setLimitTime(60);

        //读取试卷
        XWPFDocument doc = new XWPFDocument(in);
        List<IBodyElement> paragraphs = doc.getBodyElements();

        //获取标题,如：2020年9月16日上午福建省考面试题
        String title = ((XWPFParagraph) paragraphs.get(0)).getParagraphText();
        examInterViewVO.setExamTitle(title);
        for (int i = 1; i < paragraphs.size(); i++) {
            String question = getContent(paragraphs.get(i), new StringBuilder());
            if (question.contains(QuestionTypeEnum.QUESTION.getType())) {
                i++;
                //如果不是以【答案】结尾,拼接多行【问题】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANSWER.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    question += content;
                    i++;
                }
            }
            String answer = getContent(paragraphs.get(i), new StringBuilder());
            if (answer.contains(QuestionTypeEnum.ANSWER.getType())) {
                i++;
                //如果不是以【结束】结尾,拼接多行【答案】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.FINISH.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    answer += content;
                    i++;
                }
            }
            String[] questions = question.split(QuestionTypeEnum.QUESTION.getType());
            String[] answers = answer.split(QuestionTypeEnum.ANSWER.getType());

            if (questions.length == 1) {
                throw new RRException(QuestionTypeEnum.QUESTION.getType() + "标识符格式错误，请检查!");
            }
            if (answers.length == 1) {
                throw new RRException(QuestionTypeEnum.ANSWER.getType() + "标识符格式错误,请检查!");
            }

            if (questions.length != answers.length) {
                throw new RRException(QuestionTypeEnum.QUESTION.getType() + "数量必须与" + QuestionTypeEnum.ANSWER.getType() + "一致!");
            }
            for (int j = 0; j < questions.length; j++) {
                if (StringUtils.isNotBlank(questions[j]) && StringUtils.isNotBlank(answers[j])) {
                    QuestionEntity q = new QuestionEntity();
                    q.setQuestionTopic(questions[j]);
                    q.setAnswer(answers[j]);
                    q.setAnalysis(j + "");
                    examInterViewVO.getQuestions().add(q);
                }
            }
        }
        return examInterViewVO;
    }


    /**
     * 判断字符串是否含有标识符
     *
     * @param str
     * @return
     */
    private static boolean matchIdentifier(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.IDENTIFIER_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 获取大题类型对应的名字
     *
     * @return
     */
    private static String getQuestionTypeName(String str) {
        //判断所给的大题信息是否符合格式
        Pattern p = Pattern.compile(QuestionTypeEnum.TYPE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    /**
     * 判断题目类型是否符合指定格式
     *
     * @param str
     * @return
     */
    private static boolean matchQuestionType(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.QUESTION_TYPE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取题目类型对应的试题分数
     *
     * @param str
     * @return
     */
    private static BigDecimal getScore(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.SCORE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return new BigDecimal(m.group());
        }
        return null;
    }

    /**
     * 获取某个大题内的试题列表
     *
     * @param i
     * @param score
     * @param paragraphs
     * @return
     */
    private static int getChoiceQuestionList(int i, String rootName, BigDecimal score, List<IBodyElement> paragraphs, List<QuestionEntity> list) {
        i++;
        for (i = i; i < paragraphs.size(); i++) {
            //首先匹配大题(列如:一、常识判断，共20题。（每题0.8分)
            String contentType = getContent(paragraphs.get(i), new StringBuilder());
            //如果为空直接返回,不做处理
            if (StringUtils.isBlank(contentType)) {
                return i;
            }
            // 如果匹配到大题则返回
            boolean match = matchQuestionType(contentType);
            if (match) {
                i--;
                return i;
            }

            //如果存在资料,则获取题目中的【资料】信息
            String data = getContent(paragraphs.get(i), new StringBuilder());
            if (data.contains(QuestionTypeEnum.DATA.getType())) {
                i++;
                //如果标签不是以【选项】结尾，拼接多行【资料】以及【题文】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.CHOICE.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    data += content;
                    i++;
                }
                //将获取的【资料】和【题文】用\n来分隔
                data = data.replaceAll(QuestionTypeEnum.TOPIC.getType(), "\n");
            }
            //如果不存在【资料】,则单独提取【题文】信息
            else {
                if (data.contains(QuestionTypeEnum.TOPIC.getType())) {
                    i++;
                    //截取掉【题文】标识符
                    data = data.replaceAll(QuestionTypeEnum.TOPIC.getType(), "");
                    //如果标签不是以【选项】结尾，拼接多行【题文】信息
                    while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.CHOICE.getType())) {
                        String content = getContent(paragraphs.get(i), new StringBuilder());
                        data += content;
                        i++;
                    }
                }

            }
            //获取题目中的【选项】信息
            String choice = getContent(paragraphs.get(i), new StringBuilder());
            if (choice.contains(QuestionTypeEnum.CHOICE.getType())) {
                i++;
                //截取掉【选项】标识符
                choice = choice.replaceAll(QuestionTypeEnum.CHOICE.getType(), "");
                //如果标签不是以【答案】结尾，拼接多行【选项】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANSWER.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    choice += content;
                    i++;
                }
            }

            //获取题目的【答案】信息
            String answer = getContent(paragraphs.get(i), new StringBuilder());
            if (answer.contains(QuestionTypeEnum.ANSWER.getType())) {
                i++;
                //截取掉【答案】标识符
                answer = answer.replaceAll(QuestionTypeEnum.ANSWER.getType(), "");
                //如果标签不是以【解析】结尾，拼接多行【答案】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANALYSIS.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    answer += content;
                    i++;
                }
            }

            //获取题目的【解析】信息
            String analysis = getContent(paragraphs.get(i), new StringBuilder());

            if (analysis.contains(QuestionTypeEnum.ANALYSIS.getType())) {
                i++;
                //截取掉【解析】标识符
                analysis = analysis.replaceAll(QuestionTypeEnum.ANALYSIS.getType(), "");
                //如果标签不是以【结束】结尾，拼接多行【解析】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.FINISH.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    analysis += content;
                    i++;
                }
            }
            //验证合法性
            choice = checkValidate(data, choice, answer, analysis);
            QuestionEntity q = new QuestionEntity();
            q.setQuestionTopic(data);
            q.setChoice(choice);
            q.setAnswer(answer.trim());
            q.setAnalysis(analysis);
            q.setScore(score == null ? new BigDecimal("1.00") : score);
            list.add(q);
        }
        return i;
    }

    /**
     * 检查提取的数据的合法性,若合法则返回处理好的选项值
     *
     * @param data
     * @param choice
     * @param answer
     * @param analysis
     * @return
     */
    private static String checkValidate(String data, String choice, String answer, String analysis) {
        //验证【题文】合法性
        if (StringUtils.isBlank(data)) {
            throw new RRException(QuestionTypeEnum.TOPIC.getType() + "内容不能为空,请检查!");
        }
        String slice = data.length() > 15 ? data.substring(0, 14) : data;
        //如果提取出来的数据仍然包含标识符，视为文档格式错误
        if (matchIdentifier(data + choice + answer + analysis)) {
            throw new RRException("标识符格式错误,请检查\"" + slice + "...\"附近内容");
        }
        //验证【选项】合法性
        int indexA = choice.indexOf("A、");
        int indexB = choice.indexOf("B、");
        int indexC = choice.indexOf("C、");
        int indexD = choice.indexOf("D、");
        if (indexA == -1 || indexB == -1 || indexC == -1 || indexD == -1) {
            throw new RRException(QuestionTypeEnum.CHOICE.getType() + "内容格式错误,请检查\"" + slice + "...\"附近内容");
        }
        try {
            String choiceA = choice.substring(indexA + 2, indexB).trim();
            String choiceB = choice.substring(indexB + 2, indexC).trim();
            String choiceC = choice.substring(indexC + 2, indexD).trim();
            String choiceD = choice.substring(indexD + 2).trim();
            if (StringUtils.isNotBlank(choiceA) && StringUtils.isNotBlank(choiceB)
                    && StringUtils.isNotBlank(choiceC) && StringUtils.isNotBlank(choiceD)) {
                choice = choiceA + "\n" + choiceB + "\n" + choiceC + "\n" + choiceD;
            } else {
                choice = "";
            }
        } catch (Exception e) {
            throw new RRException(QuestionTypeEnum.CHOICE.getType() + "内容格式错误,请检查\"" + slice + "...\"附近内容");
        }
        if (StringUtils.isBlank(choice)) {
            throw new RRException(QuestionTypeEnum.CHOICE.getType() + "内容不能为空,请检查\"" + slice + "...\"附近内容");
        }
        //验证【答案】合法性
        answer = answer.trim();
        if (!("A".equals(answer) || "B".equals(answer) || "C".equals(answer) || "D".equals(answer))) {
            throw new RRException(QuestionTypeEnum.ANSWER.getType() + "内容格式错误!请检查\"" + slice + "...\"附近内容");
        }
        //验证【解析】合法性
        if (StringUtils.isBlank(analysis)) {
            throw new RRException(QuestionTypeEnum.ANALYSIS.getType() + "内容不能为空!请检查\"" + slice + "...\"附近内容");
        }
        return choice;
    }

    /**
     * 获取一个段落的内容
     *
     * @param body
     * @param content
     * @return
     */
    private static String getContent(IBodyElement body, StringBuilder content) {
        //将word中的图片存储到本地磁盘中
        ImageParseUtil imageParse = new ImageParseUtil();
        //拿到所有的段落的表格，这两个属于同级无素
        if (body.getElementType().equals(BodyElementType.PARAGRAPH)) {
            //处理段落中的文本以及公式图片
            handleParagraph(content, body, imageParse);
        }
        return content.toString();
    }

    /**
     * 处理段落
     *
     * @param content
     * @param body
     * @param imageParser
     */
    private static void handleParagraph(StringBuilder content, IBodyElement body, ImageParseUtil imageParser) {
        XWPFParagraph p = (XWPFParagraph) body;
        if (p.isEmpty() || p.isWordWrap() || p.isPageBreak()) {
            return;
        }
        ParagraphChildUtil runOrMaths = new ParagraphChildUtil(p);
        List<Object> childList = runOrMaths.getChildList();

        for (Object child : childList) {
            if (child instanceof XWPFRun) {
                //处理段落中的文本以及图片

                handleParagraphRun(content, (XWPFRun) child, imageParser);
            }
        }
    }

    /**
     * 处理图片
     *
     * @param content
     * @param run
     * @param imageParser
     */
    private static void handleParagraphRun(StringBuilder content, XWPFRun run, ImageParseUtil imageParser) {
        // 有内嵌的图片
        List<XWPFPicture> pics = run.getEmbeddedPictures();
        if (pics != null && pics.size() > 0) {
            handleParagraphRunsImage(content, pics, imageParser);
        } else {
            //纯文本直接获取
            content.append(run.toString());
        }
    }

    /**
     * 获取图片
     *
     * @param content
     * @param pics
     * @param imageParser
     */
    private static void handleParagraphRunsImage(StringBuilder content, List<XWPFPicture> pics, ImageParseUtil imageParser) {
        for (XWPFPicture pic : pics) {
            //这里已经获取好了
            String path = imageParser.parse(pic.getPictureData().getData(),
                    pic.getPictureData().getFileName());
            content.append(String.format("<img src=\"%s\" />", path));
        }
    }
}
