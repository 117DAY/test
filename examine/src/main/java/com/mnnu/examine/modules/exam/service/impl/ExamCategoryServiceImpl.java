package com.mnnu.examine.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.Query;
import com.mnnu.examine.modules.exam.dao.ExamCategoryDao;
import com.mnnu.examine.modules.exam.entity.ExamCategoryEntity;
import com.mnnu.examine.modules.exam.service.ExamCategoryService;
import com.mnnu.examine.modules.exam.vo.ExamCategoryVO;
import com.mnnu.examine.modules.question.entity.QuestionTypeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("examCategoryService")
public class ExamCategoryServiceImpl extends ServiceImpl<ExamCategoryDao, ExamCategoryEntity> implements ExamCategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamCategoryEntity> page = this.page(
                new Query<ExamCategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

    @Override
    public ExamCategoryEntity getRoot(List<ExamCategoryEntity> list, ExamCategoryEntity examCategoryEntity) {
        if (examCategoryEntity.getParentId().equals(0L)) {
            return examCategoryEntity;
        }
        ExamCategoryEntity parentCategory = list.stream().filter(
                type -> Objects.equals(type.getId(), examCategoryEntity.getParentId())
        ).findFirst().get();
        return getRoot(list, parentCategory);
    }

    @Override
    public List<ExamCategoryVO> withTree() {
        List<ExamCategoryEntity> list = this.list();
        List<ExamCategoryEntity> collect = list.stream().sorted(
                Comparator.comparingInt(ExamCategoryEntity::getSort)).collect(Collectors.toList());
        return this.withTree(collect);
    }

    @Override
    public List<ExamCategoryVO> withTree(List<ExamCategoryEntity> list) {
        List<ExamCategoryVO> vos = new ArrayList<>();
        list.forEach(e -> {
            if (e.getParentId().equals(0L)) {
                ExamCategoryVO examCategoryVO = new ExamCategoryVO();
                BeanUtils.copyProperties(e, examCategoryVO);
                addChildren(list, examCategoryVO);
                vos.add(examCategoryVO);
            }
        });
        return vos;
    }

    @Override
    public List<ExamCategoryVO> withTree(boolean withUUID) {
        List<ExamCategoryEntity> list = this.list();
        Integer max = Collections.max(
                list.stream().map(ExamCategoryEntity::getCategoryLevel).collect(Collectors.toList()));
        List<ExamCategoryEntity> collect = list.stream().filter(
                e -> !e.getCategoryLevel().equals(max)).collect(Collectors.toList());
        List<ExamCategoryVO> vos = new ArrayList<>();
        collect.forEach(e -> {
            if (e.getParentId().equals(0L)) {
                ExamCategoryVO examCategoryVO = new ExamCategoryVO();
                BeanUtils.copyProperties(e, examCategoryVO);
                examCategoryVO.setUuid(UUID.randomUUID().toString());
                addChildren(collect, examCategoryVO);
                vos.add(examCategoryVO);
            }
        });
        return vos;
    }

    @Override
    public void getAllPath(ExamCategoryVO node, List<String> path, List<List<String>> allPah) {
        if (node.getChildren().isEmpty()) {
            allPah.add(path);
            return;
        }
        for (ExamCategoryVO cNode : node.getChildren()) {
            List<String> cPath = new ArrayList<>(path);
            cPath.add(cNode.getCategoryName());
            getAllPath(cNode, cPath, allPah);
        }
    }


    private void addChildren(@org.jetbrains.annotations.NotNull List<ExamCategoryEntity> list, ExamCategoryVO vo) {
        list.forEach(e -> {
            if (e.getParentId().equals(vo.getId())) {
                ExamCategoryVO examCategoryVO = new ExamCategoryVO();
                BeanUtils.copyProperties(e, examCategoryVO);
                examCategoryVO.setUuid(UUID.randomUUID().toString());
                vo.getChildren().add(examCategoryVO);
                addChildren(list, examCategoryVO);
            }
        });
    }


}
