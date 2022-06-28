package io.renren.modules.mall.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.mall.entity.DeliveryAddressEntity;
import io.renren.modules.mall.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:48
 */
@RestController
@RequestMapping("mall/deliveryaddress")
public class DeliveryAddressController {
    @Autowired
    private DeliveryAddressService deliveryAddressService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = deliveryAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        DeliveryAddressEntity deliveryAddress = deliveryAddressService.getById(id);

        return R.ok().put("deliveryAddress", deliveryAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DeliveryAddressEntity deliveryAddress) {
        deliveryAddressService.save(deliveryAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody DeliveryAddressEntity deliveryAddress) {
        deliveryAddressService.updateById(deliveryAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        deliveryAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


}
