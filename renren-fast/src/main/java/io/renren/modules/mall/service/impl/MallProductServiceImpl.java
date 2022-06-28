package io.renren.modules.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.mall.dao.MallProductDao;
import io.renren.modules.mall.entity.MallProductEntity;
import io.renren.modules.mall.service.MallProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("mallProductService")
public class MallProductServiceImpl extends ServiceImpl<MallProductDao, MallProductEntity> implements MallProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<MallProductEntity> wrapper = new QueryWrapper<>();
        if (Integer.parseInt((String) params.get("sort")) == 1) {
            wrapper.eq("is_public", 1);
        }
        if (Integer.parseInt((String) params.get("sort")) == 2) {
            wrapper.eq("is_public", 0);
        }

        if (StringUtils.isNotBlank(key)) {
            wrapper.like("name", key);
        }
        IPage<MallProductEntity> page = this.page(
                new Query<MallProductEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}
