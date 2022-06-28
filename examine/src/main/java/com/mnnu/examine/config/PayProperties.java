package com.mnnu.examine.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付的相关参数
 *
 * @author qiaoh
 */
@Component
@ConfigurationProperties(prefix = "ali.pay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayProperties {
    private String appId;
    private String merchantPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
    /**
     * 公钥私钥的加密方式
     */
    private String signType;
    private String charset;
    private String gatewayUrl;
}
