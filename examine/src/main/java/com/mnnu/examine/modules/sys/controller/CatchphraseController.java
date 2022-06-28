package com.mnnu.examine.modules.sys.controller;

import cn.hutool.core.io.FileUtil;
import com.aliyuncs.exceptions.ClientException;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.oss.utils.OSSUtils;
import com.mnnu.examine.modules.sys.entity.CatchphraseEntity;
import com.mnnu.examine.modules.sys.service.CatchphraseService;
import com.mnnu.examine.modules.sys.vo.CatchphraseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/catchphrase")
@CacheConfig(cacheNames = {"catchphrase"})
public class CatchphraseController {
    @Autowired
    private CatchphraseService catchphraseService;

    @Autowired
    private OSSUtils ossUtils;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = catchphraseService.queryPage(params);
        return R.ok().put("page", page);
    }

    @RequestMapping("/all")
    public R getAllCatchphrase() {
        List<CatchphraseEntity> list = catchphraseService.list();
        List<CatchphraseVO> vos = new ArrayList<>();
        for (CatchphraseEntity catchphraseEntity : list) {
            vos.add(new CatchphraseVO()
                    .setId(catchphraseEntity.getId())
                    .setUrl(catchphraseEntity.getWords()));
        }
        return R.ok().put("all", vos);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) throws InterruptedException {
        CatchphraseEntity catchphrase = catchphraseService.getById(id);

        return R.ok().put("catchphrase", catchphrase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CatchphraseEntity catchphrase) {
        catchphraseService.save(catchphrase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CatchphraseEntity catchphrase) {
        catchphraseService.updateById(catchphrase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        catchphraseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
