package com.mnnu.examine.modules.sys.service.impl;

import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.sys.dao.ChatMessageDao;
import com.mnnu.examine.modules.sys.entity.ChatMessageEntity;
import com.mnnu.examine.modules.sys.service.ChatMessageService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * 聊天消息服务impl
 *
 * @author qiaoh
 * @date 2021/12/09
 */
@Service("chatMessageService")
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageDao, ChatMessageEntity> implements ChatMessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ChatMessageEntity> page = this.page(
                new Query<ChatMessageEntity>().getPage(params),
                new QueryWrapper<ChatMessageEntity>()
        );

        return new PageUtils(page);
    }

}