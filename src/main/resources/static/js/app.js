$(document).on('change', '.btn-file :file', function() {
    var input = $(this),
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    disableSign();
    hideDownloadSection();
    $("#fileName").val(label);
    $('#fileUpload').submit();
});

$(document).on("click", "#signButton", function(event) {
    event.preventDefault();
    console.log("sign button pressed");
    sign();
});

$(document).ready(function() {
    $('#fileUpload').submit(function(e) {
        if($('#file').val()) {
            e.preventDefault();
            var progressBar = $("#progress-bar");
            $(this).ajaxSubmit({
                beforeSubmit: function() {
                    progressBar.width('0%');
                    progressBar.removeClass("progress-bar-success");
                },
                uploadProgress: function (event, position, total, percentComplete){
                    console.log("percent complete: "+ percentComplete);
                    progressBar.width(percentComplete + '%');
                    progressBar.html('<span>' + percentComplete +' %</span>');
                },
                success: function (){
                    console.log("successfully uploaded file");
                    progressBar.addClass("progress-bar-success");
                    enableSign();
                },
                error: function(xhr, textStatus, error){
                    console.log("error uploading file: " + error)
                },
                resetForm: false
            });
            return false;
        }
    });
});

enableSign = function() {
    $("#signButton").addClass("btn-success").prop('disabled', false);;
};

disableSign = function() {
    $("#signButton").removeClass("btn-success").prop('disabled', true);;
};

showDownloadSection = function() {
    var downloadSection = $("#downloadSection");
    if(downloadSection.hasClass("hidden")) {
        downloadSection.toggleClass("show hidden");
    }
};

hideDownloadSection = function() {
    var downloadSection = $("#downloadSection");
    if(downloadSection.hasClass("show")) {
        downloadSection.toggleClass("show hidden");
    }
};

post = function(url, data) {
    return new Promise(function(resolve, reject) {
        $.ajax({
            dataType: "json",
            url: url,
            type: "POST",
            data: data
        }).done(function(data) {
            if(data.result != "ok") {
                reject(Error(data.result))
            } else {
                resolve(data);
            }
        }).fail(function() {
            reject(Error("Post operation failed"));
        });
    });
};

fetchHash = function(certInHex) {
    return post("generateHash", {certInHex:certInHex})
};

createContainer = function(signatureInHex) {
    return post("createContainer", {signatureInHex:signatureInHex});
};

sign = function() {
    var cert;
    window.hwcrypto.getCertificate({lang: 'en'}).then(function(certificate) {
        cert = certificate;
        return fetchHash(certificate.hex);
    }).then(function(digest) {
        return window.hwcrypto.sign(cert, {type: 'SHA-256', hex: digest.hex}, {lang: 'en'});
    }).then(function(signature) {
        return createContainer(signature.hex);
    }).then(function(result) {
        showDownloadSection();
        console.log("container is ready for download");
    });
};

