package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.QQServiceDao;
import io.renren.modules.sys.entity.QQServiceEntity;
import io.renren.modules.sys.service.QQServiceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("serviceService")
public class QQServiceServiceImpl extends ServiceImpl<QQServiceDao, QQServiceEntity> implements QQServiceService {

    /**
     * 传入手机号、姓名、qq号
     * phone
     * name
     * qqNum
     * 列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QQServiceEntity> wrapper = new QueryWrapper<>();

        String phone = (String) params.get("phone");
        String name = (String) params.get("name");
        String qqNum = (String) params.get("qqNum");

        if (StringUtils.isNotBlank(phone)) {
            wrapper.and(w -> {
                w.like("phone", phone);
            });
        }


        if (StringUtils.isNotBlank(name)) {
            wrapper.and(w -> {
                w.like("name", name);
            });
        }

        if (StringUtils.isNotBlank(qqNum)) {
            wrapper.and(w -> {
                w.like("qq_link", qqNum);
            });
        }

        IPage<QQServiceEntity> page = this.page(
                new Query<QQServiceEntity>().getPage(params), wrapper
        );
        return new PageUtils(page);
    }

}