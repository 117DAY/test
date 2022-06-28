package io.renren.modules.sys.service;



import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.GwyCatchphraseEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-11-10 16:49:05
 */
public interface GwyCatchphraseService extends IService<GwyCatchphraseEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

