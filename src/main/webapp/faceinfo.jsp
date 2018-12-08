<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/12/2
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>检测结果-人脸识别.hx</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<%--

    <table align="center" border="2px">
        <c:forEach items="${map}" var="m">
            <tr>
                <td>${m.key}</td>
                <td>${m.value}</td>
            </tr>
        </c:forEach>
    </table>
--%>
<h2 style="text-align: center">人脸检测结果</h2>
<br>
您的人脸:
<img src="data:image/jpg;base64,${yours}" width="150px" height="200px"/>
<br>
<h3>您的人脸特征如下:</h3>

    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <table class="table">
                    <tbody>
                        <c:forEach items="${map}" var="m">
                            <tr class="info">
                                <td>${m.key}</td>
                                <td>${m.value}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
