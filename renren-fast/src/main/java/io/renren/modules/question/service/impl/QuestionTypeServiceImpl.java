package io.renren.modules.question.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.exam.vo.ExamCategoryVO;
import io.renren.modules.question.dao.QuestionTypeDao;
import io.renren.modules.question.entity.QuestionTypeEntity;
import io.renren.modules.question.service.QuestionTypeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("questionTypeService")
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeDao, QuestionTypeEntity> implements QuestionTypeService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionTypeEntity> page = this.page(
                new Query<QuestionTypeEntity>().getPage(params),
                new QueryWrapper<QuestionTypeEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<QuestionTypeEntity> getQuestionTypeAsTree(Integer id) {
        //1.查询所有分类
        List<QuestionTypeEntity> types = this.list();
        return this.getQuestionTypeAsTree(types, id);
    }

    @Override
    public List<QuestionTypeEntity> getQuestionTypeAsTree(List<QuestionTypeEntity> types, Integer id) {
        return types.stream().filter(category ->
                category.getParentId().equals(id))
                .peek(childType -> childType.setChildren(getChildCategories(childType, types)))
                .sorted(Comparator.comparingInt(QuestionTypeEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public void getLeave(List<QuestionTypeEntity> root, List<Integer> idList) {
        root.forEach(e -> {
            if (e.getChildren().size() == 0) {
                idList.add(e.getId());
            } else {
                getLeave(e.getChildren(), idList);
            }
        });
    }


    @Override
    public List<QuestionTypeEntity> getPath(List<QuestionTypeEntity> idList, List<QuestionTypeEntity> list, QuestionTypeEntity typeEntity) {
        idList.add(typeEntity);
        if (typeEntity.getParentId().equals(1) || typeEntity.getParentId().equals(2) || typeEntity.getParentId().equals(3)) {
            return idList;
        }
        QuestionTypeEntity parentType = list.stream().filter(
                type -> Objects.equals(type.getId(), typeEntity.getParentId())
        ).findFirst().get();
        return getPath(idList, list, parentType);
    }


    //递归查找所有菜单的子菜单
    private List<QuestionTypeEntity> getChildCategories(QuestionTypeEntity root, List<QuestionTypeEntity> all) {
        return all.stream().filter(category -> category.getParentId().equals(root.getId()))
                .peek(examCategory -> {
                    examCategory.setChildren(getChildCategories(examCategory, all));
                })
                .sorted(Comparator.comparingInt(QuestionTypeEntity::getSort))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionTypeEntity> getDefaultPath(List<QuestionTypeEntity> typeList, QuestionTypeEntity questionTypeEntity) {
        typeList.add(questionTypeEntity);
        if (questionTypeEntity.getChildren().size() == 0) {
            return typeList;
        }
        return getDefaultPath(typeList, questionTypeEntity.getChildren().get(0));
    }

}
