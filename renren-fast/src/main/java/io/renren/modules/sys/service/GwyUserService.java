package io.renren.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.GwyUserEntity;


import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-19 14:21:23
 */
public interface GwyUserService extends IService<GwyUserEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

