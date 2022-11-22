package com.bjpn.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bjpn.workbench.bean.Activity;
import com.bjpn.workbench.mapper.ActivityMapper;
import com.bjpn.workbench.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2022-11-11
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Autowired(required = false)
    ActivityMapper activityMapper;
    @Override
    public List<Activity> findAll(int pageNum,int pageSize,String activityName,String userName,
                                  String activityStartDate,String activityEndDate) {
        //sql语句被调用之前 就是分页插件引入的位置
        PageHelper.startPage(pageNum, pageSize);
        //注意：分页信息必须在要分页的sql的上一行  他俩得紧挨着
//
        if (activityName == null) {
            activityName="";
        }
        if (userName == null) {
            userName="";
        }
        if (activityStartDate == null) {
            activityStartDate="";
        }
        if (activityEndDate == null) {
            activityEndDate="";
        }
        List<Activity> activityList = activityMapper.findAll(activityName,userName,activityStartDate,activityEndDate);
        return activityList;
    }

    @Override
    public List<Activity> exportActivityAll() {
        List<Activity> activityList = activityMapper.exportActivityAll();
        return activityList;
    }

    @Override
    public List<Activity> exportActivityChecked(List<String> list) {
        List<Activity> activityList = activityMapper.exportActivityChecked(list);
        return activityList;
    }

    @Override
    @Transactional
    public boolean importExcelByList(List<Activity> list) {
        return  activityMapper.importActivityByList(list);
    }

    //用Id来查询
    @Override
    public Activity exportActivityAllById(int id) {
        return activityMapper.exportActivityAllById(id);
    }

}
