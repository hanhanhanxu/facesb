<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/11/26
  Time: 17:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>照片添加-人脸识别.hx</title>
</head>
<body>

<br>
    <h1 style="text-align: center">添加人脸</h1>

    <form method="post" action="${pageContext.request.contextPath}/faceadd2.action" enctype="multipart/form-data">
            <table align="center">
                <tr>
                    <td>姓名(user_info)</td>
                    <td><input type="text" name="userinfo"></td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td>人脸文件</td>
                    <td><input type="file" name="file" width="90px"></td>
                </tr>
                <tr>
                    <td>提交</td>
                    <td><input type="submit" value="提 交"></td>
                </tr>
            </table>
    </form>


</body>
</html>
