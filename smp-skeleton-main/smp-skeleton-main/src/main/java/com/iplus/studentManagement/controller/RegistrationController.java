package com.iplus.studentManagement.controller;

import com.iplus.studentManagement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    // POST /register - Handles registration submission from signup.html
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String role, 
                               RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(username, password, role);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            // Catches the "Username already exists" exception from the service layer
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }
}