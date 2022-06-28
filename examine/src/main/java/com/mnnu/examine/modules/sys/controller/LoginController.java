package com.mnnu.examine.modules.sys.controller;

import com.mnnu.examine.common.utils.IPUtils;
import com.mnnu.examine.common.utils.R;
import com.mnnu.examine.modules.sys.service.CodeService;
import com.mnnu.examine.modules.sys.service.SmsService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author qiaoh
 */
@Controller
@RequestMapping("sys/login")
public class LoginController {


    @Resource
    CodeService codeService;

    @Resource
    SmsService smsService;

    @RequestMapping("/kaptcha")
    public void kaptcha(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setHeader("Cache-Control", "no-store, no-cache");
        resp.setContentType("image/jpeg");

        String ip = IPUtils.getIpAddr(req);

        ServletOutputStream out = resp.getOutputStream();
        BufferedImage image = codeService.createCodeWithIP(ip);
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    @RequestMapping("/send/msg")
    public R sendMsg(@RequestParam("phone") String phone) {
//     smsService
        return R.ok();
    }
}
