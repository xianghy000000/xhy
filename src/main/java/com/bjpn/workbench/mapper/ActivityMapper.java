package com.bjpn.workbench.mapper;

import com.bjpn.workbench.bean.Activity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ${author}
 * @since 2022-11-11
 */
public interface ActivityMapper extends BaseMapper<Activity> {
    //查询
    List<Activity> findAll(@Param("activityName") String activityName,@Param("userName") String userName,
                           @Param("activityStartDate") String activityStartDate,@Param("activityEndDate") String activityEndDate);
    //全部导出
    List<Activity> exportActivityAll();
    //选择导出
    List<Activity> exportActivityChecked(List<String> list);

    //文件上传
    boolean  importActivityByList(List<Activity> activityList);
    //用Id来查询
    Activity exportActivityAllById(int id);

    //修改
    boolean updateActivity(Activity activity);
}
