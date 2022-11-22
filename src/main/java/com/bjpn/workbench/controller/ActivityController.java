package com.bjpn.workbench.controller;


import com.bjpn.settings.bean.User;
import com.bjpn.util.MessageUtil;
import com.bjpn.util.ReturnObject;
import com.bjpn.workbench.bean.Activity;
import com.bjpn.workbench.bean.LikeActivity;
import com.bjpn.workbench.service.ActivityService;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2022-11-11
 */
@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @RequestMapping("/toIndex.action")
    public String toIndex(){
        return "/workbench/activity/index";
    }
    @RequestMapping("/saveActivity.action")
    @ResponseBody
    public ReturnObject save(Activity activity, HttpSession session, ReturnObject returnObject){
        //获取添加人和添加时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String nowTim = sdf.format(date);
        //赋值给activity类中对应的属性
        activity.setCreateTime(nowTim);
        //获取添加人
        User user = (User) session.getAttribute("user");
        String p = user.getUserId() + "";
        //赋值给activity类中对应的属性
        activity.setCreateBy(p);
        boolean b = activityService.save(activity);
        if(b){
            returnObject.setMessageCode(MessageUtil.SUCCESS_CODE);
            returnObject.setMessageStr(MessageUtil.SUCCESS_STR);
        }else {
            returnObject.setMessageCode(MessageUtil.FAIL_CODE);
            returnObject.setMessageStr(MessageUtil.FAIL_STR);
        }
        return returnObject;
    }
    @RequestMapping("/findAllactivity.action")
    @ResponseBody
    public LikeActivity findAllactivity(@RequestParam(defaultValue = "1")Integer pageNum, @RequestParam(defaultValue = "5")Integer pageSize,
                                              String activityName, String userName,
                                              String activityStartDate, String activityEndDate, LikeActivity likeActivity){
        System.out.println("activityName"+activityName);
        List<Activity> activityList = activityService.findAll(pageNum,pageSize,activityName,userName,activityStartDate,activityEndDate);
        PageInfo<Activity> pageInfo = new PageInfo<>(activityList);
        likeActivity.setLikeActivityName(activityName);
        likeActivity.setLikeActivityOwner(userName);
        likeActivity.setLikeEndDate(activityEndDate);
        likeActivity.setLikeStartDate(activityStartDate);
        likeActivity.setPageInfoLike(pageInfo);
        System.out.println("pageInfo = " + pageInfo);
        return likeActivity;
    }

    //批量下载
    @RequestMapping("/exportActivityAll.action")
    public void exportActivityAll(HttpServletResponse response) {
        try {

            //查询数据库，获取所有信息
            List<Activity> activityList = activityService.exportActivityAll();
            //把市场活动信息 通过poi 下载到本地浏览器
            //1.创建workbook对象
            HSSFWorkbook wb = new HSSFWorkbook();
            //2、创建页
            HSSFSheet sheet = wb.createSheet("市场活动记录");
            //3、创建行 创建第一行 用作标题
            HSSFRow row = sheet.createRow(0);
            //4、创建列
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("市场活动编号");
            cell = row.createCell(1);
            cell.setCellValue("市场活动名称");
            cell = row.createCell(2);
            cell.setCellValue("市场活动所有者");
            cell = row.createCell(3);
            cell.setCellValue("市场活动开始时间");
            cell = row.createCell(4);
            cell.setCellValue("市场活动结束时间");
            cell = row.createCell(5);
            cell.setCellValue("市场活动成本");
            cell = row.createCell(6);
            cell.setCellValue("市场活动描述");
            cell = row.createCell(7);
            cell.setCellValue("市场活动创建时间");
            cell = row.createCell(8);
            cell.setCellValue("市场活动创建人");
            cell = row.createCell(9);
            cell.setCellValue("市场活动修改时间");
            cell = row.createCell(10);
            cell.setCellValue("市场活动修改人");
            //把后面的数据 拼到以下行中
            for (int i = 1; i <= activityList.size(); i++) {
                HSSFRow row1 = sheet.createRow(i);
                //获取查询的每一个数据
                Activity activity = activityList.get(i - 1);
                for (int j = 0; j < 11; j++) {
                    HSSFCell cell1 = row1.createCell(j);
                    if (j == 0) {
                        cell1.setCellValue(activity.getActivityId());
                    } else if (j == 1) {
                        cell1.setCellValue(activity.getActivityName());
                    } else if (j == 2) {
                        cell1.setCellValue(activity.getActivityOwner());
                    } else if (j == 3) {
                        cell1.setCellValue(activity.getActivityStartDate());
                    } else if (j == 4) {
                        cell1.setCellValue(activity.getActivityEndDate());
                    } else if (j == 5) {
                        cell1.setCellValue(activity.getActivityCost());
                    } else if (j == 6) {
                        cell1.setCellValue(activity.getActivityDescription());
                    } else if (j == 7) {
                        cell1.setCellValue(activity.getCreateTime());
                    } else if (j == 8) {
                        cell1.setCellValue(activity.getCreateBy());
                    } else if (j == 9) {
                        cell1.setCellValue(activity.getEditTime());
                    } else if (j == 10) {
                        cell1.setCellValue(activity.getEditBy());
                    }
                }
            }
            //完成 wb的下载
            //通知浏览器  调用浏览器的下载器
            String fileName = URLEncoder.encode("市场活动信息", "utf-8");
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(date);
            //可以设置响应头信息，使浏览器接收到响应信息之后，在下载窗口打开
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "-" + nowTime + ".xls");
            //下载
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    //选择导出
    @RequestMapping("/exportActivityChecked.action")
    public void exportActivityChecked(HttpServletResponse response,String ids){
        try {
            //ids是多个id组合  我们要把他编程数组或者集合 用于批量查询
            //以   ，  来切割
            String[] arrId = ids.split(",");
            //放到集合中
            List<String> list = Arrays.asList(arrId);
            List<Activity> activityList = activityService.exportActivityChecked(list);
            //把市场活动信息 通过poi  下载都本地浏览器
            //1、创建workbook对象
            HSSFWorkbook wb = new HSSFWorkbook();
            //2、创建页
            HSSFSheet sheet = wb.createSheet("市场活动记录");
            //3、创建行  创建第一行  用作标题
            HSSFRow row = sheet.createRow(0);
            //4、创建行  11行
            HSSFCell cell = row.createCell(0);
            cell.setCellValue("市场活动编号");
            cell = row.createCell(1);
            cell.setCellValue("市场活动名称");
            cell = row.createCell(2);
            cell.setCellValue("市场活动所有者");
            cell = row.createCell(3);
            cell.setCellValue("市场活动开始时间");
            cell = row.createCell(4);
            cell.setCellValue("市场活动结束时间");
            cell = row.createCell(5);
            cell.setCellValue("市场活动成本");
            cell = row.createCell(6);
            cell.setCellValue("市场活动描述");
            cell = row.createCell(7);
            cell.setCellValue("市场活动创建时间");
            cell = row.createCell(8);
            cell.setCellValue("市场活动创建人");
            cell = row.createCell(9);
            cell.setCellValue("市场活动修改时间");
            cell = row.createCell(10);
            cell.setCellValue("市场活动修改人");
            //把后面的数据  拼着到以下行
            for(int i=1;i<=activityList.size();i++){
                HSSFRow row1 = sheet.createRow(i);
                //获取查询的每一个数据
                Activity activity = activityList.get(i-1);
                for(int j=0;j<11;j++){
                    HSSFCell cell1 =  row1.createCell(j);
                    if(j==0){
                        cell1.setCellValue(activity.getActivityId());
                    }else if(j==1){
                        cell1.setCellValue(activity.getActivityName());
                    }else if(j==2){
                        cell1.setCellValue(activity.getActivityOwner());
                    }else if(j==3){
                        cell1.setCellValue(activity.getActivityStartDate());
                    }else if(j==4){
                        cell1.setCellValue(activity.getActivityEndDate());
                    }else if(j==5){
                        cell1.setCellValue(activity.getActivityCost());
                    }else if(j==6){
                        cell1.setCellValue(activity.getActivityDescription());
                    }else if(j==7){
                        cell1.setCellValue(activity.getCreateTime());
                    }else if(j==8){
                        cell1.setCellValue(activity.getCreateBy());
                    }else if(j==9){
                        cell1.setCellValue(activity.getEditTime());
                    }else if(j==10){
                        cell1.setCellValue(activity.getEditBy());
                    }
                }
            }
            //完成 wb的下载
            //通知浏览器  调用浏览器的下载器
            String fileName = URLEncoder.encode("市场活动选中信息", "utf-8") ;
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(date);
            //可以设置响应头信息，使浏览器接收到响应信息之后，在下载窗口打开
            response.setHeader("Content-Disposition","attachment;filename="+fileName+"-"+nowTime+".xls");
            //下载
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/importActivityExcel.action")
    @ResponseBody
    public ReturnObject importActivityExcel(MultipartFile importFile,ReturnObject returnObject) throws IOException {
        System.out.println("importFile = " + importFile);
        //通过io流  把excel中的数据  存放到workbook对象中
        InputStream in = importFile.getInputStream();
        HSSFWorkbook wb = new HSSFWorkbook(in);
        //通过poi解析数据
        HSSFSheet sheet = wb.getSheetAt(0);
        //从第二行开始
        ArrayList<Activity> activityList = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            System.out.println("行号："+sheet.getLastRowNum());
            HSSFRow row = sheet.getRow(i);
            Activity activity = new Activity();
            //从每一行中去数据  给对象赋值
            for (int j = 0; j < row.getLastCellNum(); j++) {
                HSSFCell cell = row.getCell(j);
                String cellValue = getCellValueByType(cell);
                if(j==0){
                    //activity.setActivityId();
                }else if(j==1){
                    activity.setActivityName(cellValue);
                }else if(j==2){
                    if(!"".equals(cellValue)){
                        double owner = Double.parseDouble(cellValue);
                        int activityOwner = (int)owner;
                        activity.setActivityOwner(activityOwner);
                    }
                }else if(j==3){
                    activity.setActivityStartDate(cellValue);
                }else if(j==4){
                    activity.setActivityEndDate(cellValue);
                }else if(j==5){
                    activity.setActivityCost(cellValue);
                }else if(j==6){
                    activity.setActivityDescription(cellValue);
                }else if(j==7){
                    activity.setCreateTime(cellValue);
                }else if(j==8){
                    activity.setCreateBy(cellValue);
                }else if(j==9){
                    activity.setEditTime(cellValue);
                }else if(j==10){
                    activity.setEditBy(cellValue);
                }
            }
            activityList.add(activity);
        }
        //调用批量添加的sql语句
        boolean b = activityService.importExcelByList(activityList);
        if(b){
            returnObject.setMessageCode(MessageUtil.SUCCESS_CODE);
            returnObject.setMessageStr(MessageUtil.SUCCESS_STR);
        }else{
            returnObject.setMessageCode(MessageUtil.FAIL_CODE);
            returnObject.setMessageStr(MessageUtil.FAIL_STR);
        }

        return returnObject;
    }
    //cell的不同格式 取值
    public String  getCellValueByType(HSSFCell cell){
        String result = "";
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                result = cell.getNumericCellValue()+"";
                break;
            case BOOLEAN: //Boolean
                result = cell.getBooleanCellValue()+"";
                break;
            case ERROR: //故障
                result = cell.getErrorCellValue()+"";
                break;
            case FORMULA: //公式
                result = cell.getCellFormula()+"";
                break;
            case BLANK: //空值
                result = "";
                break;
            default: //字符串
                result = cell.getStringCellValue();
        }
        return result;
    }

    //修改前先查询
    @RequestMapping("/selectActivity.action")
    @ResponseBody
    public Activity selectActivity(int id,HttpSession session){
        System.out.println("id = " + id);
        Activity activity = activityService.exportActivityAllById(id);
        session.setAttribute("activityId",activity.getActivityId());
        System.out.println("session = " + session);
        System.out.println("activity = " + activity);
        return activity;
    }
    @RequestMapping("/updateActivity.action")
    @ResponseBody
    public ReturnObject updateActivity(Activity activity,HttpSession session,ReturnObject returnObject){
        int activityId = (int)session.getAttribute("activityId");
        System.out.println("activityId = " + activityId);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = sdf.format(date);
        activity.setEditTime(nowTime);
        User user = (User)session.getAttribute("user");
        activity.setEditBy(user.getUserName());
        System.out.println("activity = " + activity);

//        if(){
//            returnObject.setMessageCode(MessageUtil.SUCCESS_CODE);
//            returnObject.setMessageStr(MessageUtil.SUCCESS_STR);
//        }else {
//            returnObject.setMessageCode(MessageUtil.FAIL_CODE);
//            returnObject.setMessageStr(MessageUtil.FAIL_STR);
//        }
        return returnObject;
    }
}
