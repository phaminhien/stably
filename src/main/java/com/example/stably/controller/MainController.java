package com.example.stably.controller;

import com.example.stably.dto.FormRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class MainController {

    @GetMapping
    public String index(Model model) {
        FormRequest formRequest = new FormRequest();
        formRequest.setInterval(5);
        formRequest.setDuration(7);
        model.addAttribute("formRequest", formRequest);
        model.addAttribute("list", Collections.emptyList());
        return "index";
    }

    @PostMapping
    public String submit(@ModelAttribute FormRequest formRequest, Model model) {
        return "index";
    }


}
