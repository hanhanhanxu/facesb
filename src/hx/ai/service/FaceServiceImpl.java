package hx.ai.service;

import hx.ai.dao.FaceDao;
import hx.ai.dao.GroupDao;
import hx.ai.mapper.FacekuMapper;
import hx.ai.pojo.Faceku;
import hx.ai.utils.my.imageUtil;
import hx.ai.utils.my.resultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FaceServiceImpl implements FaceService {

    @Autowired
    private FacekuMapper facekuMapper;

    private FaceDao faceDao = new FaceDao();
    private GroupDao groupDao = new GroupDao();

    //得到所有gid
    @Override
    public List getgid(String token) {
        //System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>begin:FaceServiceImpl.getgid()");
        String result1 = groupDao.GroupGetlist(token);
        List list = null;
        if(resultUtil.error_msg(result1)==true) {
            String res = resultUtil.result_extract(result1, "group_id_list\":[", "]}");
            //System.out.println(res);//"test1_group","test2_group","test_search_group"
            list = resultUtil.getEle(res);
        }
        //System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>end:FaceServiceImpl.getgid()");
        return list;
    }

    //得到某个gid的所有uid
    @Override
    public List getuid(String token,String gid) {//
        String result2 = groupDao.getUsers(token,gid);
        List list = null;
        if(resultUtil.error_msg(result2)==true) {
            String res = resultUtil.result_extract(result2, "user_id_list\":[", "]}");
            //System.out.println(res);//"user1","user2","user3"
            list = resultUtil.getEle(res);
        }
        return list;
    }

    //通过gid和uid得到一个face_token
    @Override
    public List getface_token(String token,String gid,String uid) {//
        //一个人创建一个user，上传一张照片，所以通过gid uid两个条件搜索出来的face_token值只有一个
        String result3 = faceDao.getList(token,gid,uid);
        List list = null;
        if(resultUtil.error_msg(result3)==true) {
            String res = resultUtil.result_extract(result3, "face_list\":[{", "}]}");
            //System.out.println("getface_token的输出：" + res);//"face_token":"3558ca38903427eb617608108d1cf13d","ctime":"2018-11-21 21:20:11"
            list = resultUtil.getEle(res);
        }
        return list;
    }

    @Override//人脸添加
    public String faaceadd(String token, String imagecode,String userinfo,String failepath,String filename) {
        String uid = "user"+ UUID.randomUUID().toString().substring(0,7);
        String gid = "user_group";
        //向人脸库中添加
        String result = faceDao.add(token,gid,uid,imagecode,userinfo);//都存放在user_group用户组，uid随机生成
        if("SUCCESS".equals(resultUtil.error_msg2(result))){
            //向img路径添加
            imageUtil.generateImage(imagecode,failepath,filename);
            //向数据库中添加
            facekuMapper.insert(new Faceku(UUID.randomUUID().toString(),userinfo,filename,gid,uid));
        }
        return resultUtil.error_msg2(result);
    }

    @Override//人脸登录
    public String facelogin(String token, String imagecode) {//人脸登录
        String gidstr = "login_group";
        System.out.println("facelogin开始");
        String result = faceDao.search2(token, imagecode, gidstr);//223125人脸遮挡程度过高
        System.out.println("facelogin结束");
        return resultUtil.result_login(result);
    }

    @Override//人脸注册
    public String faaceregist(String token, String imagecode, String userinfo) {
        String uid = "login"+ UUID.randomUUID().toString().substring(0,7);
        String result = faceDao.add(token,"login_group",uid,imagecode,userinfo);//都存放在login_group用户组，uid随机生成
        return resultUtil.error_msg2(result);
    }

    @Override//人脸相似度识别
    public String facesearch(String token,String imagecode) {//人脸识别-扫描
        List<String> glist = getgid(token);//获取所有gid

        glist.remove("login_group");//不要管理员的那个组
        String gidstr = "";
        for(String s:glist) {
            gidstr = gidstr + s + ",";
        }
        gidstr = gidstr.substring(0,gidstr.length()-1);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result = faceDao.search2(token, imagecode, gidstr);//223125人脸遮挡程度过高
        String resu =resultUtil.result_extract(result,"error_msg\":\"","\",\"");//取得error_msg信息
        if(resu==null || resu.length()==0){
            return "error_abnormal";//异常错误
        }
        if(resu.equals("SUCCESS")){//人脸检测成功
            return result;
        }else if("match user is not found".equals(resu)){
            return "NotUser_" + resu;
        } else{//人脸检测失败（由于遮挡等原因，或者由于没有找到匹配的人脸），返回错误信息
            //match user is not found 222207
            return "error_" + resu;
        }
    }

    @Override
    public String facesearch2(String token,String imagecode, String imagecode2) {
        String result = faceDao.match(token, imagecode, imagecode2);//223125人脸遮挡程度过高
        //System.out.println("service,两张人脸对比:"+result);
        String resu =resultUtil.result_extract(result,"error_msg\":\"","\",\"");
        if(resu.equals("SUCCESS")){//人脸检测成功
            return result;
        }else{
            return "error_" + resu;
        }
    }

    @Override//人脸信息检测
    public String facedetect(String token, String imagecode) {
        String result = faceDao.detect(token,imagecode);
        System.out.println("人脸检测:"+result);
        if(resultUtil.error_msg(result)!=true){
            String error_code = result.substring(result.indexOf("error_msg")+12,result.indexOf("log_id")-3);
            System.out.println("error_code:"+error_code);
            return "error_code:"+error_code;
        }else{
            return result;
        }
    }

    @Override
    public Map facesearchmap(String result) {
        return resultUtil.getmatchuser(result,facekuMapper);
    }


}
