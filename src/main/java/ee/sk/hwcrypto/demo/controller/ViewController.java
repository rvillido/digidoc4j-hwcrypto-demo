package ee.sk.hwcrypto.demo.controller;

import ee.sk.hwcrypto.demo.model.FileWrapper;
import ee.sk.hwcrypto.demo.model.SigningSessionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewController {

    private static final Logger log = LoggerFactory.getLogger(ViewController.class);
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
            log.error("Error Writing file content to output stream", e);
        }
    }
}
