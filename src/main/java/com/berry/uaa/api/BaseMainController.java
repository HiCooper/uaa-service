package com.berry.uaa.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Berry_Cooper.
 * @date 2019/11/13 17:17
 * fileName：BaseMainController
 * Use：
 */
@Controller
public class BaseMainController {

    @GetMapping("/auth/login")
    public String loginPage(Model model) {
        model.addAttribute("loginUrl", "/auth/login");
        return "base-login";
    }
}
