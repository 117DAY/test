package io.renren.modules.sys.controller;


import com.google.gson.Gson;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.ParamsEntity;
import io.renren.modules.sys.service.ParamsService;
import io.renren.modules.sys.vo.ParamsVO;
import io.renren.modules.sys.vo.StyleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 页脚的参数
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@RestController
@RequestMapping("sys/params")
@CacheEvict(cacheNames = "pageFoot", allEntries = true)

public class ParamsController {
    ArrayList<String> keyList = new ArrayList<String>() {{
        add("公司");
        add("地区");
        add("电话");
        add("备案号");

    }};
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

    @RequestMapping("/addentry")
    public R addEntry(@RequestBody Map<String, String> form) {
        String key = form.get("key");
        String value = form.get("value");
        ParamsEntity paramsEntity = paramsService.getBaseMapper().selectById(1L);
        String params = paramsEntity.getParams();
        Gson gson = new Gson();
        ParamsVO paramsVO = gson.fromJson(params, ParamsVO.class);
        if (paramsVO.getParams() == null) paramsVO.setParams(new HashMap<String, String>());
        paramsVO.getParams().put(key, value);
        paramsEntity.setParams(gson.toJson(paramsVO, ParamsVO.class));
        paramsService.getBaseMapper().updateById(paramsEntity);
        return R.ok();
    }

    @RequestMapping("/listentry")
    public R listEntry() {
        String params = paramsService.getBaseMapper().selectById(1L).getParams();
        Gson gson = new Gson();
        if(params.equals("")){
            params="{\"params\":{}}";
        }
        ParamsVO paramsVO = gson.fromJson(params, ParamsVO.class);
        if (paramsVO.getParams() == null||paramsVO.getParams().size()==0) {
            HashMap<String, String> hashMap = new HashMap<>();
            keyList.forEach(k -> {
                hashMap.put(k, "");
            });
            paramsVO.setParams(hashMap);
        }
        return R.ok().put("params", paramsVO);
    }


    @RequestMapping("/delentry")
    public R delEntry(@RequestBody Map<String, String> form) {
        String key = form.get("key");
        ParamsEntity paramsEntity = paramsService.getBaseMapper().selectById(1L);
        String params = paramsEntity.getParams();
        Gson gson = new Gson();
        ParamsVO paramsVO = gson.fromJson(params, ParamsVO.class);
        paramsVO.getParams().remove(key);
        paramsEntity.setParams(gson.toJson(paramsVO, ParamsVO.class));
        paramsService.getBaseMapper().updateById(paramsEntity);
        return R.ok();
    }

    @RequestMapping("/upentry")
    public R upEntry(@RequestBody Map<String, String> form) {
        String key = form.get("key");
        String value = form.get("value");

        ParamsEntity paramsEntity = paramsService.getBaseMapper().selectById(1L);
        String params = paramsEntity.getParams();
        if(params.equals("")){
            params="{\"params\":{}}";
        }
        Gson gson = new Gson();
        ParamsVO paramsVO = gson.fromJson(params, ParamsVO.class);
        if (paramsVO.getParams() == null||paramsVO.getParams().size()==0) {
            HashMap<String, String> hashMap = new HashMap<>();
            keyList.forEach(k -> {
                hashMap.put(k, "");
            });
            paramsVO.setParams(hashMap);
        }
        if (paramsVO.getParams().containsKey(key)) {
            paramsVO.getParams().replace(key, value);
        }
        paramsEntity.setParams(gson.toJson(paramsVO, ParamsVO.class));
        paramsService.getBaseMapper().updateById(paramsEntity);
        return R.ok();
    }


    /**
     * 改变风格
     * 传入需要替换的时间和样式
     *
     * @param styleVO 风格签证官
     * @return {@link R}
     */
    @RequestMapping("/change/style")
    public R changeStyle(@Validated @RequestBody StyleVO styleVO) {
        String key = "style_change";

        redisTemplate.opsForValue().set(key, styleVO.getStyle(), styleVO.getExpireTime(), TimeUnit.SECONDS);
        return R.ok();
    }
}
