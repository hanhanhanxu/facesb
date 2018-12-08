package hx.ai.service;

import java.util.List;
import java.util.Map;

public interface FaceService {
    public List getgid(String token);
    public List getuid(String token,String gid);
    public List getface_token(String token,String gid,String uid);
    public String facelogin(String token,String imagecode);//人脸登录
    public String faaceregist(String token, String imagecode,String userinfo);
    public String facesearch(String token,String imagecode);//人脸识别 默认，使用imagecode查找
    public String facesearch2(String token,String imagecode,String sj2);
    public String faaceadd(String token, String imagecode,String userinfo,String filepath,String filename);
    public String facedetect(String token,String imagecode);//人脸检测
    public Map facesearchmap(String result);//人脸相似度识别成功后处理result得到map用于页面展示。
}
