package io.renren.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.sys.entity.CatchphraseAudioEntity;
import io.renren.modules.sys.service.CatchphraseAudioService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2022-02-23 21:34:08
 */
@RestController
@RequestMapping("sys/catchphraseaudio")
public class CatchphraseAudioController {
    @Autowired
    private CatchphraseAudioService catchphraseAudioService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = catchphraseAudioService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Integer catId){
		CatchphraseAudioEntity catchphraseAudio = catchphraseAudioService.getById(catId);

        return R.ok().put("catchphraseAudio", catchphraseAudio);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CatchphraseAudioEntity catchphraseAudio){
		catchphraseAudioService.save(catchphraseAudio);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CatchphraseAudioEntity catchphraseAudio){
		catchphraseAudioService.updateById(catchphraseAudio);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] catIds){
		catchphraseAudioService.removeByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
