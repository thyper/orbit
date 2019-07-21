package com.mercadolibre.orbit.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class SwaggerController {

    @RequestMapping("")
    public String greeting() {
        return "redirect:/swagger-ui.html";
    }

}
