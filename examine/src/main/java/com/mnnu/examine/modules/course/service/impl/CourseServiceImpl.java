package com.mnnu.examine.modules.course.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.dao.CourseDao;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.course.vo.CourseTeacherVO;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {
    @Resource
    CourseOrderService courseOrderService;
    @Resource
    LiveService liveService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        QueryWrapper<CourseEntity> courseEntityQueryWrapper = new QueryWrapper<>();
        courseEntityQueryWrapper.eq("teacher_Id",userId).eq("is_public",1);
        IPage<CourseEntity> page = this.page(
                new Query<CourseEntity>().getPage(params),
                courseEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * ??????????????????
     *
     * @param num ??????????????????
     * @return ??????????????????
     * ---JOJO
     */
    @Override
    public List<CourseEntity> queryRecommendCourseEntity(int num) {
        return getBaseMapper().queryRecommendCourseEntity(num);
    }


    @Override
    public ArrayList<CourseTeacherVO> queryLessonList(Map<String, Object> params, Integer total) {
        //??????????????????

        int page = Integer.parseInt(params.get("page") == null ? "1" : (String) params.get("page"));
        int limit = Integer.parseInt(params.get("limit") == null ? "16" : (String) params.get("limit"));
        if (page <= 0) {
            page = 1;
        }
        int totalPage = 1;
        if (total % limit != 0) {
            totalPage = total / limit + 1;
        } else {
            totalPage = total / limit;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        Page<CourseTeacherVO> p = new Page<>(page, limit);

        ArrayList<CourseTeacherVO> list = null;
        String sort = params.get("sort") == null ? "0" : (String) params.get("sort");
        //???????????????????????????????????????????????????????????????????????????
        if (sort.equals("0")) {
            //?????????????????????
            list = getBaseMapper().selectCourseTeacher(p, 0, (String) params.get("key")).size()==0?new ArrayList<>():getBaseMapper().selectCourseTeacher(p, 0, (String) params.get("key"));
        } else if (sort.equals("1")) {
            //????????????????????????
            list = getBaseMapper().selectCourseTeacher(p, 1, (String) params.get("key")).size()==0?new ArrayList<>():getBaseMapper().selectCourseTeacher(p, 1, (String) params.get("key"));
        } else if (sort.equals("2")) {
            //????????????????????????
            list = getBaseMapper().selectCourseTeacher(p, 2, (String) params.get("key")).size()==0?new ArrayList<>():getBaseMapper().selectCourseTeacher(p, 2, (String) params.get("key"));
        } else {
            //????????????????????????
            list = getBaseMapper().selectCourseTeacher(p, 3, (String) params.get("key")).size()==0?new ArrayList<>():getBaseMapper().selectCourseTeacher(p, 3, (String) params.get("key"));

        }

        if (params.containsKey("page")) {
            params.replace("page", page);
        } else {
            params.put("page", page);
        }


        return list;
    }

    @Override
    public Integer count(String key) {
        Page<CourseTeacherVO> p = new Page<>(1, 999);
        ArrayList list = getBaseMapper().selectCourseTeacher(null, 0,key);

        return list.size();
    }

    @Override
    public Boolean isBuy(Integer id, Integer type) {
        if(id==null||type==null){
            throw new GwyException("???????????????", "???????????????", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());
        }
        if(type==0){//??????????????????
            if (getBaseMapper().selectById(id) == null) {
                throw new GwyException("???????????????", "???????????????", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());
            } else {
                //???????????????id
                Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
                if (userId == null) {
                    throw new GwyException("????????????", "????????????", GwyConstant.BizCode.USER_NOT_LOGIN.getCode());
                } else {
                    if(userId==getBaseMapper().selectById(id).getTeacherId().intValue()){
                        return true;
                    }

                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("user_id", userId);
                    wrapper.eq("course_id", id);
                    wrapper.eq("course_type",type);
                    wrapper.eq("status",1);


                    ArrayList<CourseOrderEntity> list = (ArrayList<CourseOrderEntity>) courseOrderService.list(wrapper);
                    if (list.size() == 0) {
                        throw new GwyException("??????????????????", "??????????????????", GwyConstant.BizCode.LESSON_NOT_BUY.getCode());

                    } else {
                        return true;
                    }
                }
            }
        } else if (type == 1) {
            //??????????????????
            if (liveService.getById(id) == null) {
                throw new GwyException("???????????????", "???????????????", GwyConstant.BizCode.LESSON_NO_EXIST.getCode());
            } else {
                //???????????????id
                Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
                if (userId == null) {
                    throw new GwyException("????????????", "????????????", GwyConstant.BizCode.USER_NOT_LOGIN.getCode());
                } else {
                    if(userId==liveService.getById(id).getTeacherId().intValue()){
                        return true;
                    }
                    QueryWrapper wrapper = new QueryWrapper();
                    wrapper.eq("user_id", userId);
                    wrapper.eq("course_id", id);
                    wrapper.eq("course_type",type);
                    wrapper.eq("status",1);
                    ArrayList<CourseOrderEntity> list = (ArrayList<CourseOrderEntity>) courseOrderService.list(wrapper);
                    if (list.size() == 0) {
                        throw new GwyException("??????????????????", "??????????????????", GwyConstant.BizCode.LESSON_NOT_BUY.getCode());

                    } else {
                        return true;
                    }
                }
            }
        } else {
            throw new GwyException("???????????????????????????", "???????????????????????????", GwyConstant.BizCode.LESSON_KIND_NOT_EXIST.getCode());

        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @param params
     * @param
     * @return
     */
    @Override
    public PageUtils getCoursesByTeacherId(Map<String, Object> params, Long teacherId) {
        QueryWrapper<CourseEntity> wrapper = new QueryWrapper<CourseEntity>().
                eq("teacher_id", teacherId);
        IPage<CourseEntity> page = this.page(
                new Query<CourseEntity>().getPage(params), wrapper);
        PageUtils coursePage = new PageUtils(page);

        return coursePage;
    }

    @Override
    public Map Buy(Integer id, Integer type) {
        Map map=new HashMap();
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("user_id",userId);
        wrapper.eq("course_id",id);
        wrapper.eq("course_type",type);
        wrapper.eq("status",1);
        ArrayList<CourseOrderEntity> list= (ArrayList<CourseOrderEntity>) courseOrderService.list(wrapper);
        if(list.size()!=0){//???????????????
            map.put("result",GwyConstant.OrderStatus.PAID.getCode());
            return map;
        }else {
            QueryWrapper wrapper1=new QueryWrapper();
            wrapper1.eq("user_id",userId);
            wrapper1.eq("course_id",id);
            wrapper1.eq("course_type",type);
            wrapper1.eq("status",2);
            ArrayList<CourseOrderEntity> list1= (ArrayList<CourseOrderEntity>) courseOrderService.list(wrapper1);
            if(list1.size()>0){//???????????????
                map.put("result",GwyConstant.OrderStatus.APPLY_REFUND.getCode());
                return map;

            }else {
                QueryWrapper wrapper2=new QueryWrapper();
                wrapper2.eq("user_id",userId);
                wrapper2.eq("course_id",id);
                wrapper2.eq("course_type",type);
                wrapper2.eq("status",0);
                ArrayList<CourseOrderEntity> list2= (ArrayList<CourseOrderEntity>) courseOrderService.list(wrapper2);

                if(list2.size()>0){//???????????????????????????
                    map.put("result",GwyConstant.OrderStatus.NEW.getCode());
                    map.put("orderId",list2.get(0).getOrderId());

                    return map;

                }else {//??????????????????????????????
                    map.put("result",GwyConstant.OrderStatus.REFUNDED.getCode());
                    return map;

                }
            }
        }

    }




}
