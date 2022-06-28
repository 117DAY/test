package io.renren.modules.exam.service;



import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamRecordEntity;
import io.renren.modules.exam.vo.ExamRecordVO;
import io.renren.modules.question.entity.QuestionEntity;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
public interface ExamRecordService extends IService<ExamRecordEntity> {

    PageUtils queryPage(Map<String, Object> params);


    /**
     * 批改试卷
     * @param recordVO
     * @param list
     */
    void gradingExams(ExamRecordVO recordVO, List<QuestionEntity> list);
}

