package com.mnnu.examine.modules.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import com.mnnu.examine.modules.exam.vo.ExamRecordVO;
import com.mnnu.examine.modules.question.entity.QuestionEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
public interface ExamRecordService extends IService<ExamRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 批改试卷
     * @return
     */
    void gradingExams(ExamRecordVO recordVO, List<QuestionEntity> list);

    /**
     * 获得排名
     * @param examId
     * @return
     */
    List<ExamRankVO> getRanksByExam(Long examId);
}

