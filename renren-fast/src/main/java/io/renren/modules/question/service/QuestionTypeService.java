package io.renren.modules.question.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.exam.vo.ExamCategoryVO;
import io.renren.modules.question.entity.QuestionEntity;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
public interface QuestionTypeService extends IService<QuestionTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 将试卷类型组装为树形结构
     *
     * @param id
     * @return
     */
    List<QuestionTypeEntity> getQuestionTypeAsTree(Integer id);


    /**
     * 重载方法,减少数据库查询次数
     *
     * @param types
     * @param id
     * @return
     */
    List<QuestionTypeEntity> getQuestionTypeAsTree(List<QuestionTypeEntity> types, Integer id);


    /**
     * 获取树的叶子节点
     * @param root
     * @param idList
     */
    void getLeave(List<QuestionTypeEntity> root,List<Integer> idList);


    /**
     * 根据子分类获取到子分类的路径(id列表)
     *
     * @param list
     * @param typeEntity
     * @return
     */
    List<QuestionTypeEntity> getPath(List<QuestionTypeEntity> idList, List<QuestionTypeEntity> list, QuestionTypeEntity typeEntity);

    /**
     * 获取默认路径
     *
     * @param typeList
     * @param questionTypeEntity
     * @return
     */
    List<QuestionTypeEntity> getDefaultPath(List<QuestionTypeEntity> typeList, QuestionTypeEntity questionTypeEntity);

}

