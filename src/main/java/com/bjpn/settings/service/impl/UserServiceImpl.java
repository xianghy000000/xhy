package com.bjpn.settings.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bjpn.settings.bean.User;
import com.bjpn.settings.mapper.UserMapper;
import com.bjpn.settings.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-11-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //忽略对象检查
    @Autowired(required = false)
    UserMapper userMapper;
    @Override
    public User loginUser(String userCode, String userPwd) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.eq("user_code", userCode);
        wrapper.eq("user_pwd", userPwd);
        User user = userMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public List<User> findAllUser() {
        //查询用户 锁定状态为1   失效时间大于当前时间
        QueryWrapper<User> wrapper=Wrappers.query();
        //判断锁定状态是否为1
        wrapper.eq("user_lock_state", 1);
        //获取当前时间 类型为 yyyy-MM-dd
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String nowTime = sdf.format(date);
        wrapper.gt("user_expire_time", nowTime);
        return userMapper.selectList(wrapper);

    }
}
