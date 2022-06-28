package com.mnnu.examine.modules.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.exam.entity.ExamEntity;
import com.mnnu.examine.modules.exam.vo.ExamRankVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface ExamService extends IService<ExamEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

