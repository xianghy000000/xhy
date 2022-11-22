package com.bjpn.settings.controller;

import com.bjpn.settings.bean.User;
import com.bjpn.settings.service.UserService;
import com.bjpn.util.MessageUtil;
import com.bjpn.util.ReturnObject;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    UserService userService;
    @RequestMapping("/toLogin.action")
    public String toLogin(){
        return "settings/qx/user/login";
    }

    @RequestMapping("loginUser.action")
    @ResponseBody
    public ReturnObject loginUser(String userCode, String userPwd,Boolean loginCheck,ReturnObject returnObject,
                                  HttpServletResponse response, HttpSession session) throws ParseException {
        User user = userService.loginUser(userCode, userPwd);
        //判断是否有用户用户
        if (user!=null) {
            //判断用户状态
            if ("0".equals(user.getUserLockState())) {
                returnObject.setMessageCode(MessageUtil.LOCK_ERROR_CODE);
                returnObject.setMessageStr(MessageUtil.LOCK_ERROR_STR);
                return returnObject;
            }
            //要判断是否过期,由于数据库时间类型是string要先转化时间戳
            //获取当前时间戳
            long nowTime=System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //把数据库的时间转化成时间戳
            long expireTime = sdf.parse(user.getUserExpireTime()).getTime();
            //判断是否过期
            if (expireTime<nowTime) {
                returnObject.setMessageCode(MessageUtil.TIME_OUT_CODE);
                returnObject.setMessageStr(MessageUtil.TIME_OUT_STR);
                return returnObject;
            }
            //登录成功
            if(loginCheck){
                //记住密码
                Cookie cookieCode = new Cookie("loginCode", userCode);
                Cookie cookiePwd = new Cookie("loginPwd", userPwd);
                cookieCode.setPath("/");
                cookiePwd.setPath("/");
                cookieCode.setMaxAge(60*60*24*10);
                cookiePwd.setMaxAge(60*60*24*10);
                response.addCookie(cookieCode);
                response.addCookie(cookiePwd);
            }else{
                Cookie cookieCode = new Cookie("loginCode", userCode);
                Cookie cookiePwd = new Cookie("loginPwd", userPwd);
                cookieCode.setPath("/");
                cookiePwd.setPath("/");
                cookieCode.setMaxAge(0);
                cookiePwd.setMaxAge(0);
                response.addCookie(cookieCode);
                response.addCookie(cookiePwd);
            }
            //把User放在session中
            session.setAttribute("user", user);
            returnObject.setMessageCode(MessageUtil.LOGIN_SUCCESS_CODE);
            returnObject.setMessageStr(MessageUtil.LOGIN_SUCCESS_STR);

        }else{
            returnObject.setMessageCode(MessageUtil.LOGIN_FAIL_CODE);
            returnObject.setMessageStr(MessageUtil.LOGIN_FAIL_STR);
        }
        return returnObject;
    }
}

