package io.renren.modules.exam.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.exam.entity.ExamRecordEntity;
import io.renren.modules.exam.service.ExamRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@RestController
@RequestMapping("exam/examrecord")
public class ExamRecordController {
    @Autowired
    private ExamRecordService examRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = examRecordService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		ExamRecordEntity examRecord = examRecordService.getById(id);

        return R.ok().put("examRecord", examRecord);
    }



    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamRecordEntity examRecord){
		examRecordService.save(examRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamRecordEntity examRecord){
		examRecordService.updateById(examRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		examRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
