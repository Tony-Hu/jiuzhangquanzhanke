package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.dto.CourseDto;
import com.mycompany.myapp.service.CourseService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
@Api(value="Course Service Controller", description = "Controller for find couses information")
public class CourseController {
    private final Logger log = LoggerFactory.getLogger(CourseController.class);
    @Autowired
    private CourseService courseService;

    @GetMapping(path = "/api/course/findAllCourses", produces = "application/json")
    public HttpEntity<List<CourseDto>> findAllCourses(){
        List<CourseDto> allCourses = courseService.findAllCourses();

        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    @GetMapping(path = "/api/course/findAllCoursesDto", produces = "application/json")
    public HttpEntity<List<CourseDto>> findAllCoursesDto(){
        List<CourseDto> allCourses = courseService.findAllCoursesDtoFromDB();

        return new ResponseEntity<>(allCourses, HttpStatus.OK);
    }

    @PostMapping(path = "/api/course/addCourse", produces = "application/json")
    public ResponseEntity<Void> addCourse(@RequestBody CourseDto courseDto) {
        log.debug("Add a new course: " + courseDto);

        String userName = getUserName();
//        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
//        log.debug("Current user session id: " + sessionId);
        courseService.registerCourse(userName, courseDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/api/course/getAllRegisteredCourses", produces = "application/json")
    public HttpEntity<List<CourseDto>> getAllRegisteredCourses() {
        String userName = getUserName();

        List<CourseDto> registeredCourses = courseService.getAllRegisteredCourses(userName);
        return new ResponseEntity<>(registeredCourses, HttpStatus.OK);
    }

    @DeleteMapping(path = "api/course/withdrawCourse", produces = "application/json")
    public HttpEntity<Void> withdrawCourse(@RequestBody CourseDto courseDto) {
        String userName = getUserName();

        courseService.withdrawCourse(userName, courseDto);
        return ResponseEntity.ok().build();
    }

    private String getUserName() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        log.debug("Current user's name: " + userName);
        return userName;
    }
}
