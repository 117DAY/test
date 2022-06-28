package com.mnnu.examine.modules.mall.controller;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.mall.entity.MallProductEntity;
import com.mnnu.examine.modules.mall.service.MallProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 上架，调整兑换商城的商品
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
        // 公开且个数大于0才显示
        List<?> collect = page.getList().stream().filter(e -> {
            MallProductEntity entity = (MallProductEntity) e;
            return entity.getIsPublic() == 1 && entity.getCount() > 0;
        }).collect(Collectors.toList());
        page.setList(collect);
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
