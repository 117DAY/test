package com.mnnu.examine.modules.exam.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.PageUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.exam.entity.ExamCategoryEntity;
import com.mnnu.examine.modules.exam.service.ExamCategoryService;
import com.mnnu.examine.modules.exam.vo.ExamCategoryShowVO;
import com.mnnu.examine.modules.exam.vo.ExamCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 自动生成
 * @email generate
 * @date 2021-11-14 19:34:57
 */
@RestController
@RequestMapping("exam/examcategory")
public class ExamCategoryController {
    @Autowired
    private ExamCategoryService examCategoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = examCategoryService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 获取所有分类的树形结构
     *
     * @return
     */
    @RequestMapping("/tree")
    public R getAllCateTree() {
        List<ExamCategoryVO> categoryVOList = examCategoryService.withTree(true);
        categoryVOList.forEach(e -> {
            ExamCategoryVO lt = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.LT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.LT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO at = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.AT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.AT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO it = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.TITORIT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.TITORIT.getName())
                    .setParentId(GwyConstant.ExamType.GWY.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO st = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.ST.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.ST.getName())
                    .setParentId(GwyConstant.ExamType.SYDW.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            ExamCategoryVO wt = new ExamCategoryVO()
                    .setId(GwyConstant.ExamType.WT.getType().longValue())
                    .setCategoryName(GwyConstant.ExamType.WT.getName())
                    .setParentId(GwyConstant.ExamType.SYDW.getType().longValue())
                    .setUuid(UUID.randomUUID().toString());
            if (GwyConstant.ExamType.GWY.getType().longValue() == e.getId()) {
                lt.setChildren(e.getChildren());
                at.setChildren(e.getChildren());
                it.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(lt, at, it));
            } else if (GwyConstant.ExamType.SYDW.getType().longValue() == e.getId()) {
                st.setChildren(e.getChildren());
                wt.setChildren(e.getChildren());
                it.setId(GwyConstant.ExamType.IT.getType().longValue());
                it.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(st, wt, it));
            } else {
                lt.setChildren(e.getChildren());
                at.setChildren(e.getChildren());
                it.setChildren(e.getChildren());
                st.setChildren(e.getChildren());
                wt.setChildren(e.getChildren());
                e.setChildren(Arrays.asList(lt, at, st, wt, it));
            }
        });
        return R.ok().put("tree", categoryVOList);
    }

    /**
     * 根据id获取子分类数组
     *
     * @return
     */
    @RequestMapping("/children/{id}")
    public R getChildren(@PathVariable("id") Integer id) {
        List<ExamCategoryEntity> list = examCategoryService.list(
                new QueryWrapper<ExamCategoryEntity>().eq("parent_id", id));
        List<ExamCategoryEntity> collect = list.stream().sorted(
                Comparator.comparingInt(ExamCategoryEntity::getSort)).collect(Collectors.toList());
        return R.ok().put("list", collect);
    }


    /**
     * 获取试卷类型的树形结构
     *
     * @return
     */
    @RequestMapping("/tree/{excludeId}")
    public R getCateAsTree(@PathVariable("excludeId") Long excludeId) {
        List<ExamCategoryEntity> list = examCategoryService.list();

        if (!excludeId.equals(-1L)) {
            list = list.stream().filter(
                    e -> !excludeId.equals(e.getId())).collect(Collectors.toList());
        }
        List<ExamCategoryVO> categoryVOS = examCategoryService.withTree(
                list.stream().sorted(Comparator.comparingInt(ExamCategoryEntity::getSort)).collect(Collectors.toList()));

        return R.ok().put("data", categoryVOS);
    }

    /**
     * 获取所有路径
     *
     * @return
     * @author jljy
     */
    @RequestMapping("/path/all")
    public R getAllPath() {
        List<String> path = new ArrayList<>();
        List<List<String>> allPath = new ArrayList<>();
        List<ExamCategoryVO> examCategoryVOS = examCategoryService.withTree();

        //设置根节点
        List<ExamCategoryVO> list = new ArrayList<>();
        ExamCategoryVO examCategoryVO = new ExamCategoryVO();
        examCategoryVO.setId(0L);
        examCategoryVO.setParentId(-1L);
        examCategoryVO.setChildren(examCategoryVOS);
        list.add(examCategoryVO);

        for (ExamCategoryVO categoryVO : list) {
            examCategoryService.getAllPath(categoryVO, path, allPath);
        }

        Map<String, List<String>> map = new LinkedHashMap<>();

        allPath.forEach(e -> {
            String remove = e.remove(e.size() - 1);
            String root = String.join(" / ", e);
            if (map.get(root) == null) {
                List<String> children = new ArrayList<>();
                children.add(remove);
                map.put(root, children);

            } else {
                map.get(root).add(remove);
            }
        });
        List<ExamCategoryShowVO> showVOList = new ArrayList<>();
        ExamCategoryShowVO rootVO = new ExamCategoryShowVO();
        rootVO.setDirection("全部");
        rootVO.getData().add("全部");

        ExamCategoryShowVO childVO = new ExamCategoryShowVO();
        childVO.setDirection("二级");
        List<Object> childVOList = new ArrayList<>();

//        ExamCategoryShowVO defaultChildVO = new ExamCategoryShowVO();
//        defaultChildVO.setDirection("全部");
//        List<Object> defaultData = new ArrayList<>();
//        defaultData.add("不限");
//        defaultChildVO.setData(defaultData);
//        childVOList.add(defaultChildVO);

        for (String s : map.keySet()) {
            rootVO.getData().add(s);
            ExamCategoryShowVO showVO = new ExamCategoryShowVO();
            showVO.setDirection(s);
            List<Object> data = new ArrayList<>(map.get(s));
            showVO.setData(data);
            childVOList.add(showVO);
        }
        childVO.setData(childVOList);
        showVOList.add(rootVO);
        showVOList.add(childVO);

        return R.ok().put("data", showVOList);
    }

    /**
     * 根据标签名字获取根分类,并返回对应的filter
     *
     * @param name
     * @return
     */
    @RequestMapping("/filter")
    public R getFilterByName(@RequestParam String name) {
        //获取所有试卷分类
        List<ExamCategoryEntity> list = examCategoryService.list();
        //根据名字找到需要查找的试卷分类
        ExamCategoryEntity categoryEntity = list.stream().filter(examCategoryEntity
                -> examCategoryEntity.getCategoryName().equals(name)).findFirst().orElse(null);
        //获取其对应的根分类
        ExamCategoryEntity root = examCategoryService.getRoot(list, categoryEntity);
        //判断根分类信息，从而返回对应的filter标签
        List<Map<String, Object>> filter = new ArrayList<>();
        if (GwyConstant.ExamType.GWY.getType().longValue() == root.getId()) {
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.LT.getType());
                put("title", GwyConstant.ExamType.LT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.AT.getType());
                put("title", GwyConstant.ExamType.AT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.TITORIT.getType());
                put("title", GwyConstant.ExamType.TITORIT.getName());
            }});
        } else if (GwyConstant.ExamType.SYDW.getType().longValue() == root.getId()) {
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.ST.getType());
                put("title", GwyConstant.ExamType.ST.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.WT.getType());
                put("title", GwyConstant.ExamType.WT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.TITORIT.getType());
                put("title", GwyConstant.ExamType.TITORIT.getName());
            }});
        } else {
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.LT.getType());
                put("title", GwyConstant.ExamType.LT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.AT.getType());
                put("title", GwyConstant.ExamType.AT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.TITORIT.getType());
                put("title", GwyConstant.ExamType.TITORIT.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.ST.getType());
                put("title", GwyConstant.ExamType.ST.getName());
            }});
            filter.add(new HashMap<String, Object>() {{
                put("id", GwyConstant.ExamType.WT.getType());
                put("title", GwyConstant.ExamType.WT.getName());
            }});
        }
        return R.ok().put("filter", filter);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ExamCategoryEntity examCategory = examCategoryService.getById(id);

        return R.ok().put("examCategory", examCategory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ExamCategoryEntity examCategory) {
        examCategoryService.save(examCategory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ExamCategoryEntity examCategory) {
        examCategoryService.updateById(examCategory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        examCategoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
