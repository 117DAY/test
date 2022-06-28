package io.renren.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.sys.dao.GwyUserDao;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.entity.RoleUserRelationEntity;
import io.renren.modules.sys.service.GwyUserService;
import io.renren.modules.sys.service.RoleUserRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * impl gwy用户服务
 *
 * @author qiaoh
 * @date 2021/12/06
 */
@Service("gwyUserService")
public class GwyUserServiceImpl extends ServiceImpl<GwyUserDao, GwyUserEntity> implements GwyUserService {

    @Resource
    RoleUserRelationService roleUserRelationService;

    @Override
    @Transactional(readOnly = true)
    public PageUtils queryPage(Map<String, Object> params) {
        List<RoleUserRelationEntity> list = roleUserRelationService.list(
                new QueryWrapper<RoleUserRelationEntity>()
                        .eq("role_id", GwyConstant.RoleConstant.STUDENT.getId()));
        List<Long> userIds = list.stream().map(RoleUserRelationEntity::getUserId).collect(Collectors.toList());
        /**
         * 列表
         * 查询参数
         * gender 性别0男性 1女性
         * username
         * phone
         * minAge maxAge出生年份  '1990' '2000'
         */
        QueryWrapper<GwyUserEntity> wrapper = new QueryWrapper<>();
        Object gender = params.get("gender");
        String username = (String) params.get("username");
        String phone = (String) params.get("phone");
        String minAge = (String) params.get("minAge");
        String maxAge = (String) params.get("maxAge");
        wrapper.and(w -> w.in("id", userIds));

        if (gender != null) {
            Integer sex = Integer.valueOf(gender.toString());
            if (sex.equals(1) || sex.equals(0)) {
                wrapper.and(w -> w.eq("gender", gender));
            }
        }

        if (StringUtils.isNotBlank(username)) {
            wrapper.and(w -> w.like("username", username));
        }

        if (StringUtils.isNotBlank(phone)) {
            wrapper.and(w -> w.like("phone", phone));
        }

        if (StringUtils.isNotBlank(minAge)) {
            wrapper.and(w -> w.ge("birth_year", minAge));
        }

        if (StringUtils.isNotBlank(maxAge)) {
            wrapper.and(w -> w.le("birth_year", maxAge));
        }

        IPage<GwyUserEntity> page = this.page(
                new Query<GwyUserEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }


}
