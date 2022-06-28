package com.mnnu.examine.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.sys.entity.RefundEntity;

import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
public interface RefundService extends IService<RefundEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存退款信息 供管理人员查看
     *
     * @param courseOrderEntity 当然订单实体
     */
    void saveRefundInfo(CourseOrderEntity courseOrderEntity);
}

