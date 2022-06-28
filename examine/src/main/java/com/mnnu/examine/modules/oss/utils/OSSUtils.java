package com.mnnu.examine.modules.oss.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.mnnu.examine.common.exception.GwyException;
import com.mnnu.examine.common.utils.GwyConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Date;

/**
 *
 * oss 的工具类
 * 将视频的文件名改为能够访问的路径
 * @author qiaoh
 */
@Component
public class OSSUtils {


    @Resource
    OSS ossClient;

    @Value("${roleArn}")
    private String roleArn;

    @Value("${videoBucket}")
    private String videoBucket;

    /**
     * 将存在数据库的视频地址拼成可以访问的地址
     *
     * @param
     * @return
     */
    public String authentication(String objectName) throws ClientException {
        if (StringUtils.isBlank(objectName)) {
            throw new GwyException(GwyConstant.BizCode.VALID_EXCEPTION.getCode(), "视频对象名不合法");
        }

        if (objectName.startsWith("http")) {
            return objectName;
        }
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-shenzhen.aliyuncs.com/";
        // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
        AssumeRoleResponse.Credentials credentials = getSecurityToken();
        assert credentials != null;
        String tempAccessKey = credentials.getAccessKeyId();
        String tempAccessKeySecret = credentials.getAccessKeySecret();
        // 从STS服务获取的安全令牌（SecurityToken）。
        String securityToken = credentials.getSecurityToken();
        // 填写Bucket名称，例如examplebucket。
        // 填写Object完整路径，例如exampleobject.txt。Object完整路径中不能包含Bucket名称。

        // 创建OSSClient实例。
        ossClient = new OSSClientBuilder().build(endpoint, tempAccessKey, tempAccessKeySecret, securityToken);

        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000 * 10);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(videoBucket, objectName, expiration);
        return String.valueOf(url);

    }

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessKeyId;
    @Value("${spring.cloud.alicloud.secret-key}")
    private String accessKeySecret;

    /**
     * 用于获取sts服务的securityToken
     *
     * @return
     */
    private AssumeRoleResponse.Credentials getSecurityToken() throws ClientException {
        // STS接入地址，例如sts.cn-hangzhou.aliyuncs.com。
        String endpoint = "sts.cn-shenzhen.aliyuncs.com";


        // 自定义角色会话名称，用来区分不同的令牌，例如可填写为SessionTest。
        String roleSessionName = "videoAccess";

        // 以下Policy用于限制仅允许使用临时访问凭证向目标存储空间examplebucket上传文件。
        // 临时访问凭证最后获得的权限是步骤4设置的角色权限和该Policy设置权限的交集，即仅允许将文件上传至目标存储空间examplebucket下的exampledir目录。
        String policy = "{\n" +
                "    \"Version\": \"1\",\n" +
                "    \"Statement\": [\n" +
                "     {\n" +
                "           \"Effect\": \"Allow\",\n" +
                "           \"Action\": [\n" +
                "             \"oss:*\"\n" +
                "           ],\n" +
                "           \"Resource\": [\n" +
                "             \"acs:oss:*:*:*\"\n" +
                "           ]\n" +
                "     }\n" +
                "    ]\n" +
                "}\n";

        try {
            // regionId表示RAM的地域ID。以华东1（杭州）地域为例，regionID填写为cn-hangzhou。也可以保留默认值，默认值为空字符串（""）。
            String regionId = "";
            // 添加endpoint。
            DefaultProfile.addEndpoint(regionId, "Sts", endpoint);
            // 构造default profile。
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            // 构造client。
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            // 如果policy为空，则用户将获得该角色下所有权限。
            request.setPolicy(policy);
            // 设置临时访问凭证的有效时间为3600秒。
            request.setDurationSeconds(3600L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response.getCredentials();
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
        }
        return null;
    }
}



