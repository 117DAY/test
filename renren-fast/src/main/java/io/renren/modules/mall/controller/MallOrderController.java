package io.renren.modules.mall.controller;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.mall.entity.MallOrderEntity;
import io.renren.modules.mall.service.MallOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
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
     * name 商品名
     * isPublic 是否公开
     * minCount 最小数量
     * maxCount 最高数量
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

    /**
     * 更新快递单号
     *
     * @param orderId    订单id
     * @param expressNum 表达num
     * @return {@link R}
     */
    @RequestMapping("/express/num")
    public R updateExpressNum(@RequestParam("orderId") String orderId, @RequestParam("expressNum") String expressNum) {
        if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(expressNum)) {
            throw new RuntimeException("订单号或快递单号为空");
        }
        mallOrderService.updateExpressNum(orderId, expressNum);
        return R.ok();
    }

}
