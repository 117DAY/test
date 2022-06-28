package com.mnnu.examine.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.RefundEntity;
import com.mnnu.examine.modules.sys.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@RestController
@RequestMapping("sys/refund")
public class RefundController {
    @Autowired
    private RefundService refundService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = refundService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		RefundEntity refund = refundService.getById(id);

        return R.ok().put("refund", refund);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RefundEntity refund){
		refundService.save(refund);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RefundEntity refund){
		refundService.updateById(refund);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		refundService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
