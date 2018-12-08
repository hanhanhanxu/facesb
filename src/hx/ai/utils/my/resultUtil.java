package hx.ai.utils.my;

import hx.ai.mapper.FacekuMapper;

import java.util.*;

//对result结果进行处理，提取有用信息。
public class resultUtil {
	
	public static Boolean error_msg(String result) {
		//error_msg":"
		//","
		//取得error_msg信息
		String res = "";
		String str =  result.substring(result.indexOf("error_msg\":\"")+12, result.indexOf("\",\""));
		if(str==null || "".equals(str)){
			res = "resultUtil->error_msg : error_msg为空。";
			System.err.println(res);
			return false;
		}else {
			if("SUCCESS".equals(str))
				return true;
			else {
				res = "resultUtil->error_msg : error_msg不为空也不为SUCCESS。";
				System.err.println(res);
				return false;
			}
		}
	}
	
	public static String error_msg2(String result) {
		//error_msg":"
		//","
		//取得error_msg信息
		String str =  result.substring(result.indexOf("error_msg\":\"")+12, result.indexOf("\",\""));
		if(str==null || "".equals(str)){
			System.err.println("resultUtil->error_msg : error_msg为空。");
			return "";
		}else {
			System.err.println(str);
			return str;
		}
	}

	public static String result_get(String result){
		String code = resultUtil.result_extract(result,"error_code\":",",\"");
		if(!code.equals("0")){//不成功的话
			String errstr = resultUtil.result_extract(result,"error_msg\":\"","\",\"");
			return "err_code:" + code + ":" + errstr;//返回错误信息
		}
		String socre;
		socre = resultUtil.result_extract(result,"score\":","}");
		return socre;
	}

	public static String result_login(String result){
		String code = resultUtil.result_extract(result,"error_code\":",",\"");
		if(!code.equals("0")){//不成功的话
			String errstr = resultUtil.result_extract(result,"error_msg\":\"","\",\"");
			return "err_code:" + code + ":" + errstr;//返回错误信息
		}
		String socre;
		socre = resultUtil.result_extract(result,"score\":","}");
		System.out.println("score:"+socre);
		if(Float.parseFloat(socre)>=80){
			String info = result.substring(result.indexOf("user_info")+12,result.indexOf("score")-3);
			System.out.println("info:"+info);
			if(info.startsWith("\\u"))
				return decode(info);
			return info;
		}else{
			return "err_code:"+"您的人脸不能登录";
		}
	}

	
	//切割result的信息，初步获得有用信息
	public static String result_extract(String result,String a,String b) {
		String str =  result.substring(result.indexOf(a)+a.length(), result.indexOf(b));
		return str;
	}
	
	public static String result_extract(String result,String a,String b,int ib) {
		String str =  result.substring(result.indexOf(a)+a.length(), result.indexOf(b)-ib);
		return str;
	}
	
	//从初步获得有用信息的字符串中获得有用信息
	public static List getEle(String str) {//如果str.isEmpty的话返回的list.isEmpty
		//"test1_group","test2_group","test_search_group"
		//"user1","user2","user3"
		//"face_token":"3558ca38903427eb617608108d1cf13d","ctime":"2018-11-21 21:20:11"
		
		List list = new ArrayList();//list必定!=null
		if(str==null || str.isEmpty())
			return list;
		
		String[] s = str.split(",");//按照逗号切割
		for(int i=0;i<s.length;i++) {
			String sh = s[i];
			list.add(sh.substring(1,sh.length()-1));//去掉首尾分号
		}
		
		/*Iterator<List> it = list.iterator();
		while(it.hasNext()) {//遍历输出
			System.out.println(it.next());
		}*/
		
		return list;
	}
	
	//第一步
	public static List trans(List<List<String>> list) {//每一个list集合中有一个小的list集合,这个第二重list集合中储存了tace_token和ctime这两个信息
		int n=0;
		List<String> lr = new ArrayList();
		if(list==null || list.isEmpty())
			return lr;
		for(List<String> l:list) {
			for(String s:l) {
				if(n%2==0) //把ctime舍弃，只要face_token
					lr.add(s);
				n++;
			}
		}
		return lr;
	}
	
	//第二步
	public static List getEle2(List<String> list) {
		List<String> l = new ArrayList();
		String be = "face_token\":\"";
		for(String s:list) {
			String str = s.substring(s.indexOf(be)+be.length());
			l.add(str);
		}
		return l;
	}

	public static Map<Integer, List<String>> getmatchuser(String result, FacekuMapper facekuMapper){

		String res = result.substring(result.indexOf("user_list\":["));
		System.out.println("人脸识别，有用信息(user_list)："+res);

		int n = countStr(res,"group_id");
		System.out.println("有" + n + "个匹配的结果");

		String[] score = new String[n];
		String[] info = new String[n];
		String[] gid = new String[n];
		String[] uid = new String[n];

		if(n==1){
			score[0] = res.substring(res.indexOf("score\":")+"score\":".length(),res.indexOf("}"));
			info[0] = res.substring(res.indexOf("user_info\":\"")+"user_info\":\"".length(),res.indexOf("score")-3);
			gid[0] = res.substring(res.indexOf("group_id")+11,res.indexOf("user_id")-3);
			uid[0] = res.substring(res.indexOf("user_id")+10,res.indexOf("user_info")-3);
		}
		if(n==2){
			String str1 = res.substring(0,res.indexOf("},")+2);
			System.out.println("str1:"+str1);

			String str2 = res.substring(res.indexOf(str1)+str1.length());
			System.out.println("str2:"+str2);

			score[0] = str1.substring(str1.indexOf("score\":")+"score\":".length(), str1.indexOf("}"));
			score[1] = str2.substring(str2.indexOf("score\":")+"score\":".length(), str2.indexOf("}"));

			info[0] = str1.substring(str1.indexOf("user_info\":\"")+"user_info\":\"".length(),str1.indexOf("score")-3);
			info[1] = str2.substring(str2.indexOf("user_info\":\"")+"user_info\":\"".length(),str2.indexOf("score")-3);

			gid[0] = str1.substring(str1.indexOf("group_id")+11,str1.indexOf("user_id")-3);
			gid[1] = str2.substring(str2.indexOf("group_id")+11,str2.indexOf("user_id")-3);

			uid[0] = str1.substring(str1.indexOf("user_id")+10,str1.indexOf("user_info")-3);
			uid[1] = str2.substring(str2.indexOf("user_id")+10,str2.indexOf("user_info")-3);
		}
		if(n==3){
			String str1 = res.substring(0,res.indexOf("},")+2);
			System.out.println("str1:"+str1);

			String res2 = res.substring(res.indexOf(str1)+str1.length());
			//System.out.println(res2);

			String str2 = res2.substring(0,res2.indexOf("},")+2);
			System.out.println("str2:"+str2);

			String str3 = res2.substring(res2.indexOf(str2)+str2.length());
			System.out.println("str3:"+str3);

			//--------------------------------------------------------------

			score[0] = str1.substring(str1.indexOf("score\":")+"score\":".length(), str1.indexOf("}"));
			score[1] = str2.substring(str2.indexOf("score\":")+"score\":".length(), str2.indexOf("}"));
			score[2] = str3.substring(str3.indexOf("score\":")+"score\":".length(), str3.indexOf("}"));


			info[0] = str1.substring(str1.indexOf("user_info\":\"")+"user_info\":\"".length(),str1.indexOf("score")-3);
			info[1] = str2.substring(str2.indexOf("user_info\":\"")+"user_info\":\"".length(),str2.indexOf("score")-3);
			info[2] = str3.substring(str3.indexOf("user_info\":\"")+"user_info\":\"".length(),str3.indexOf("score")-3);

			gid[0] = str1.substring(str1.indexOf("group_id")+11,str1.indexOf("user_id")-3);
			gid[1] = str2.substring(str2.indexOf("group_id")+11,str2.indexOf("user_id")-3);
			gid[2] = str3.substring(str3.indexOf("group_id")+11,str3.indexOf("user_id")-3);

			uid[0] = str1.substring(str1.indexOf("user_id")+10,str1.indexOf("user_info")-3);
			uid[1] = str2.substring(str2.indexOf("user_id")+10,str2.indexOf("user_info")-3);
			uid[2] = str3.substring(str3.indexOf("user_id")+10,str3.indexOf("user_info")-3);
		}

		Map<Integer,List<String>> map = new LinkedHashMap();
		for(int i=0;i<n;i++){
			List list = new ArrayList();
			String infos = "";
			String img = facekuMapper.selectBygiduid(gid[i],uid[i]);
			if(info[i].startsWith("\\u"))//是unicode编码原码
				infos = decode(info[i]);//map.put(decode(info[i]),score[i].substring(0,4));
			else//map.put(info[i],score[i].substring(0,4));
				infos = info[i];
			list.add(infos);
			list.add(score[i].substring(0,4));
			list.add(img);
			map.put(i,list);
		}
		return map;
	}


	public static String result_extractaj(String result,String a,String b,int ia,int ib){
		return result.substring(result.indexOf(a)+ia,result.indexOf(b,result.indexOf(a))-ib);
	}

	public static Map<String,String> getdetectinfo(String result){
		Map<String,String> map = new LinkedHashMap();
		map.put("年龄",resultUtil.result_extract(result,"age\":","beauty",2));
		map.put("颜值",resultUtil.result_extract(result,"beauty\":","face_shape",2));
		String shape = resultUtil.result_extractaj(result,"face_shape","probability",21,3);
		String gender =  resultUtil.result_extractaj(result,"gender","probability",17,3);
		String race = resultUtil.result_extractaj(result,"race","probability",15,3);
		String emotion =  resultUtil.result_extractaj(result,"emotion","probability",18,3);

		System.out.println(shape+","+gender+","+race+","+emotion);

		switch(shape){
			case "square": shape = "正方形";break;
			case "triangle": shape = "三角形";break;
			case "oval": shape = "椭圆";break;
			case "heart": shape = "心型";break;
			case "round": shape = "圆型";break;
		}

		switch(gender){
			case "male": gender = "男性";break;
			case "female": gender = "女性";break;
		}

		switch(race){
			case "yellow": race = "黄种人";break;
			case "white": race = "白种人";break;
			case "black": race = "黑种人";break;
			case "arabs": race = "阿拉伯人";break;
		}

		switch(emotion){
			case "angry": emotion = "愤怒";break;
			case "disgust": emotion = "厌恶";break;
			case "fear": emotion = "恐惧";break;
			case "happy": emotion = "高兴";break;
			case "sad": emotion = "伤心";break;
			case "surprise": emotion = "惊讶";break;
			case "neutral": emotion = "无情绪";break;
		}
		map.put("脸型",shape);
		map.put("性别",gender);
		map.put("人种",race);
		map.put("情绪",emotion);
		return map;
	}

	/*
	 * unicode编码转中文
	 */
	public static String decode(String str) {
		String sg = "\\u";
		int a = 0;
		List<String> list = new ArrayList();
		while (str.contains(sg)) {
			str = str.substring(2);
			String substring;
			if (str.contains(sg)) {
				substring = str.substring(0, str.indexOf(sg));
			} else {
				substring = str;
			}
			if (str.contains(sg)) {
				str = str.substring(str.indexOf(sg));
			}
			list.add(substring);
		}
		StringBuffer sb = new StringBuffer();
		if (!list.isEmpty()){
			for (String string : list) {
				sb.append((char) Integer.parseInt(string, 16));
			}
		}
		return sb.toString();
	}

	public static int countStr(String str,String sToFind) {
		int num = 0;
		while (str.contains(sToFind)) {
			str = str.substring(str.indexOf(sToFind) + sToFind.length());
			num ++;
		}
		return num;
	}
	
	public static int getcount(String s) {
        int num = 0;// 出现次数
        for (int i=0;i<s.length();i++)// 循环遍历每个字符，判断是否是字符 , ，如果是，累加次数
        {
            if (s.charAt(i)==',') {// 获取每个字符，判断是否是字符,
                num++; 
            }
        }
        System.out.println("字符a出现的次数：" + num);
        return num;
    }
	
	
}
