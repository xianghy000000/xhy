package com.bjpn.settings.service;

import com.bjpn.settings.bean.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpn.workbench.bean.Activity;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-11-10
 */
public interface UserService extends IService<User> {
    //登录的方法
    User loginUser(String userCode,String userPwd);
    //查询所有可用用户
    List<User> findAllUser();

}
