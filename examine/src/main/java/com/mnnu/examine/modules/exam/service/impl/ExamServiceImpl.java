package com.mnnu.examine.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.exam.dao.ExamDao;
import com.mnnu.examine.modules.exam.entity.ExamEntity;
import com.mnnu.examine.modules.exam.service.ExamService;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("examService")
public class ExamServiceImpl extends ServiceImpl<ExamDao, ExamEntity> implements ExamService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<ExamEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("is_custom", GwyConstant.ExamType.NORMAL);
        Integer type = Integer.valueOf((String) params.get("type"));
        Long categoryId = (Long) params.get("categoryId");
        String examTitle = (String) params.get("examTitle");
        if (Objects.nonNull(categoryId)) {
            wrapper.eq("category_id", categoryId);
        }
        if (StringUtils.hasText(examTitle)) {
            wrapper.like("exam_title", examTitle);
        }
        if (GwyConstant.ExamType.TIT.getType().equals(type)
                || GwyConstant.ExamType.IT.getType().equals(type)) {
            wrapper.and(QueryWrapper ->
                    QueryWrapper.eq("type", GwyConstant.ExamType.TIT.getType())
                            .or().eq("type", GwyConstant.ExamType.IT.getType()));
        } else {
            wrapper.eq("type", type);
        }
        IPage<ExamEntity> page = this.page(
                new Query<ExamEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

}
