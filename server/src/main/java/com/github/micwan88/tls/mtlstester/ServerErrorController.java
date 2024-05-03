package com.github.micwan88.tls.mtlstester;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServerErrorController implements ErrorController {
    @RequestMapping(value = "/error")
    public String errorPage() {
        return "error.html";
    }
}
