package com.mnnu.examine.modules.sys.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.GwyUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.config.PayProperties;
import com.mnnu.examine.modules.course.entity.CourseEntity;
import com.mnnu.examine.modules.course.entity.CourseOrderEntity;
import com.mnnu.examine.modules.course.service.CourseOrderService;
import com.mnnu.examine.modules.course.service.CourseService;
import com.mnnu.examine.modules.live.entity.LiveEntity;
import com.mnnu.examine.modules.live.service.LiveService;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.vo.CourseOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * 支付相关
 *
 * @author qiaoh
 */
@Controller
@Slf4j
@RequestMapping("/sys/pay")
public class PayController {
    @Resource
    PayProperties payProperties;

    @Resource
    CourseOrderService courseOrderService;

    @Resource
    CourseService courseService;

    @Resource
    LiveService liveService;

    @Resource
    private Gson gson;

    /**
     * 生成订单
     * 不可重复购买
     *
     * @param vo 签证官
     * @return {@link String}
     * @throws AlipayApiException 支付宝api例外
     */
    @ResponseBody
    @RequestMapping("/generate/order")
    @PreAuthorize("hasRole('student')")
    public String generateOrder(@RequestBody @Validated CourseOrderVO vo) throws AlipayApiException {
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        // 支付需要的四个参数 订单号、商品名、商品描述、商品价格
        String order = GwyUtils.generateOrder(userId);
        String courseTitle = null;
        String content = "";
        BigDecimal payPrice = null;

        Integer isPublic = null;
        Integer courseId = null;
        Integer courseType = vo.getCourseType();
        if (courseType.equals(GwyConstant.CourseType.RECORDED.getType())) {
            CourseEntity courseEntity = courseService.getById(vo.getCourseId());
            if (courseEntity == null || courseEntity.getIsPublic().equals(0)) {
                throw new RuntimeException("您所要购买的当前商品不存在，请确认");
            }
            isPublic = courseEntity.getIsPublic();

            courseTitle = courseEntity.getCourseTitle();
            payPrice = courseEntity.getCurrentPrice();
            courseId = courseEntity.getId();
        } else if (courseType.equals(GwyConstant.CourseType.LIVE.getType())) {
            LiveEntity liveEntity = liveService.getById(vo.getCourseId());
            if (liveEntity == null || liveEntity.getIsPublic().equals(0)) {
                throw new RuntimeException("您所要购买的当前不存在，请确认");
            }
            isPublic = liveEntity.getIsPublic();

            courseTitle = liveEntity.getCourseTitle();
            payPrice = liveEntity.getCurrentPrice();
            courseId = liveEntity.getId().intValue();
        }
        String script = courseOrderService.orderIfExist(userId, courseId, courseType);
        if (StringUtils.isNotBlank(script)) {
            return gson.toJson(R.error("订单已存在"));
        }

        if (Objects.equals(isPublic, 0)) {
            throw new RuntimeException("本课程禁止订阅");
        }

        String payMode = "支付宝";
        // 保存订单信息
        courseOrderService.createOrder(userId, vo.getCourseId(), vo.getCourseType()
                , payPrice, order, payMode);

        return sendPaymentRequest(order, courseTitle, content, payPrice);

    }

    /**
     * 重新支付未支付的订单
     *
     * @return {@link String}
     */
    @ResponseBody
    @RequestMapping("/repay/{orderId}")
    @PreAuthorize("hasRole('student')")
    public String repayOrder(@PathVariable String orderId) throws AlipayApiException {

        CourseOrderEntity orderEntity = courseOrderService.getOne(new QueryWrapper<CourseOrderEntity>().eq("order_id", orderId));
        if (orderEntity == null) {
            throw new RuntimeException("没有id为: " + orderId + "的订单");
        }

        if (!orderEntity.getStatus().equals(GwyConstant.OrderStatus.NEW.getCode())) {
            throw new RuntimeException("订单状态异常，请检查订单号是否正确");
        }
        Integer isPublic = null;
        String courseTitle = "";
        BigDecimal payPrice = null;
        Integer courseType = orderEntity.getCourseType();
        if (courseType.equals(GwyConstant.CourseType.RECORDED.getType())) {
            CourseEntity courseEntity = courseService.getById(orderEntity.getCourseId());
            isPublic = courseEntity.getIsPublic();
            courseTitle = courseEntity.getCourseTitle();
            payPrice = courseEntity.getCurrentPrice();
        } else if (courseType.equals(GwyConstant.CourseType.LIVE.getType())) {
            LiveEntity liveEntity = liveService.getById(orderEntity.getCourseId());
            isPublic = liveEntity.getIsPublic();
            courseTitle = liveEntity.getCourseTitle();
            payPrice = liveEntity.getCurrentPrice();
        }

        if (Objects.equals(isPublic, 0)) {
            throw new RuntimeException("本课程禁止订阅");
        }
        Long userId = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        String order = GwyUtils.generateOrder(userId);

        orderEntity.setOrderId(order);
        // 重新设置订单号
        courseOrderService.updateById(orderEntity);

        return sendPaymentRequest(orderEntity.getOrderId(), courseTitle, "", payPrice);
    }


    private String sendPaymentRequest(String order, String courseTitle, String content, BigDecimal payPrice) throws AlipayApiException {

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                payProperties.getGatewayUrl(),
                payProperties.getAppId(),
                payProperties.getMerchantPrivateKey(),
                "json",
                payProperties.getCharset(),
                payProperties.getAlipayPublicKey(),
                payProperties.getSignType());

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        alipayRequest.setReturnUrl(payProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(payProperties.getNotifyUrl());
        alipayRequest.setBizContent("{\"out_trade_no\":\"" + order + "\","
                + "\"total_amount\":\"" + payPrice + "\","
                + "\"subject\":\"" + courseTitle + "\","
                + "\"body\":\"" + content + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        /*   若 想 给 BizContent 增 加 其 他 可 选 请 求 参 数 ， 以 增 加 自 定 义 超 时 时 间 参 数timeout_express 来举例说明
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
         + "\"total_amount\":\""+ total_amount +"\","
         + "\"subject\":\""+ subject +"\","
         + "\"body\":\""+ body +"\","
         + "\"timeout_express\":\"10m\","
         + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");*/
        //请求参数可查阅【电脑网站支付的 API 文档-alipay.trade.page.pay-请求参数】 章节

        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    /**
     * 交易成功后走这里，然后保存信息到数据库
     *
     * @param request
     * @return
     * @throws AlipayApiException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/return")
    public String returnUrlMethod(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException,
            IOException {


        // 获取支付宝 GET 过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            // 乱码解决， 这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                //调用 SDK 验证签名
                payProperties.getSignType());
        log.debug("验签参数：" + params);
        log.debug("验签结果：" + signVerified);
        // ——请在这里编写您的程序（以下代码仅作参考） ——
        if (signVerified) {
            // 商户订单号
            String orderNum = new
                    String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 支付宝交易号
            String payOrderNum = new
                    String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            // 付款金额
            String orderAmount = new
                    String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
            // 保存到数据库

//            courseOrderService.updateOrderStatus(orderNum, payOrderNum, GwyConstant.OrderStatus.PAID.getCode());

            return "redirect:http://localhost:3400/#/userinfo/order";
        } else {
            // 页面显示信息： 验签失败
            response.setContentType("text/json;charset=utf-8");
            response.setStatus(401);
            //塞到HttpServletResponse中返回给前台
            response.getWriter().write(gson.toJson(R.error("支付过程出现问题")));
            throw new RuntimeException("支付过程出现问题");
        }
    }


    /**
     * 支付宝校验的时候调用
     *
     * @param request
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request) throws
            UnsupportedEncodingException, AlipayApiException {
        System.out.println("支付宝支付成功回调");
        //获取支付宝 POST 过来反馈信息
        Map<String, String> params = new HashMap<String, String>(15);
        Map<String, String[]> requestParams = request.getParameterMap();
        log.debug(requestParams.toString());
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决， 这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.debug(params.toString());
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                //调用 SDK 验证签名
                payProperties.getSignType());
        log.debug("验签：" + signVerified);
        //——请在这里编写您的程序（以下代码仅作参考） ——
            /* 实际验证过程建议商户务必添加以下校验：
            1、 需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号，
            2、 判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额），
            3、 校验通知中的 seller_id（ 或者 seller_email) 是否为 out_trade_no 这笔单据的对应的
            操作方（有的时候， 一个商户可能有多个 seller_id/seller_email）
            4、 验证 app_id 是否为该商户本身。
            */
        if (signVerified) {//验证成功
            //商户订单号
            String orderNum = new
                    String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //支付宝交易号
            String payOrderNum = new
                    String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
            //交易状态
            String tradeStatus = new
                    String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            // 保存到数据库
            System.out.println(tradeStatus);
            if (tradeStatus.equalsIgnoreCase("TRADE_SUCCESS")) {
                courseOrderService.updateOrderStatus(orderNum, payOrderNum, GwyConstant.OrderStatus.PAID.getCode());
            }

        } else {//验证失败
            log.error("验签失败");
            //调试用， 写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
            throw new RuntimeException("支付流程出错");
        }
    }
}
