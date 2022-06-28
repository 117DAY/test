package io.renren.modules.course.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.course.entity.ViewRecordEntity;
import io.renren.modules.course.service.ViewRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
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
     * @param page
     * @param size
     * @description: 获取用户观看历史记录
     * @return: 分页对象
     * @author: Geralt
     * @time: 2021/11/26 17:47
     */
    @RequestMapping("/userRecordList/{id}")
    public R getViewRecordListByUserId(@RequestParam Map<String, Object> params, @PathVariable("id") Long id) {
        PageUtils page = viewRecordService.getViewRecordListByUserId(params, id);

        return R.ok().put("page", page);

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = viewRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ViewRecordEntity viewRecord = viewRecordService.getById(id);

        return R.ok().put("viewRecord", viewRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ViewRecordEntity viewRecord) {
        viewRecordService.save(viewRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ViewRecordEntity viewRecord) {
        viewRecordService.updateById(viewRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        viewRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
