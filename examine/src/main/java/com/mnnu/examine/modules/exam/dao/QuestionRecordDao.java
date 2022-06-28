package com.mnnu.examine.modules.exam.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mnnu.examine.modules.exam.entity.ExamRecordEntity;
import com.mnnu.examine.modules.exam.entity.QuestionRecordEntity;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
@Mapper
public interface QuestionRecordDao extends BaseMapper<QuestionRecordEntity> {

    List<ExamRankVO> getRanksByExam(@Param("examId") Long examId);
}
