package io.renren.modules.mall.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.mall.entity.MallProductEntity;
import io.renren.modules.mall.service.MallProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 后台对积分商城的管理
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@RestController
@RequestMapping("mall/mallproduct")
public class MallProductController {
    @Autowired
    private MallProductService mallProductService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = mallProductService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MallProductEntity mallProduct = mallProductService.getById(id);

        return R.ok().put("mallProduct", mallProduct);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MallProductEntity mallProduct) {
        mallProductService.save(mallProduct);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MallProductEntity mallProduct) {
        mallProductService.updateById(mallProduct);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        mallProductService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
