package io.renren.modules.course.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.course.dao.CommentsDao;
import io.renren.modules.course.entity.CommentsEntity;
import io.renren.modules.course.service.CommentsService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author qiaoh
 */
@Service("commentsService")
public class CommentsServiceImpl extends ServiceImpl<CommentsDao, CommentsEntity> implements CommentsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentsEntity> page;
        if(Integer.parseInt((String) params.get("sort"))==0){
             page = this.page(
                    new Query<CommentsEntity>().getPage(params),
                    new QueryWrapper<CommentsEntity>().orderByDesc("time").like("content",params.get("key"))
            );
        }else if(Integer.parseInt((String) params.get("sort"))==1){
            page = this.page(
                    new Query<CommentsEntity>().getPage(params),
                    new QueryWrapper<CommentsEntity>().eq("is_public",1).orderByDesc("time").like("content",params.get("key"))
            );
        }else {
            page = this.page(
                    new Query<CommentsEntity>().getPage(params),
                    new QueryWrapper<CommentsEntity>().eq("is_public",0).orderByDesc("time").like("content",params.get("key"))
            );
        }


        return new PageUtils(page);
    }

}
