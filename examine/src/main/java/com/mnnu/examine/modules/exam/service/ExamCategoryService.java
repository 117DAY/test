package com.mnnu.examine.modules.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.modules.exam.entity.ExamCategoryEntity;
import com.mnnu.examine.modules.exam.vo.ExamCategoryVO;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;

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
     *
     * @param list
     * @return
     */
    List<ExamCategoryVO> withTree(List<ExamCategoryEntity> list);

    /**
     * 生成带uuid的树
     *
     * @param withUUID
     * @return
     */
    List<ExamCategoryVO> withTree(boolean withUUID);

    /**
     * 获取所有路径
     *
     * @param node
     * @param path
     * @param allPah
     */
    void getAllPath(ExamCategoryVO node, List<String> path, List<List<String>> allPah);

    /**
     * 获取根分类
     *
     * @param list
     * @param categoryEntity
     * @return
     */
    ExamCategoryEntity getRoot(List<ExamCategoryEntity> list, ExamCategoryEntity categoryEntity);
}

