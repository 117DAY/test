package com.mnnu.examine.modules.mall.service.impl;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.mall.dao.MallOrderDao;
import com.mnnu.examine.modules.mall.entity.MallOrderEntity;
import com.mnnu.examine.modules.mall.service.MallOrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("mallOrderService")
public class MallOrderServiceImpl extends ServiceImpl<MallOrderDao, MallOrderEntity> implements MallOrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MallOrderEntity> page = this.page(
                new Query<MallOrderEntity>().getPage(params),
                new QueryWrapper<MallOrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getCourseListByUserId(Map<String, Object> params, Long userId) {
        QueryWrapper<MallOrderEntity> wrapper= new QueryWrapper<MallOrderEntity>().
                eq("user_id", userId).orderByDesc("create_time");
        IPage<MallOrderEntity> page = this.page(
                new Query<MallOrderEntity>().getPage(params), wrapper
        );
        PageUtils orderPage = new PageUtils(page);
        List<MallOrderEntity> orderList = (List<MallOrderEntity>) orderPage.getList();
        System.out.println(orderList);
        orderPage.setList(orderList);
        return orderPage;
    }

}
