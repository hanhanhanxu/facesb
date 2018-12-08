<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/12/3
  Time: 9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>照片检测-人脸识别.hx</title>
</head>
<body>

<form method="post" action="${pageContext.request.contextPath}/facedetect2.action" enctype="multipart/form-data">
    <table align="center">
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
