String.prototype.replaceAll = function(s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

$(function() {

    $('#btn_hidden_btns').click(function() {
        document.getElementById('WebVideoCap1').hiddenControllButtons();
        document.getElementById('WebVideoCap1').autofill(636, false);

    }), $('#btn_show_btns').click(function() {
        document.getElementById('WebVideoCap1').showControllButtons();
        document.getElementById('WebVideoCap1').autofill(636, true);
    }), $('#btn_start').click(function() {
        document.getElementById('WebVideoCap1').start();

    }), $('#btn_stop').click(function() {
        document.getElementById('WebVideoCap1').stop();

    }), $('#btn_cap_only').click(function() {
        document.getElementById('WebVideoCap1').cap();

    }), $('#btn_cap').click(function() {
        document.getElementById('WebVideoCap1').cap();
        document.getElementById('base64_jpeg').value = document
            .getElementById('WebVideoCap1').jpegBase64Data;
        document.getElementById('base64_bmp').value = document
            .getElementById('WebVideoCap1').bmpBase64Data;
        document.getElementById("picData").value = document
            .getElementById('WebVideoCap1').jpegBase64Data;

        ajax_post();
    }), $('#btn_submit_only').click(function() {
        document.getElementById('WebVideoCap1').cap();
        document.getElementById('base64_jpeg').value = document
            .getElementById('WebVideoCap1').jpegBase64Data;
        document.getElementById('base64_bmp').value = document
            .getElementById('WebVideoCap1').bmpBase64Data;
        document.getElementById("picData").value = document
            .getElementById('WebVideoCap1').jpegBase64Data;

        alert(document.getElementById("picData").value.length);
        document.forms[0].submit();

    }), $('#btn_getdata_only').click(function() {
        document.getElementById('base64_jpeg').value = document
            .getElementById('WebVideoCap1').jpegBase64Data;
        document.getElementById('base64_bmp').value = document
            .getElementById('WebVideoCap1').bmpBase64Data;
        document.getElementById("picData").value = document
            .getElementById('WebVideoCap1').jpegBase64Data;

        alert(document.getElementById("picData").value.length);
    }), $('#btn_clear').click(function() {
        document.getElementById('WebVideoCap1').clear();
    }), $('#btn_submit').click(function() {
        document.getElementById('WebVideoCap1').cap();
        document.getElementById('base64_jpeg').value = document
            .getElementById('WebVideoCap1').jpegBase64Data;
        document.getElementById('base64_bmp').value = document
            .getElementById('WebVideoCap1').bmpBase64Data;
        document.getElementById("picData").value = document
            .getElementById('WebVideoCap1').jpegBase64Data;

        alert(document.getElementById("picData").value.length);
        document.forms[0].submit();
    });

});

function ajax_post() {
    var base64_data = document.getElementById('WebVideoCap1').jpegBase64Data;
    $.ajax({
        url : 'http://localhost:8080/VideoCap/servlet/VideoCap4Ajax',
        type : 'POST',
        dataType : 'jason',
        data : {
            picData : "'" + base64_data + "'"
        },
        timeout : 1000,
        success : callbackfun
    });

}

function callbackfun(data) {
    var obj = eval('(' + data + ')');

    if ('ok' == obj.savestatus) {
        alert('照片采集成功!');
    }

}
