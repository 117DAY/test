package com.mnnu.examine.modules.sys.controller;

import com.google.gson.Gson;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.entity.ParamsEntity;
import com.mnnu.examine.modules.sys.service.ParamsService;
import com.mnnu.examine.modules.sys.vo.ParamsVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@RestController
@RequestMapping("sys/params")
public class ParamsController {
    @Autowired
    private ParamsService paramsService;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = paramsService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ParamsEntity params = paramsService.getById(id);

        return R.ok().put("params", params);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ParamsEntity params) {
        paramsService.save(params);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ParamsEntity params) {
        paramsService.updateById(params);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        paramsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    @RequestMapping("/listentry")
    @Cacheable(cacheNames = "pageFoot", key = "'params'")
    public R listEntry() {
        String params = paramsService.getBaseMapper().selectById(1L).getParams();
        Gson gson = new Gson();
        ParamsVO paramsVO = gson.fromJson(params, ParamsVO.class);
        return R.ok().put("params", paramsVO);
    }

    /**
     * 获取样式，如果拿不到则按原本的样式显示
     *
     * @return {@link R}
     */
    @RequestMapping("/get/style")
    public R getStyle() {
        String key = "style_change";
        String style = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(style)) {
            return R.ok().put("style", "");
        } else {
            return R.ok().put("style", style);
        }
    }

}
