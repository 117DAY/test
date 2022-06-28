package io.renren.modules.sys.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.GwyPropagandaEntity;
import io.renren.modules.sys.service.GwyPropagandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 轮播图
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/propaganda")
@CacheEvict(cacheNames = "propaganda",allEntries = true,beforeInvocation = true)

public class PropagandaController {
    @Autowired
    private GwyPropagandaService propagandaService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @Deprecated
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = propagandaService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有要展示在前台的轮播图
     *
     * @return
     */
    @RequestMapping("/all")
    public R allPropaganda() {
        List<GwyPropagandaEntity> list = propagandaService.list();

        List<GwyPropagandaEntity> collect = list.stream().filter(p -> {
            return p.getIsShow() == 1;
        }).sorted(Comparator.comparingInt(GwyPropagandaEntity::getSort)).collect(Collectors.toList());
        return R.ok().put("allPropaganda", collect);
    }

    /**
     * 更改图片显示
     *
     * @param ids    id
     * @param isShow 是显示
     * @return {@link R}
     */
    @RequestMapping("/change/show/{isShow}")
    public R changePicShow(@RequestBody List<Integer> ids, @PathVariable Integer isShow) {
        List<GwyPropagandaEntity> list = ids.stream().map(id -> {
            GwyPropagandaEntity entity = new GwyPropagandaEntity();
            entity.setId(id);
            entity.setIsShow(isShow);
            return entity;
        }).collect(Collectors.toList());
        propagandaService.updateBatchById(list);
        return R.ok();
    }

    /**
     * 改变排序
     * 传入 id 和排序字段即可
     *
     * @param list 列表
     * @return {@link R}
     */
    @RequestMapping("/change/sort")
    public R changeSort(@RequestBody List<GwyPropagandaEntity> list) {
        propagandaService.updateBatchById(list);
        return R.ok();
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GwyPropagandaEntity propaganda) {
        propagandaService.save(propaganda);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody GwyPropagandaEntity propaganda) {
        propagandaService.updateById(propaganda);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")

    public R delete(@RequestBody Integer[] ids) {
        propagandaService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        GwyPropagandaEntity propaganda = propagandaService.getById(id);

        return R.ok().put("propaganda", propaganda);
    }
}
