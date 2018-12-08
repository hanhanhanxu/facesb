<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/11/28
  Time: 20:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>刷脸添加-人脸识别.hx</title>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>

    <br>
    <h1 style="text-align: center">添加人脸</h1>

    <div style="text-align: center">
        <br>
        <h2>请扫描您的人脸</h2>
        <br><br>
        ${message}
        <div>
            姓名(userinfo)*<input id="userinfo" type="text" name="userinfo"><br><br><br>
        </div>
        <div >
            <video id="video" width="320" height="320" autoplay></video>
            <button id="picture"  >拍照</button>
        </div>
        <div>
            <canvas id="canvas" width="320" height="320"></canvas>
            <button id="sc"  width="50" height="50" >上传</button>
        </div>
    </div>





    <script>
        navigator.getUserMedia = navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia;
        if (navigator.getUserMedia) {
            navigator.getUserMedia({ audio: true, video: { width: 320, height: 320 } },
                function(stream) {
                    var video = document.getElementById("video");
                    video.src = window.URL.createObjectURL(stream);
                    video.onloadedmetadata = function(e) {
                        console.log('nihao44eee4aaaaddda');
                        video.play();
                    };
                },
                function(err) {
                    console.log("The following error occurred: " + err.name);
                }
            );
        } else {
            console.log("getUserMedia not supported");
        }
        var context = document.getElementById("canvas").getContext("2d");
        document.getElementById("picture").addEventListener("click", function () {
            context.drawImage(video, 0, 0, 320, 320);
        });
        document.getElementById("sc").addEventListener("click", function () {
            var imgData=document.getElementById("canvas").toDataURL("image/jpg");
            var userinfo = document.getElementById("userinfo").value;
            var data=imgData.substr(22);
            $.post('${pageContext.request.contextPath}/faceadd1.action',{'sj':data,'userinfo':userinfo},function(data){
                if(data=="SUCCESS"){
                    alert("添加成功");
                    window.location.href = "${pageContext.request.contextPath}/menu.jsp";
                } else{
                    alert("添加人脸失败！请重试。错误原因:" + data)
                    window.location.href = "${pageContext.request.contextPath}/faceadd1.jsp";
                }
            });
        });
    </script>
</body>
</html>