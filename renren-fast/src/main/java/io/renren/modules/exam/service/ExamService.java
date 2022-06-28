package io.renren.modules.exam.service;



import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.exam.vo.ExamCategoryVO;
import io.renren.modules.question.entity.QuestionTypeEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:58
 */
public interface ExamService extends IService<ExamEntity> {

    PageUtils queryPage(Map<String, Object> params);



}

