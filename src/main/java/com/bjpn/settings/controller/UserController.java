package com.bjpn.settings.controller;


import com.bjpn.settings.bean.User;
import com.bjpn.settings.service.UserService;
import com.bjpn.util.ReturnObject;
import com.bjpn.workbench.bean.Activity;
import com.bjpn.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2022-11-10
 */
@Controller
@RequestMapping("/workbench/activity")
public class UserController {
    @Autowired
    UserService userService;
    //查询用户信息
    @RequestMapping("/findUser.action")
    @ResponseBody
    public List<User> findUser(){
        List<User> userList = userService.findAllUser();
        return userList;
    }
}

