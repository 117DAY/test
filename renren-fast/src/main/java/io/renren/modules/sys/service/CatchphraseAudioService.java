package io.renren.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.sys.entity.CatchphraseAudioEntity;

import java.util.Map;

/**
 *
 *
 * @author 自动生成
 * @email generat
 * @date 2022-02-23 21:34:08
 */
public interface CatchphraseAudioService extends IService<CatchphraseAudioEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

