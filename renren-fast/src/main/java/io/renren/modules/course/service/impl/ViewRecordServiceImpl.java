package io.renren.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.dao.ViewRecordDao;
import io.renren.modules.course.entity.ViewRecordEntity;
import io.renren.modules.course.service.ViewRecordService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("viewRecordService")
public class ViewRecordServiceImpl extends ServiceImpl<ViewRecordDao, ViewRecordEntity> implements ViewRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ViewRecordEntity> page = this.page(
                new Query<ViewRecordEntity>().getPage(params),
                new QueryWrapper<ViewRecordEntity>()
        );

        return new PageUtils(page);
    }
    /**
     *
     *
     * @description: 获取用户观看历史记录
     * @param page
     * @param size
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/26 17:47
     */
    @Override
    public PageUtils getViewRecordListByUserId(Map<String,Object> params,Long userId){
        QueryWrapper<ViewRecordEntity> wrapper= new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        IPage<ViewRecordEntity> page = this.page(
                new Query<ViewRecordEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }





}
