package io.renren.modules.sys.controller;


import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.CatchphraseEntity;
import io.renren.modules.sys.entity.GwyCatchphraseEntity;
import io.renren.modules.sys.service.GwyCatchphraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import io.renren.common.utils.R;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * 标语 心灵鸡汤
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
@RestController
@RequestMapping("sys/catchphrase")
@CacheEvict(cacheNames = "catchphrase" ,allEntries = true)

public class CatchphraseController {
    @Autowired
    private GwyCatchphraseService catchphraseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = catchphraseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) throws InterruptedException {
        GwyCatchphraseEntity catchphrase = catchphraseService.getById(id);

        return R.ok().put("catchphrase", catchphrase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GwyCatchphraseEntity catchphrase) {
        catchphraseService.save(catchphrase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")

    public R update(@RequestBody GwyCatchphraseEntity catchphrase) {
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
    @RequestMapping("/updateVideo")
    public R updateVideo(@RequestBody Map<String,Object> params) {
//        for(int i=0;i<params.videoUrl.length;i++){
//            System.out.println(params.videoUrl[i].url);
//        }
        List l= (List) params.get("videoUrl");

//        System.out.println(l.get(0).);
        GwyCatchphraseEntity courseEntity=new GwyCatchphraseEntity();
        for (Object o:l
             ) {
            LinkedHashMap<String,String> map= (LinkedHashMap<String, String>) o;
            courseEntity.setWords(map.get("url"));
            catchphraseService.save(courseEntity);
        }



//        GwyCatchphraseEntity.setWords();
        return R.ok();
    }
}
