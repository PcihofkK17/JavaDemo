package com.example.demo.wxoauth2;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信授权获取用户基本信息测试
 * Created by bf on 2018/1/20.
 */
@RestController
@RequestMapping("/oauth2")
public class Wxoauth2Demo {

    public static final String APPID = "wx24480bb809dff9fa";

    public static final String USERINFO = "snsapi_userinfo";
;
    public static final String OAUTH111 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    public static final String CODE = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";


    @RequestMapping("/getCode")
    public String getCode(HttpServletRequest request, HttpServletResponse response
        , String code){
        System.out.println("code: " + code);

        String codeUrl = CODE.replaceAll("APPID", APPID).replaceAll("CODE", code);

        return "redirect:" + codeUrl;
    }






}
