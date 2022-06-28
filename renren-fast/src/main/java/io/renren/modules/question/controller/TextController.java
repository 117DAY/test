package io.renren.modules.question.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.question.entity.TextEntity;
import io.renren.modules.question.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * (申论)试题材料表
 *
 * @author 自动生成
 * @email generate
 * @date 2021-12-01 22:30:37
 */
@RestController
@RequestMapping("modules/text")
public class TextController {
    @Autowired
    private TextService textService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = textService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		TextEntity text = textService.getById(id);

        return R.ok().put("text", text);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TextEntity text){
		textService.save(text);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody TextEntity text){
		textService.updateById(text);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		textService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
