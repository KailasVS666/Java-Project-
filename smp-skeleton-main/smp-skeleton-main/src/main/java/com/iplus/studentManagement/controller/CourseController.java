package com.iplus.studentManagement.controller;

import com.iplus.studentManagement.entity.Course;
import com.iplus.studentManagement.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // GET /courses - List all courses (Maps to courses.html)
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses";
    }

    // GET /courses/new - Show new course form (Maps to create_course.html)
    @GetMapping("/new")
    public String createCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    // POST /courses - Save new course
    @PostMapping
    public String saveCourse(@ModelAttribute("course") Course course, RedirectAttributes redirectAttributes) {
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("success", "Course added successfully!");
        return "redirect:/courses";
    }

    // GET /courses/edit/{id} - Show edit form (Maps to edit_course.html)
    @GetMapping("/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        return "edit_course";
    }

    // POST /courses/{id} - Update existing course
    @PostMapping("/{id}")
    public String updateCourse(@PathVariable Long id, @ModelAttribute("course") Course courseDetails, RedirectAttributes redirectAttributes) {
        Course existingCourse = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));

        existingCourse.setCourseName(courseDetails.getCourseName());
        existingCourse.setDescription(courseDetails.getDescription());

        courseService.saveCourse(existingCourse);
        redirectAttributes.addFlashAttribute("success", "Course updated successfully!");
        return "redirect:/courses";
    }

    // GET /courses/delete/{id} - Delete a course
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("success", "Course deleted successfully!");
        return "redirect:/courses";
    }
}