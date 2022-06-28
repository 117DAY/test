package com.mnnu.examine.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;

import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:58
 */
public interface RoleUserRelationService extends IService<RoleUserRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /*
     * 获取用户角色,查询失败返回-1
     * params:
     * userId
     *
     * * author: Geralt
     * */
    public int getUserRoleByUserId(String userId);

    /**
     * 根据用户id查询关系表中的角色
     * @param id
     * @return
     * @author JOJO
     */
     RoleUserRelationEntity getRoleTypeByUserId(Long id);
}

