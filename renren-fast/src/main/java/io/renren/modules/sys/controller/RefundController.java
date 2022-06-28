package io.renren.modules.sys.controller;

import com.google.gson.Gson;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.sys.entity.RefundEntity;
import io.renren.modules.sys.service.RefundService;
import io.renren.modules.sys.vo.RefundVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author 自动生成
 * @email generat
 * @date 2021-12-05 21:42:47
 */
@RestController
@RequestMapping("sys/refund")
public class RefundController extends AbstractController {
    @Autowired
    private RefundService refundService;

    @Resource
    Gson gson;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = refundService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        RefundEntity refund = refundService.getById(id);

        return R.ok().put("refund", refund);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody RefundEntity refund) {
        refundService.save(refund);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody RefundEntity refund) {
        refundService.updateById(refund);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        refundService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 交易退款
     * agree 1 同意 2 拒绝
     * <p>
     * agree photos orderId mark
     *
     * @return {@link R}
     */
    @RequestMapping("/agree/refund")
    public R dealRefund(@RequestBody RefundVO refundVO
    ) {
        Integer agree = refundVO.getAgree();
        String orderId = refundVO.getOrderId();
        String mark = refundVO.getMark();
        List<String> photoList = refundVO.getPhotos();
        String photos = gson.toJson(photoList);
        if (agree != null
                && StringUtils.isNotBlank(orderId)
                && StringUtils.isNotBlank(mark)
                && StringUtils.isNotBlank(photos)) {
            Integer agree1 = Integer.valueOf(agree);
            RefundEntity refund = new RefundEntity();
            String operator = getUser().getUsername();
            // 设置操作员、是否同意退款、退款截图、操作员记录、订单号
            refund.setOperator(operator);
            refund.setStatus(agree1);
            refund.setOrderId(orderId);
            refund.setRefundScreenshots(photos);
            refund.setRemark(mark);
            refund.setFinishTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
            refundService.dealRefund(refund, agree1);
            return R.ok();
        } else {
            return R.error();
        }

    }

    @RequestMapping("/orderid/info")
    public R getRefundInfo(@RequestBody Map<String, String> params) {
        String orderId = params.get("orderId");
        RefundEntity refundByOrderId = refundService.getRefundByOrderId(orderId);
        return R.ok().put("refund", refundByOrderId);
    }


}
