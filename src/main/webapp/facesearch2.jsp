<%--
  Created by IntelliJ IDEA.
  User: 11952
  Date: 2018/12/3
  Time: 9:22
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
    <table align="center">
        <tr>
            <td>

            </td>
            <td>
                <div>
                    <button id="sc"  width="50" height="50" >相似度检测</button>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div >
                    <video id="video1" width="320" height="320" autoplay></video>
                    <button id="picture1"  >拍照1</button>
                </div>
            </td>
            <td>
                <div>
                    <canvas id="canvas1" width="320" height="320"></canvas>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div >
                    <video id="video2" width="320" height="320" autoplay></video>
                    <button id="picture2"  >拍照2</button>
                </div>
            </td>
            <td>
                <div>
                    <canvas id="canvas2" width="320" height="320"></canvas>
                </div>
            </td>
        </tr>
    </table>


<script>
    navigator.getUserMedia = navigator.getUserMedia ||
        navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia;
    if (navigator.getUserMedia) {
        navigator.getUserMedia({ audio: true, video: { width: 320, height: 320 } },
            function(stream) {
                var video1 = document.getElementById("video1");
                video1.src = window.URL.createObjectURL(stream);
                video1.onloadedmetadata = function(e) {
                    console.log('nihao44eee4aaaaddda');
                    video1.play();
                };
            },
            function(err) {
                console.log("The following error occurred: " + err.name);
            }
        );
    } else {
        console.log("getUserMedia not supported");
    }
    var context = document.getElementById("canvas1").getContext("2d");
    document.getElementById("picture1").addEventListener("click", function () {
        context.drawImage(video1, 0, 0, 320, 320);
    });

    if (navigator.getUserMedia) {
        navigator.getUserMedia({ audio: true, video: { width: 320, height: 320 } },
            function(stream) {
                var video2 = document.getElementById("video2");
                video2.src = window.URL.createObjectURL(stream);
                video2.onloadedmetadata = function(e) {
                    console.log('nihao44eee4aaaaddda');
                    video2.play();
                };
            },
            function(err) {
                console.log("The following error occurred: " + err.name);
            }
        );
    } else {
        console.log("getUserMedia not supported");
    }
    var context2 = document.getElementById("canvas2").getContext("2d");
    document.getElementById("picture2").addEventListener("click", function () {
        context2.drawImage(video2, 0, 0, 320, 320);
    });



    document.getElementById("sc").addEventListener("click", function () {
        var imgData=document.getElementById("canvas1").toDataURL("image/jpg");
        var imgData2=document.getElementById("canvas2").toDataURL("image/jpg");
        var code=imgData.substr(22);
        var code2=imgData2.substr(22);
        $.post('${pageContext.request.contextPath}/facesearch2.action',{'sj':code,'sj2':code2},function(data){
            if(data=="error"){
                alert("人脸识别错误，请保证图片内有人脸且无遮挡。");
                window.location.href = "${pageContext.request.contextPath}/facesearch2.jsp";
            }else{
                alert("相似分数: "+data+" (80分以上我们认为是同一个人)");
                window.location.href = "${pageContext.request.contextPath}/facesearch2.jsp";
            }
        });
    });
</script>

</body>
</html>
