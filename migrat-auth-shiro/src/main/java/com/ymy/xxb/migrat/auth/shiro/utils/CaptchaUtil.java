package com.ymy.xxb.migrat.auth.shiro.utils;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.ymy.xxb.migrat.auth.shiro.constant.ShiroConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类，重写 {@link com.wf.captcha.utils.CaptchaUtil}
 * 因为它没有提供修改验证码类型方法
 *
 * @author wangyi
 */
@Component
public class CaptchaUtil {
		
	@Autowired
	private RedisTemplate<String, Serializable> redisTemplate;
	
    // gif 类型验证码
    private static final int GIF_TYPE = 1;
    // png 类型验证码
    private static final int PNG_TYPE = 0;

    // 验证码图片默认高度
    private static final int DEFAULT_HEIGHT = 48;
    // 验证码图片默认宽度
    private static final int DEFAULT_WIDTH = 130;
    // 验证码默认位数
    private static final int DEFAULT_LEN = 5;

    public  void out(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_LEN, request, response);
    }

    public  void out(int len, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, null, request, response);
    }

    public  void out(int len, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, null, font, request, response);
    }

    public  void out(int width, int height, int len, Integer vType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        out(width, height, len, vType, null, request, response);
    }

    public  void out(int width, int height, int len, Integer vType, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        outCaptcha(width, height, len, font, GIF_TYPE, vType, request, response);
    }

    public  void outPng(HttpServletRequest request, HttpServletResponse response) throws IOException {
        outPng(DEFAULT_LEN, request, response);
    }

    public  void outPng(int len, HttpServletRequest request, HttpServletResponse response) throws IOException {
        outPng(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, null, request, response);
    }

    public  void outPng(int len, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        outPng(DEFAULT_WIDTH, DEFAULT_HEIGHT, len, null, font, request, response);
    }

    public boolean outPng(int width, int height, int len, Integer vType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return outPng(width, height, len, vType, null, request, response);
    }

    public  boolean outPng(int width, int height, int len, Integer vType, Font font, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return outCaptcha(width, height, len, font, PNG_TYPE, vType, request, response);
    }

    public  boolean verify(String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String key = ShiroConst.CAPTCHA_PREFIX + session.getId();
        String sessionCode = "";
        Serializable serializable = redisTemplate.opsForValue().get(key);
		if (serializable != null) {
			sessionCode = serializable.toString();
		}
        return StringUtils.equalsIgnoreCase(code, sessionCode);
    }

    private boolean outCaptcha(int width, int height, int len, Font font, int cType, Integer vType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        setHeader(response, cType);
        Captcha captcha = null;
        if (cType == GIF_TYPE) {
            captcha = new GifCaptcha(width, height, len);
        } else {
            captcha = new SpecCaptcha(width, height, len);
        }
        if (font != null) {
            captcha.setFont(font);
        }
        if (vType != null) {
            captcha.setCharType(vType);
        }
        HttpSession session = request.getSession();
        String code = captcha.text().toLowerCase();
        String key = ShiroConst.CAPTCHA_PREFIX + session.getId();

        redisTemplate.opsForValue().set(key, code, 120000L, TimeUnit.MILLISECONDS);

        return captcha.out(response.getOutputStream());
    }

    public  void setHeader(HttpServletResponse response, int cType) {
        if (cType == GIF_TYPE) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
