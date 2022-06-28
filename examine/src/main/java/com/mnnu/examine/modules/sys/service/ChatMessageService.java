package com.mnnu.examine.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.sys.entity.ChatMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author 自动生成
 * @email generat
 * @date 2021-12-09 16:28:24
 */
public interface ChatMessageService extends IService<ChatMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

