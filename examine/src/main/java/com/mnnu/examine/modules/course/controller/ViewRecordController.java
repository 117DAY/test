package com.mnnu.examine.modules.course.controller;


import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.course.entity.ViewRecordEntity;
import com.mnnu.examine.modules.course.service.ViewRecordService;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@RestController
@RequestMapping("course/viewrecord")
public class ViewRecordController {
    @Autowired
    private ViewRecordService viewRecordService;

    /**
     *
     *
     * @description: 获取用户观看历史记录
     * @param page
     * @param size
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/26 17:47
     */
    @RequestMapping("/userRecordList")
    public R getViewRecordListByUserId(@RequestParam Map<String, Object> params){
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        PageUtils page = viewRecordService.getViewRecordListByUserId(params,id);

        return R.ok().put("page", page);

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = viewRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ViewRecordEntity viewRecord = viewRecordService.getById(id);

        return R.ok().put("viewRecord", viewRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ViewRecordEntity viewRecord){
		viewRecordService.save(viewRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ViewRecordEntity viewRecord){
		viewRecordService.updateById(viewRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		viewRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/insertViewRecord")
    public R insertViewRecord(@RequestParam Map<String,Object> params ){
        int result=viewRecordService.insertViewRecord(params);
        if(result>0){
            return R.ok();
        }
        return R.error();
    }
    @RequestMapping("/getTime")
    public R getTime(@RequestParam Map<String,Object> params ){
        String time=viewRecordService.getTime(params);

        return R.ok().put("data",time);
    }


    @RequestMapping("/deleteViewRecordById")
    public R deleteViewRecordById(@RequestParam("id") Integer id ){

        if(viewRecordService.removeById(id)){
            return R.ok();
        }

        return R.error(GwyConstant.BizCode.VIEW_DELETE_FAILED.getCode(),GwyConstant.BizCode.VIEW_DELETE_FAILED.getMessage()) ;
    }

}
