package hx.ai.controller;


import hx.ai.service.FaceService;
import hx.ai.utils.my.imageUtil;
import hx.ai.utils.my.resultUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
public class FaceController {

    @Autowired
    private FaceService faceService;

    private String token = "24.4694ca2478310a94d66e07ee180ef70b.2592000.1545398855.282335-14894542";



    @ResponseBody//把return的数据以json串的格式返回赋值到data里面
    @RequestMapping("/login")//登录
    public String login(HttpSession session,String sj) {
        System.out.println("**************************人脸登录开始:************************");
        String res = faceService.facelogin(token,sj);
        System.out.println("**************************人脸登录结束:************************");
        if(res.startsWith("err_code")){//返回的是错误码，或者人脸分数低于80不能登录
            return "/login.jsp";
        }else{//返回的是info
            session.setAttribute("admin",res);
            System.out.println("姓名:"+res);
            return res;
        }
    }


    @ResponseBody
    @RequestMapping("/regist")
    public String regist(HttpSession session,HttpServletRequest request,String sj,String userinfo){
        System.out.println("userinfo:"+userinfo);
        if(userinfo==null || userinfo.length()==0){
            return "userinfo can not be empty.";
        }
        String res = faceService.faaceregist(token,sj,userinfo);
        if(res!=null && res.length()!=0){
            if("SUCCESS".equals(res)){
                System.out.println("注册成功！");
                return "SUCCESS";
            }else{
                System.out.println("注册失败！");
                return res;
            }
        }
        request.setAttribute("message", "返回消息res为空");
        System.out.println("返回消息res为空");
        return "/message.jsp";
    }


    @ResponseBody
    @RequestMapping("/faceadd1")//添加人脸-扫描人脸添加
    public String faceadd1(HttpServletRequest request,String sj,String userinfo){
        //String s = request.getServletContext().getRealPath("/img");
        //System.out.println(s);
        System.out.println("userinfo:"+userinfo);
        if(userinfo==null || userinfo.length()==0){
            return "userinfo can not be empty.";
        }
        String filepath = request.getServletContext().getRealPath("/img");
        //String filepath = "D:\\IntelliJ IDEA 2017.3.1\\webpro\\facesb\\src\\main\\webapp\\img";
        String filename = userinfo+UUID.randomUUID().toString().substring(0,20) + ".jpg";
        String res = faceService.faaceadd(token,sj,userinfo,filepath,filename);//人脸库添加  服务器文件添加 数据库添加
        if(res!=null && res.length()!=0){
            if("SUCCESS".equals(res)){
                System.out.println("添加成功！");
                return "SUCCESS";
            }else{
                System.out.println("添加失败！");
                return res;
            }
        }
        request.setAttribute("message", "返回消息res为空");
        System.out.println("返回消息res为空");
        return "/message.jsp";
    }

    @RequestMapping("/faceadd2")//添加人脸-本地照片文件添加
    public String UploadFile(HttpServletRequest request) {

        ServletContext sc = request.getSession().getServletContext();
        String tempPath = sc.getRealPath("/WEB-INF/ExamTemp"); //上传时生成的临时文件保存目录
        File tmpFile = new File(tempPath);
        if (!tmpFile.exists()) {
            tmpFile.mkdir(); //创建临时目录
        }
        //先确定文件的大致保存路径，下面再根据此文件是第几题设置此文件的确切保存路径
        String savePath = request.getServletContext().getRealPath("/img");
        //String savePath = "D:\\IntelliJ IDEA 2017.3.1\\webpro\\facesb\\src\\main\\webapp\\img";
        //String savePath =  sc.getRealPath("/WEB-INF/faceadd");
        String message = "";//消息提示
        String userinfo = "";
        String fileName = "";
        String newFileName = "";//新文件名

        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024*1024*2);//设置缓冲区的大小为2m，如果不指定，那么缓冲区的大小默认是10KB
            factory.setRepository(tmpFile);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(1024*1024*3);//3M
            upload.setSizeMax(1024*1024*3);//3M
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);

            for(FileItem item : list){
                if(item.isFormField()){//是普通输入项
                    userinfo = item.getString("UTF-8");//解决普通输入项的数据的中文乱码问题
                }
                //if(count>=2){
                    if(!item.isFormField()){//不是普通输入项
                        fileName = item.getName();//得到上传的文件名称，
                        if(fileName==null || fileName.trim().equals("")){
                            //上传文件为空
                            //to do..
                            continue;
                        }
                        //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                        //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                        fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                        System.out.println("第一次处理的文件名："+fileName);
                        //得到上传文件的扩展名   限制扩展类型
                        String fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
                        System.out.println("第二次处理的文件名："+fileExtName);
                        fileName = fileName.substring(0,fileName.indexOf("."));
                        newFileName = fileName + "@" + UUID.randomUUID().toString().substring(0,20) + "." + fileExtName;

                        File f = new File(savePath);
                        if (!f.exists())
                            f.mkdirs();

                        InputStream in = item.getInputStream();
                        FileOutputStream out = new FileOutputStream(savePath + "\\" + newFileName);

                        byte buffer[] = new byte[1024];
                        int len = 0;
                        while((len=in.read(buffer))>0){
                            out.write(buffer, 0, len);
                        }
                        in.close();
                        out.close();
                        //删除处理文件上传时生成的临时文件
                        item.delete();
                    }
                //}
            }
        }catch (FileUploadBase.FileSizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "单个文件超过3M！！！");
            return "/message.jsp";
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "上传文件的总的大小超出限制的最大值！！！");
            return "/message.jsp";
        }catch (Exception e) {
            e.printStackTrace();
            return "/error.jsp";
        }


        //-----------------------------------------------------------

        String filepath = savePath + "\\" + newFileName;
        System.out.println("保存的路径为:" + filepath);
        String imagecode = imageUtil.getImageBinary(filepath);//获取人脸照片经base64编码后的字符串

        String res = faceService.faaceadd(token,imagecode,userinfo,savePath,newFileName);
        if(res!=null && res.length()!=0){
            if("SUCCESS".equals(res)){
                System.out.println("添加成功！");
                request.setAttribute("message","添加成功！");
                return "/message.jsp";
            }else{
                System.out.println("添加失败！");
                request.setAttribute("message","添加失败:" + res);
                return "/message.jsp";
            }
        }
        request.setAttribute("message", "返回消息res为空");
        return "/message.jsp";
    }


    @ResponseBody
    @RequestMapping("/facesearch")//人脸相似度识别
    public String facesearch(HttpSession session, String sj){
        String res = faceService.facesearch(token,sj);//只有从人脸库中找到的人脸才会从数据库中查询。数据库中只存有添加成功的图片信息
        session.setAttribute("yours",sj);
        if(res.startsWith("error")){//返回的是错误信息（由于面部遮挡等于原因导致人脸检测失败，或是:error_match user is not found没有找到匹配用户）
            return res;
        }else if(res.startsWith("NotUser")){
            return "NotUser";
        }else{//人脸检测正确且找到匹配的用户
            //再进入一次service
            Map<Integer,List<String>> map = faceService.facesearchmap(res);
            session.setAttribute("map",map);
            //Iterator iterator = map.entrySet();
            System.out.println("遍历map集合");
            for (List<String> l : map.values()){
                for(String s:l){
                    System.out.println(s);
                }
                System.out.println("************");
            }
            return "SUCCESS";
        }
    }


    @ResponseBody
    @RequestMapping("/facesearch2")//人脸相似度识别
    public String facesearch2(HttpSession session, String sj,String sj2){
        String res = faceService.facesearch2(token,sj,sj2);
        if(res.startsWith("error")){//错误，原因之一是在其中一个或多个图片中没有找到人脸
            return "error";
        }else{//识别成功，返回结果相似得分
            //String score = "Face similarity score:"+res.substring(res.indexOf("score")+7,res.indexOf("face_list")-2).substring(0,4);
            String score = res.substring(res.indexOf("score")+7,res.indexOf("face_list")-2).substring(0,4);
            return score;
        }
    }


    @ResponseBody
    @RequestMapping("/facedetect")//人脸信息刷脸检测
    public String facedetect(HttpSession session,String sj){//sj是人脸的bast64编码
        String res = faceService.facedetect(token,sj);
        if(res.startsWith("error_code")){
            return res;
        }else{
            Map map = resultUtil.getdetectinfo(res);
            session.setAttribute("map",map);
            session.setAttribute("yours",sj);
            return "SUCCESS";
        }
    }


    @RequestMapping("/facedetect2")//人脸信息照片检测
    public String facedetect2(HttpSession session,HttpServletRequest request){
        ServletContext sc = request.getSession().getServletContext();
        String tempPath = sc.getRealPath("/WEB-INF/ExamTemp"); //上传时生成的临时文件保存目录
        File tmpFile = new File(tempPath);
        if (!tmpFile.exists()) {
            tmpFile.mkdir(); //创建临时目录
        }
        //先确定文件的大致保存路径，下面再根据此文件是第几题设置此文件的确切保存路径
        String savePath =  sc.getRealPath("/WEB-INF/facedetect");
        String message = "";//消息提示
        String userinfo = "";
        String fileName = "";
        String newFileName = "";//新文件名

        try{
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(1024*1024*2);//设置缓冲区的大小为2m，如果不指定，那么缓冲区的大小默认是10KB
            factory.setRepository(tmpFile);
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(1024*1024*3);//3M
            upload.setSizeMax(1024*1024*3);//3M
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);

            for(FileItem item : list){
                if(!item.isFormField()){//不是普通输入项
                    fileName = item.getName();//得到上传的文件名称，
                    if(fileName==null || fileName.trim().equals("")){
                        //上传文件为空
                        //to do..
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
                    System.out.println("第一次处理的文件名："+fileName);
                    //得到上传文件的扩展名   限制扩展类型
                    String fileExtName = fileName.substring(fileName.lastIndexOf(".")+1);
                    System.out.println("第二次处理的文件名："+fileExtName);
                    fileName = fileName.substring(0,fileName.indexOf("."));
                    newFileName = fileName + "@" + UUID.randomUUID().toString().substring(0,10) + "." + fileExtName;

                    File f = new File(savePath);
                    if (!f.exists())
                        f.mkdirs();

                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(savePath + "\\" + newFileName);

                    byte buffer[] = new byte[1024];
                    int len = 0;
                    while((len=in.read(buffer))>0){
                        out.write(buffer, 0, len);
                    }
                    in.close();
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                }
            }
        }catch (FileUploadBase.FileSizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "单个文件超过3M！！！");
            return "/message.jsp";
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "上传文件的总的大小超出限制的最大值！！！");
            return "/message.jsp";
        }catch (Exception e) {
            e.printStackTrace();
            return "/error.jsp";
        }

        //-----------------------------------------------------------

        String filepath = savePath + "\\" + newFileName;
        System.out.println("保存的路径为:" + filepath);
        request.setAttribute("filepath",filepath);
        String imagecode = imageUtil.getImageBinary(filepath);//获取人脸照片经base64编码后的字符串

        String res = faceService.facedetect(token,imagecode);
        if(res.startsWith("error_code")){
            request.setAttribute("message",res+" 此图片检测不成功，请重新选择人脸照片。");
            return "/message.jsp";
        }else{
            Map map = resultUtil.getdetectinfo(res);
            session.setAttribute("map",map);
            session.setAttribute("yours",imagecode);
            return "/faceinfo.jsp";
        }

    }


    public static boolean GenerateImage(String imgStr) { // 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try { // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            } // 生成jpeg图片
            String imgFilePath = "D:/test22.jpg";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
