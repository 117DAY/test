package io.renren.modules.exam.utils;

import io.renren.modules.oss.cloud.OSSFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 获取word文档中图片的工具类
 *
 * @author jljy
 */
public class ImageParseUtil {

    public String parse(byte[] data, String extName) {
        return parse(new ByteArrayInputStream(data), extName);
    }


    public String parse(InputStream in, String extName) {
        String suffix = extName.substring(extName.lastIndexOf("."));
        String path = OSSFactory.build().uploadSuffix(in, suffix);
        return path;
    }
}
