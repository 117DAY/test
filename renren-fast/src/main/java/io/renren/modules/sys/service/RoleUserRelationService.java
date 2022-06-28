package io.renren.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.RoleUserRelationEntity;

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
}

