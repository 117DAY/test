package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.RoleUserRelationDao;
import io.renren.modules.sys.entity.RoleUserRelationEntity;
import io.renren.modules.sys.service.RoleUserRelationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("roleUserRelationService")
public class RoleUserRelationServiceImpl extends ServiceImpl<RoleUserRelationDao, RoleUserRelationEntity> implements RoleUserRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<RoleUserRelationEntity> page = this.page(
                new Query<RoleUserRelationEntity>().getPage(params),
                new QueryWrapper<RoleUserRelationEntity>()
        );

        return new PageUtils(page);
    }
    /*
     * 获取用户角色,查询失败返回-1
     * params:
     * userId
     *
     * * author: Geralt
     * */
    @Override
    public int getUserRoleByUserId(String userId) {
        try {

        return getBaseMapper()
                .selectOne(new QueryWrapper<RoleUserRelationEntity>()
                        .eq("user_id", userId)).getRoleId();        }
        catch (Exception e){
            return -1;
        }

    }


}
