package io.renren.modules.teacher.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.GwyConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.app.entity.UserEntity;
import io.renren.modules.app.service.UserService;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.entity.RoleUserRelationEntity;
import io.renren.modules.sys.service.GwyUserService;
import io.renren.modules.sys.service.RoleUserRelationService;
import io.renren.modules.teacher.dao.TeacherInfoDao;
import io.renren.modules.teacher.entity.TeacherInfoEntity;
import io.renren.modules.teacher.service.TeacherInfoService;
import io.renren.modules.teacher.vo.TeacherTotalInfoVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author qiaoh
 */
@Service("teacherInfoService")
public class TeacherInfoServiceImpl extends ServiceImpl<TeacherInfoDao, TeacherInfoEntity> implements TeacherInfoService {

    @Resource
    RoleUserRelationService roleUserRelationService;

    @Resource
    GwyUserService gwyUserService;


    @Override
    @Transactional(readOnly = true)
    public PageUtils queryPage(Map<String, Object> params) {
        String role = (String) params.get("RoleId");
        Integer roleId = null;
        if (StringUtils.isNotBlank(role)) {
            roleId = Integer.valueOf(role);
        }
        if (roleId == null || roleId.equals(GwyConstant.RoleConstant.TEACHER.getId()) || roleId.equals(GwyConstant.RoleConstant.LIVE.getId())) {
            return getTeacherTotalInfo(params, roleId);
        } else {
//            throw new RuntimeException("??????????????????????????????????????????????????????");
            throw new RuntimeException("??????????????????");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageUtils queryTempTeacherPage(Map<String, Object> params) {
        Integer roleId = Integer.valueOf((String) params.get("RoleId"));
        if (roleId.equals(GwyConstant.RoleConstant.TEMPTEACH.getId())) {
            return getTeacherTotalInfo(params, roleId);
        } else {
//            throw new RuntimeException("??????????????????????????????????????????????????????");
            throw new RuntimeException("????????????????????????");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toFormal(Long[] ids) {
        for (Long id : ids) {
            RoleUserRelationEntity relationEntity = roleUserRelationService.getOne(new QueryWrapper<RoleUserRelationEntity>()
                    .eq("role_id", GwyConstant.RoleConstant.TEMPTEACH.getId())
                    .and(w -> w.eq("user_id", id)));

            if (relationEntity == null) {
                throw new RuntimeException("??????????????????????????????");
            }
            // ???????????????????????????
            roleUserRelationService.remove(new QueryWrapper<RoleUserRelationEntity>().eq("user_id", id));
            // ??????????????????
            relationEntity = new RoleUserRelationEntity();
            relationEntity.setUserId(id);
            relationEntity.setRoleId(GwyConstant.RoleConstant.TEACHER.getId());
            roleUserRelationService.save(relationEntity);
        }


    }

    /**
     * ????????????
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantLive(Long[] ids) {
        for (Long id : ids
        ) {

            RoleUserRelationEntity relationEntity = roleUserRelationService.getOne(new QueryWrapper<RoleUserRelationEntity>()
                    .eq("role_id", GwyConstant.RoleConstant.TEACHER.getId())
                    .and(w -> w.eq("user_id", id)));

            if (relationEntity == null) {
                throw new RuntimeException("??????????????????????????????");
            }
            // ??????????????????
            relationEntity.setRoleId(GwyConstant.RoleConstant.LIVE.getId());
            roleUserRelationService.updateById(relationEntity);
        }
    }

    /**
     * ???????????????????????????
     * @param ids
     * @author JOJO
     */
    @Override
    public void cancelGrantLive(Long[] ids) {
        for (Long id : ids
        ) {
            RoleUserRelationEntity one = roleUserRelationService.getOne(new QueryWrapper<RoleUserRelationEntity>()
                    .eq("role_id", GwyConstant.RoleConstant.LIVE.getId())
                    .and(w -> w.eq("user_id", id)));
            if (one == null) {
                throw new RuntimeException("??????????????????????????????");
            }
            one.setRoleId(GwyConstant.RoleConstant.TEACHER.getId());
            roleUserRelationService.updateById(one);
        }
    }


    /**
     * ??????????????????
     *
     * @param teacherIds ??????id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refuseBecomeTeacher(Long[] teacherIds) {
        RoleUserRelationEntity relationEntity = new RoleUserRelationEntity();
        relationEntity.setRoleId(GwyConstant.RoleConstant.STUDENT.getId());

        for (Long teacherId : teacherIds
        ) {
            roleUserRelationService.update(relationEntity, new QueryWrapper<RoleUserRelationEntity>().eq("user_id", teacherId));
        }
    }

    /**
     * ????????????(??????????????????????????????????????????????????????????????????????????????)
     *
     * @param ids ??????????????????id
     * @return
     * @author JOJO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeacher(Long[] ids) {
        RoleUserRelationEntity roleUserRelationEntity = new RoleUserRelationEntity();
        roleUserRelationEntity.setRoleId(GwyConstant.RoleConstant.STUDENT.getId());
        int flag = roleUserRelationService.getBaseMapper().update(roleUserRelationEntity, new QueryWrapper<RoleUserRelationEntity>().in("user_id", Arrays.asList(ids)));
        return flag > 0;
    }

    private PageUtils getTeacherTotalInfo(Map<String, Object> params, Integer roleId) {
        // ??????????????????????????????id
        List<Long> userIds = getUserIds(params, roleId);
        if (userIds == null || userIds.size() == 0) {
            throw new RuntimeException("???????????????????????????");
        }

        // ????????????????????????????????????id
        Map<String, Object> teacherInfoMap = getTeacherInfoEntity(params, userIds);

        List<Long> userIds1 = (List<Long>) teacherInfoMap.get("userIds");

        // ????????????????????????
        IPage<GwyUserEntity> userInfoPage = getUserInfo(params, userIds1);
        List<TeacherInfoEntity> teacherInfoEntities = (List<TeacherInfoEntity>) teacherInfoMap.get("teacher");

        // ????????????
        return packageInfo(teacherInfoEntities, userInfoPage);


    }


    /**
     * ????????????id???????????????userID
     *
     * @param params
     * @param roleId
     * @return
     */
    private List<Long> getUserIds(Map<String, Object> params, Integer roleId) {
        HashMap<String, Object> map = new HashMap<>(10);
        map.putAll(params);
        QueryWrapper<RoleUserRelationEntity> wrapper = new QueryWrapper<RoleUserRelationEntity>();

        if (roleId != null) {
            wrapper.eq("role_id", roleId);
        } else {
            wrapper.eq("role_id", GwyConstant.RoleConstant.TEACHER.getId()).or().eq("role_id", GwyConstant.RoleConstant.LIVE.getId());
        }


        // ??????????????????????????????id
        IPage<RoleUserRelationEntity> page = roleUserRelationService.page(new Query<RoleUserRelationEntity>().getPage(map), wrapper
        );
        List<RoleUserRelationEntity> records = page.getRecords();
        return records.stream().map(RoleUserRelationEntity::getUserId).collect(Collectors.toList());
    }


    /**
     * ????????????id?????????????????????
     *
     * @param params
     * @param ids
     * @return
     */
    private IPage<GwyUserEntity> getUserInfo(Map<String, Object> params, List<Long> ids) {
        HashMap<String, Object> map = new HashMap<>(10);
        map.putAll(params);
        //???map????????????????????????????????? ---JOJO
        String username = (String) params.get("username");
        String phone = (String) params.get("phone");
        String gender = (String) params.get("gender");
        QueryWrapper<GwyUserEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);

        if (StringUtils.isNotBlank(username)) {
            wrapper.and(w -> {
                w.like("username", username);
            });
        }
        if (StringUtils.isNotBlank(gender)) {
            wrapper.and(w -> {
                w.eq("gender", gender);
            });
        }
        if (StringUtils.isNotBlank(phone)) {
            wrapper.and(w -> {
                w.like("phone", phone);
            });
        }

        return gwyUserService.page(new Query<GwyUserEntity>().getPage(map), wrapper);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param params
     * @param ids
     * @return
     */
    private Map<String, Object> getTeacherInfoEntity(Map<String, Object> params, List<Long> ids) {
        HashMap<String, Object> map = new HashMap<>(10);
        map.putAll(params);
        QueryWrapper<TeacherInfoEntity> wrapper = new QueryWrapper<>();
        String realName = (String) params.get("realName");
        //  ???????????????????????????
        if (StringUtils.isNotBlank(realName)) {
            wrapper.and(w -> {
                w.like("real_name", realName);
            });
        }

        if (ids != null && ids.size() != 0) {
            wrapper.and(w -> w.in("teacher_id", ids));
            // ??????????????????????????????????????????  ---JOJO
            String eduBg = (String) params.get("eduBg");
            if (StringUtils.isNotBlank(eduBg)) {
                wrapper.and(w -> w.eq("edu_bg", eduBg));
            }
        }

        List<TeacherInfoEntity> teacherInfoEntities = getBaseMapper().selectList(wrapper);
        List<Long> userIds = teacherInfoEntities.stream().map(TeacherInfoEntity::getTeacherId).collect(Collectors.toList());

        HashMap<String, Object> listListHashMap = new HashMap<>();
        listListHashMap.put("teacher", teacherInfoEntities);
        listListHashMap.put("userIds", userIds);
//        IPage<TeacherInfoEntity> page = this.page(
//                new Query<TeacherInfoEntity>().getPage(map), wrapper
//        );
        return listListHashMap;
    }

    /**
     * ??????????????????
     *
     * @param teacherInfoEntities ??????info???List
     * @param userInfoIPage       ??????????????????
     * @return
     */
    private PageUtils packageInfo(List<TeacherInfoEntity> teacherInfoEntities, IPage<GwyUserEntity> userInfoIPage) {
//        List<TeacherInfoEntity> records = teacherInfoEntityIPage.getRecords();
        HashMap<Long, TeacherInfoEntity> map = new HashMap<>(100);
        teacherInfoEntities.forEach(r -> {
            map.put(r.getTeacherId(), r);
        });
        ArrayList<TeacherTotalInfoVO> list = new ArrayList<>();
        System.out.println("hello world");
        List<GwyUserEntity> userRecords = userInfoIPage.getRecords();
        userRecords.forEach(u -> {
            TeacherTotalInfoVO vo = new TeacherTotalInfoVO();
            BeanUtils.copyProperties(u, vo);
            TeacherInfoEntity infoEntity = map.get(vo.getId());
            BeanUtils.copyProperties(infoEntity, vo, "id");
            RoleUserRelationEntity roleUserRelationEntity = roleUserRelationService.getBaseMapper().selectOne(new QueryWrapper<RoleUserRelationEntity>().eq("user_id", vo.getId()));
            vo.setRoleId(roleUserRelationEntity.getRoleId());
            list.add(vo);
        });

        return new PageUtils(list, (int) userInfoIPage.getTotal(), (int) userInfoIPage.getSize(), (int) userInfoIPage.getCurrent());
    }
}
