package io.renren.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.mall.dao.MallOrderDao;
import io.renren.modules.mall.entity.MallOrderEntity;
import io.renren.modules.mall.service.MallOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;


@Service("mallOrderService")
@Transactional(rollbackFor = Exception.class)
public class MallOrderServiceImpl extends ServiceImpl<MallOrderDao, MallOrderEntity> implements MallOrderService {
    /**
     * name 商品名
     * isPublic 是否公开
     * minCount 最小数量
     * maxCount 最高数量
     * 列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<MallOrderEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");

        String name = (String) params.get("name");
        Integer isPublic = (Integer) params.get("isPublic");
        Integer minCount = (Integer) params.get("minCount");
        Integer maxCount = (Integer) params.get("maxCount");

        if (StringUtils.isNotBlank(name)) {
            wrapper.and(w -> w.like("name", name));
        }
        if (isPublic != null && (isPublic.equals(1) || Objects.equals(isPublic, 0))) {
            wrapper.and(w -> w.eq("is_public", isPublic));
        }
        if (minCount != null) {
            wrapper.and(w -> w.ge("count", minCount));
        }
        if (maxCount != null) {
            wrapper.and(w -> w.le("count", maxCount));
        }
        if (StringUtils.isNotBlank(key)){
            wrapper.like("phone",key);
        }

        IPage<MallOrderEntity> page = this.page(
                new Query<MallOrderEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 更新兑换商城的快递单号
     *
     * @param orderId    订单id
     * @param expressNum 快递单号
     */
    @Override
    public void updateExpressNum(String orderId, String expressNum) {
        this.update(new UpdateWrapper<MallOrderEntity>().eq("order_id", orderId).set("express_num", expressNum));
    }

}
