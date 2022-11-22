package com.bjpn.workbench.service;

import com.bjpn.workbench.bean.Activity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ${author}
 * @since 2022-11-11
 */
public interface ActivityService extends IService<Activity> {
    //查询
    List<Activity> findAll(int pageNum,int pageSize,String activityName,String userName,
                           String activityStartDate,String activityEndDate);
    //查询市场所有活动
    List<Activity> exportActivityAll();
    //选择导出
    List<Activity> exportActivityChecked(List<String> list);


    //批量导入
    boolean importExcelByList(List<Activity> list);

    //用Id来查询
    Activity exportActivityAllById(int id);
}
