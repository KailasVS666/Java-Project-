package com.iplus.studentManagement.controller;

import com.iplus.studentManagement.entity.Student;
import com.iplus.studentManagement.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // Dependency Injection (injects the StudentService)
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // GET /students - List all students (Maps to studetns.html)
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "studetns"; 
    }

    // GET /students/new - Show new student form (Maps to create_student.html)
    @GetMapping("/new")
    public String createStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student";
    }

    // POST /students - Save new student
    @PostMapping
    public String saveStudent(@ModelAttribute("student") Student student, RedirectAttributes redirectAttributes) {
        try {
            studentService.saveStudent(student);
            redirectAttributes.addFlashAttribute("success", "Student added successfully!");
            return "redirect:/students";
        } catch (IllegalStateException e) {
            // Catches the unique email violation from the service layer
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/new"; 
        }
    }

    // GET /students/edit/{id} - Show edit form (Maps to edit_student.html)
    @GetMapping("/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        return "edit_student";
    }

    // POST /students/{id} - Update existing student
    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") Student studentDetails, RedirectAttributes redirectAttributes) {
        Student existingStudent = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

        // Manually update fields before passing to service
        existingStudent.setFirstName(studentDetails.getFirstName());
        existingStudent.setLastName(studentDetails.getLastName());
        existingStudent.setEmail(studentDetails.getEmail());

        try {
            studentService.saveStudent(existingStudent); // Save handles the update and validation
            redirectAttributes.addFlashAttribute("success", "Student updated successfully!");
            return "redirect:/students";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/students/edit/" + id;
        }
    }

    // GET /students/delete/{id} - Delete a student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteStudent(id);
        redirectAttributes.addFlashAttribute("success", "Student deleted successfully!");
        return "redirect:/students";
    }
}