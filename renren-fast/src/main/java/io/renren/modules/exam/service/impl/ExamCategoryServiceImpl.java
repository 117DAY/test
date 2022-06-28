package io.renren.modules.exam.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.exam.dao.ExamCategoryDao;
import io.renren.modules.exam.entity.ExamCategoryEntity;
import io.renren.modules.exam.service.ExamCategoryService;
import io.renren.modules.exam.vo.ExamCategoryVO;
import io.swagger.models.auth.In;
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
                new QueryWrapper<ExamCategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ExamCategoryVO> withTree() {
        List<ExamCategoryEntity> list = this.list();
        return this.withTree(list);
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


    private void addChildren(@org.jetbrains.annotations.NotNull List<ExamCategoryEntity> list, ExamCategoryVO vo) {
        list.forEach(e -> {
            if (e.getParentId().equals(vo.getId())) {
                ExamCategoryVO examCategoryVO = new ExamCategoryVO();
                BeanUtils.copyProperties(e, examCategoryVO);
                vo.getChildren().add(examCategoryVO);
                addChildren(list, examCategoryVO);
            }
        });
    }

    @Override
    public List<Long> getPathById(List<Long> idList, List<ExamCategoryEntity> list, ExamCategoryEntity examCategoryEntity) {
        idList.add(examCategoryEntity.getId());
        if (examCategoryEntity.getParentId().equals(0L)) {
            return idList;
        }
        ExamCategoryEntity parentCategory = list.stream().filter(category ->
                Objects.equals(category.getId(), examCategoryEntity.getParentId())).findFirst().get();
        return getPathById(idList, list, parentCategory);
    }

}
