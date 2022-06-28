package com.mnnu.examine.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.mall.dao.MallProductDao;
import com.mnnu.examine.modules.mall.entity.MallOrderEntity;
import com.mnnu.examine.modules.mall.entity.MallProductEntity;
import com.mnnu.examine.modules.mall.service.MallOrderService;
import com.mnnu.examine.modules.mall.service.MallProductService;
import com.mnnu.examine.modules.mall.vo.MallUserVO;
import com.mnnu.examine.modules.stu.entity.StuInfoEntity;
import com.mnnu.examine.modules.stu.service.StuInfoService;
import com.mnnu.examine.modules.sys.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;


/**
 * 商城产品服务impl
 *
 * @author qiaoh
 * @date 2021/12/17
 */
@Service("mallProductService")
public class MallProductServiceImpl extends ServiceImpl<MallProductDao, MallProductEntity> implements MallProductService {

    @Autowired
    UserService userService;

    @Autowired
    StuInfoService stuInfoService;

    @Resource
    MallOrderService mallOrderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MallProductEntity> page = this.page(
                new Query<MallProductEntity>().getPage(params),
                new QueryWrapper<MallProductEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 兑换商品
     *
     * @param id         id
     * @param productId  产品id
     * @param mallUserVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exchangeProduct(Long id, Long productId, MallUserVO mallUserVO) {


        // 将商品名作为锁
        synchronized (("mallProduct" + productId).intern()) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 1、是否公开，数量是否足够
            MallProductEntity productEntity = this.getById(productId);
            if (productEntity == null) {
                throw new RuntimeException("要兑换的当前商品不存在");
            }

            if (productEntity.getIsPublic() != 1) {
                throw new RuntimeException("当前商品不可兑换");
            }
            Integer count = productEntity.getCount();
            if (count < 1) {
                throw new RuntimeException("要兑换的当前商品数量不足，请换一个商品兑换");
            }
            // 2、积分是否足够生成商品订单
            StuInfoEntity infoEntity = stuInfoService.getOne(new QueryWrapper<StuInfoEntity>().eq("stu_id", id));
            if (infoEntity.getPoint().compareTo(productEntity.getPrice()) >= 0) {
                // 3、减少积分，然后生成兑换订单
                BigDecimal point = infoEntity.getPoint();
                BigDecimal price = productEntity.getPrice();
                BigDecimal result = point.subtract(price);
                infoEntity.setPoint(result);
                stuInfoService.updateById(infoEntity);
                // 更新并创建订单
                productEntity.setCount(--count);
                this.updateById(productEntity);

                MallOrderEntity orderEntity = new MallOrderEntity();
                orderEntity.setUserId(id);
                orderEntity.setOrderId(UUID.randomUUID().toString().replace("-", ""));
                orderEntity.setPoint(productEntity.getPrice());
                BeanUtils.copyProperties(mallUserVO, orderEntity);
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                orderEntity.setCreateTime(format);
                orderEntity.setUpdateTime(format);
                orderEntity.setProductId(productEntity.getId());
                orderEntity.setProductName(productEntity.getName());
                mallOrderService.saveOrUpdate(orderEntity);
            } else {
                throw new RuntimeException("积分不足");
            }
        }
    }

}
