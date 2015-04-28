package ee.sk.hwcrypto.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping("")
    public String view() {
        return "view";
    }
}
