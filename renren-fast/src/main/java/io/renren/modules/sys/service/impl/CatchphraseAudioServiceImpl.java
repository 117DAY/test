package io.renren.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.sys.dao.CatchphraseAudioDao;
import io.renren.modules.sys.entity.CatchphraseAudioEntity;
import io.renren.modules.sys.service.CatchphraseAudioService;


@Service("catchphraseAudioService")
public class CatchphraseAudioServiceImpl extends ServiceImpl<CatchphraseAudioDao, CatchphraseAudioEntity> implements CatchphraseAudioService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CatchphraseAudioEntity> page = this.page(
                new Query<CatchphraseAudioEntity>().getPage(params),
                new QueryWrapper<CatchphraseAudioEntity>()
        );

        return new PageUtils(page);
    }

}
