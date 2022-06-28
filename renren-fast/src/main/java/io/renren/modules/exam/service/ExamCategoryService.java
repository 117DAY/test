package io.renren.modules.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.vo.ExamCategoryVO;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generat
 * @date 2021-11-14 19:34:57
 */
public interface ExamCategoryService extends IService<ExamCategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 将分类拼装成树形结构
     *
     * @return
     */
    List<ExamCategoryVO> withTree();

    /**
     * 重载withTree方法,方便使用
     * @param list
     * @return
     */
    List<ExamCategoryVO> withTree(List<ExamCategoryEntity> list);

    /**
     * 根据子分类获取根到子分类的路径(id列表)
     *
     * @return
     */
    List<Long> getPathById(List<Long> idList, List<ExamCategoryEntity> list, ExamCategoryEntity examCategoryEntity);
}

