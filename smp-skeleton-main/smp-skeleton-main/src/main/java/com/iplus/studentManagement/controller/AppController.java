package com.iplus.studentManagement.controller;

import com.iplus.studentManagement.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    private final StudentService studentService;

    // Inject StudentService to fetch the list of students for the grading page
    public AppController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET / - Home page (Maps to index.html)
    @GetMapping("/")
    public String viewHomePage() {
        return "index";
    }

    // GET /login - Login page (Maps to login.html)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // GET /signup - Signup page (Maps to signup.html)
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }
    
    // GET /grades - Grading page (Maps to grades.html)
    @GetMapping("/grades")
    public String showGradesPage(Model model) {
        // Fetch all students to populate the "Select Student" dropdown on the grading page
        model.addAttribute("students", studentService.getAllStudents());
        return "grades";
    }
}