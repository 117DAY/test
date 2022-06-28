package io.renren.modules.course.controller;



import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.course.entity.CourseTypeEntity;
import io.renren.modules.course.service.CourseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-26 10:24:40
 */
@RestController
@RequestMapping("course/coursetype")
public class CourseTypeController {
    @Autowired
    private CourseTypeService courseTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseTypeService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CourseTypeEntity courseType = courseTypeService.getById(id);

        return R.ok().put("courseType", courseType);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CourseTypeEntity courseType){
		courseTypeService.save(courseType);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CourseTypeEntity courseType){
		courseTypeService.updateById(courseType);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		courseTypeService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
