package com.mnnu.examine.modules.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.mall.entity.DeliveryAddressEntity;
import com.mnnu.examine.modules.mall.service.DeliveryAddressService;
import com.mnnu.examine.modules.mall.vo.DeliveryAddressVO;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 用户地址的相关操作，包括增删改查以及改变默认地址
 *
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
    public R list() {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<DeliveryAddressEntity> list = deliveryAddressService.list(
                new QueryWrapper<DeliveryAddressEntity>().eq("user_id", userId));
        List<DeliveryAddressEntity> collect = list.stream().sorted(
                Comparator.comparingInt(DeliveryAddressEntity::getIsDefault).reversed()).collect(Collectors.toList());
        return R.ok().put("addressList", collect);
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
    public R save(@RequestBody @Valid DeliveryAddressVO addressVO) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<DeliveryAddressEntity> list = deliveryAddressService.list(
                new QueryWrapper<DeliveryAddressEntity>().eq("user_id", userId));
        if (list.size() >= 10) {
            return R.error("地址数量不能超过10!");
        }
        DeliveryAddressEntity address = new DeliveryAddressEntity();
        BeanUtils.copyProperties(addressVO, address);
        address.setUserId(userId);
        address.setIsDefault(addressVO.getIsDefault() ? 1 : 0);
        if (address.getIsDefault() == 1) {
            DeliveryAddressEntity entity = new DeliveryAddressEntity();
            entity.setIsDefault(0);
            deliveryAddressService.update(
                    entity, new QueryWrapper<DeliveryAddressEntity>().eq("is_default", 1));
        }
        deliveryAddressService.save(address);
        return R.ok("添加成功!");
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody @Valid DeliveryAddressVO addressVO) {
        DeliveryAddressEntity addressEntity = new DeliveryAddressEntity();
        BeanUtils.copyProperties(addressVO, addressEntity);
        deliveryAddressService.updateById(addressEntity);
        return R.ok("更新成功!");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        deliveryAddressService.removeByIds(Arrays.asList(ids));

        return R.ok("删除成功!");
    }

    /**
     * 改变默认的地址
     *
     * @return {@link R}
     */
    @RequestMapping("/default/{addressId}")
    public R changeDefault(@PathVariable Long addressId) {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        deliveryAddressService.changeDefault(userId, addressId);
        return R.ok("默认地址设置成功!");
    }
}
