package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewController {

    @Autowired
    private SigningSessionData session;

    @RequestMapping("")
    public String view() {
        return "view";
    }

    @RequestMapping("/downloadContainer")
    public void downloadContainer(HttpServletResponse response) {
        FileWrapper file = session.getSignedFile();
        response.setContentType(file.getMimeType());
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getFileName());
        try {
            response.getOutputStream().write(file.getBytes());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
