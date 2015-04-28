package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.SigningSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SigningController {

    @Autowired
    private SigningSessionData sessionData;

    @RequestMapping(value="/upload", method= RequestMethod.POST)
    public String handleUpload(@RequestParam MultipartFile file) {
        try {
            sessionData.setFile(file.getBytes());
            sessionData.setFileName(file.getOriginalFilename());
            sessionData.setFileContentType(file.getContentType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionData.getFileName();
    }

    @RequestMapping(value="/generateHash", method = RequestMethod.POST)
    public String generateHash(@RequestParam String certInHex) {
        sessionData.setCertInHex(certInHex);
        return "413140d54372f9baf481d4c54e2d5c7bcf28fd6087000280e07976121dd54af2";
    }

    @RequestMapping(value="/createContainer", method = RequestMethod.POST)
    public void createContainer(@RequestParam String signatureInHex) {
        sessionData.setSignatureInHex(signatureInHex);
    }

    @RequestMapping("/getContainer")
    public void getContainer(HttpServletResponse response) {
        //TODO: Replace with actual generated container... Just return the uploaded file for now
        response.setContentType(sessionData.getFileContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + sessionData.getFileName());
        try {
            response.getOutputStream().write(sessionData.getFile());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
