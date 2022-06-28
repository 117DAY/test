package com.mnnu.examine.modules.mall.controller;

import com.mnnu.examine.common.utils.GwyUtils;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.mall.entity.MallOrderEntity;
import com.mnnu.examine.modules.mall.service.MallOrderService;
import com.mnnu.examine.modules.mall.service.MallProductService;
import com.mnnu.examine.modules.mall.vo.MallUserVO;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * 兑换商品的订单的生成以及查询更新
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 22:38:40
 */
@RestController
@RequestMapping("mall/mallorder")
public class MallOrderController {
    @Autowired
    private MallOrderService mallOrderService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = mallOrderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MallOrderEntity mallOrder = mallOrderService.getById(id);

        return R.ok().put("mallOrder", mallOrder);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MallOrderEntity mallOrder) {
        mallOrderService.save(mallOrder);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MallOrderEntity mallOrder) {
        mallOrderService.updateById(mallOrder);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        mallOrderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @Resource
    MallProductService mallProductService;

    /**
     * 交换产品
     *
     * @param productId 产品id
     * @return {@link R}
     */
    @RequestMapping("/exchange/{productId}")
    @PreAuthorize("hasRole('student')")
    public R exchangeProduct(@PathVariable Long productId, @Validated @RequestBody MallUserVO mallUserVO) {
        Long userId = GwyUtils.getUser().getId();
        mallProductService.exchangeProduct(userId, productId, mallUserVO);
        return R.ok();
    }

    @RequestMapping("/userMallOrder")
    public R getUserMallOrderById(@RequestParam Map<String, Object> params) {
        Long id = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        PageUtils page = mallOrderService.getCourseListByUserId(params, id);
        return R.ok().put("page", page);
    }

}
