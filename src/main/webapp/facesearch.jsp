<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/11/29
  Time: 9:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>人脸识别-人脸识别.hx</title>
    <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
</head>
<body>
<div style="text-align: center">
    <br>
    <h1>请扫描您的人脸</h1>
    <br><br>
    ${message}
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
        var data=imgData.substr(22);
        $.post('${pageContext.request.contextPath}/facesearch.action',{'sj':data},function(data){
            if(data=="SUCCESS") {
                alert("人脸识别成功！请点击查看具体信息。");
                window.location.href = "${pageContext.request.contextPath}/facemap.jsp";
            }else if(data=="NotUser"){
                alert("人脸识别结果:人脸库中暂无此人脸信息。您可以选择重新识别。");
                window.location.href = "${pageContext.request.contextPath}/facesearch.jsp";
            }else{
                alert("异常错误！请重新检测人脸。错误信息:" + data);
                window.location.href = "${pageContext.request.contextPath}/facesearch.jsp";
            }
        });
    });
</script>

</body>
</html>
