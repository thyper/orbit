package com.mercadolibre.orbit.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;


@Controller
@RequestMapping("/")
public class SwaggerController {

    @RequestMapping("")
    @ApiIgnore
    public String greeting() {
        return "redirect:/swagger-ui.html";
    }

}
