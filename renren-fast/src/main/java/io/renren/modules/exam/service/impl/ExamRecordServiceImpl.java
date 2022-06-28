package io.renren.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.exam.dao.ExamRecordDao;
import io.renren.modules.exam.entity.ExamRecordEntity;
import io.renren.modules.exam.vo.ExamRecordVO;
import io.renren.modules.exam.service.ExamRecordService;
import io.renren.modules.question.entity.QuestionEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("examRecordService")
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordDao, ExamRecordEntity> implements ExamRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamRecordEntity> page = this.page(
                new Query<ExamRecordEntity>().getPage(params),
                new QueryWrapper<ExamRecordEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 异步调用实现批改试卷
     *
     * @return
     */
    @Override
    @Async
    public void gradingExams(ExamRecordVO recordVO, List<QuestionEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            QuestionEntity questionEntity = list.get(i);
            String userAnswer = recordVO.getUserAnswers().get(i);
            if (questionEntity.getAnswer().equals(userAnswer)) {
                recordVO.setScore(recordVO.getScore().add(questionEntity.getScore()));
            }
        }
        ExamRecordEntity recordEntity = new ExamRecordEntity();
        BeanUtils.copyProperties(recordVO, recordEntity);
        String answers = String.join("&", recordVO.getUserAnswers());
        recordEntity.setIsChecked(1);
        recordEntity.setUserAnswers(answers);
        this.updateById(recordEntity);
    }


}
