package com.mnnu.examine.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.RoleUserRelationDao;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 根据用户id查询关系表的角色类型
     * @param id
     * @return
     * @author JOJO
     */
    @Override
    public RoleUserRelationEntity getRoleTypeByUserId(Long id) {
        return getBaseMapper().selectOne(new QueryWrapper<RoleUserRelationEntity>().eq("user_id",id));
    }


}
