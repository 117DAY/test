package com.mnnu.examine.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.exam.dao.ExamRecordDao;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.service.ExamRecordService;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import com.mnnu.examine.modules.exam.vo.ExamRecordVO;
import com.mnnu.examine.modules.question.entity.QuestionEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Service("examRecordService")
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordDao, ExamRecordEntity> implements ExamRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ExamRecordEntity> wrapper = new QueryWrapper<>();

        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        wrapper.eq("user_id", userId);
        wrapper.and(queryWrapper ->
                queryWrapper.eq("commit_time", "").or().eq("is_checked", 1));
        wrapper.orderByDesc("is_custom");
        IPage<ExamRecordEntity> page = this.page(
                new Query<ExamRecordEntity>().getPage(params),
                wrapper
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

    @Override
    public List<ExamRankVO> getRanksByExam(Long examId) {
        return this.getBaseMapper().getRanksByExam(examId);
    }
}
