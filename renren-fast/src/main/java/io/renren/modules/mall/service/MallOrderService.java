package io.renren.modules.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.mall.entity.MallOrderEntity;

import java.util.Map;

/**
 * 商城订单服务
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 22:38:40
 */
public interface MallOrderService extends IService<MallOrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新兑换商城的快递单号
     *
     * @param orderId    订单id
     * @param expressNum 快递单号
     */
    void updateExpressNum(String orderId, String expressNum);
}

