<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/11/29
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>识别结果-人脸识别.hx</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<h2 style="text-align: center">人脸识别结果</h2>
<br>
<%--实例人脸:<img src="${pageContext.request.contextPath}img/han1978578c-b249-4c08-9.jpg">--%>
您的人脸:
<img src="data:image/jpg;base64,${yours}"/>
<br>
<h3>与您的人脸相似的人脸有:</h3>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>
                            人脸信息
                        </th>
                        <th>
                            相似得分
                        </th>
                        <th>
                            人脸
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="m" items="${map}">
                            <tr class="info">
                                <td>${m.value[0]}</td>
                                <td>${m.value[1]}</td>
                                <td><img src="${pageContext.request.contextPath}/img/${m.value[2]}" width="150px" height="150px"></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</body>
</html>
