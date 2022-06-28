package io.renren.modules.sys.controller;


import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.GwyUserEntity;
import io.renren.modules.sys.service.GwyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
@RestController
@RequestMapping("/gwy/user")
public class GwyUserController   {
    @Autowired
    private GwyUserService userService;

    /**
     * 列表 这里只查角色是学生的user
     * 查询参数
     * gender 性别0男性 1女性
     * username
     * phone
     * minAge maxAge出生年份  '1990' '2000'
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {

        GwyUserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GwyUserEntity user) {
        userService.save(user);

        return R.ok();
    }

    /**
     * 更新用户信息
     *
     * @param userEntity 用户信息
     * @param id         从security中取出Id
     * @return 返回更新好的用户信息
     * @author JOJO
     */
    @RequestMapping("/update")
    public R update(@RequestBody GwyUserEntity userEntity) {
        userService.updateById(userEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 注销当前账户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete/{id}")
    public R deleteAccount(@PathVariable("id") Long id) {

        userService.removeById(id);
        return R.ok();
    }


}
