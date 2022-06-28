package io.renren.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.exam.dao.ExamDao;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.entity.ExamEntity;
import io.renren.modules.exam.service.ExamService;
import io.renren.modules.exam.vo.ExamCategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("examService")
public class ExamServiceImpl extends ServiceImpl<ExamDao, ExamEntity> implements ExamService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ExamEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_custom", GwyConstant.ExamType.NORMAL);
        String examTitle = (String) params.get("examTitle");
        if (StringUtils.hasText(examTitle)) {
            queryWrapper.like("exam_title", examTitle);
        }
        IPage<ExamEntity> page = this.page(
                new Query<ExamEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }
}
