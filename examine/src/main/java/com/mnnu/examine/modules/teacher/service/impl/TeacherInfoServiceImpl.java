package com.mnnu.examine.modules.teacher.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.entity.RoleUserRelationEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.RoleUserRelationService;
import com.mnnu.examine.modules.teacher.dao.TeacherInfoDao;
import com.mnnu.examine.modules.teacher.entity.TeacherInfoEntity;
import com.mnnu.examine.modules.teacher.service.TeacherInfoService;
import com.mnnu.examine.modules.teacher.vo.TeacherInfoRegisterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("teacherInfoService")
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfoEntity> implements TeacherInfoService {
    @Resource
    RoleUserRelationService roleUserRelationService;

    @Resource
    TransactionTemplate transactionTemplate;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    Gson gson;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TeacherInfoEntity> page = this.page(
                new Query<TeacherInfoEntity>().getPage(params),
                new QueryWrapper<TeacherInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRegisterInfo(TeacherInfoRegisterVO teacherInfo) {


        // ?????????????????????
        TeacherInfoEntity entity = new TeacherInfoEntity();
        BeanUtils.copyProperties(teacherInfo, entity);
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        entity.setTeacherId(id);
        TeacherInfoEntity teacher_id = this.getBaseMapper().selectOne(new QueryWrapper<TeacherInfoEntity>().eq("teacher_id", id));
        if (teacher_id == null) {
            this.save(entity);
        } else {
            entity.setId(teacher_id.getId());
            this.updateById(entity);
        }

        // ?????????????????????
        roleUserRelationService.remove(new QueryWrapper<RoleUserRelationEntity>().eq("user_id", id));
        // ???????????????????????????
        RoleUserRelationEntity relationEntity = new RoleUserRelationEntity();
        relationEntity.setUserId(id);
        relationEntity.setRoleId(GwyConstant.RoleConstant.TEMPTEACH.getId());
        roleUserRelationService.save(relationEntity);
        // ??????redis??????????????????
        String rolePrefix = "USER_ROLE_";
        List<RoleUserRelationEntity> relationEntityList = roleUserRelationService.list(new QueryWrapper<RoleUserRelationEntity>().eq("user_id", id));
        ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
        relationEntityList.forEach(r -> {
            list.add(new SimpleGrantedAuthority("ROLE_" + GwyConstant.RoleConstant.TEMPTEACH.getRoleName()));
        });

        redisTemplate.opsForValue().setIfPresent(rolePrefix + id, gson.toJson(list));
    }

    @Override
    public void updateCommistion(Integer teacherId, BigDecimal addCommistion) {
        getBaseMapper().updateCommistion(teacherId, addCommistion);
    }

    @Override
    public TeacherInfoEntity getTemTeacher() {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return getBaseMapper().selectOne(new QueryWrapper<TeacherInfoEntity>().eq("teacher_id", id));
    }

}
