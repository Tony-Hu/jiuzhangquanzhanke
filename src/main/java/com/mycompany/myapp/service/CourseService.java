package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Course;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.UserCourse;
import com.mycompany.myapp.domain.dto.CourseDto;
import com.mycompany.myapp.repository.CourseRepository;
import com.mycompany.myapp.repository.UserCourseRepository;
import com.mycompany.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserCourseRepository userCourseRepository;

    @Autowired
    private UserRepository userRepository;

    private List<CourseDto> courseCache = new ArrayList<>();

    public List<CourseDto> findAllCourses(){
        if (courseCache.isEmpty()) {
            List<Course> courses= courseRepository.findAll();
            for(Course c : courses) {
                courseCache.add(new CourseDto(c.getCourseName(), c.getCourseLocation(), c.getCourseContent(), c.getTeacherId()));
            }
        }

        return courseCache;
    }

    public List<CourseDto> findAllCoursesDtoFromDB(){
        return courseRepository.findAllCoursesDto();
    }

    public void registerCourse(String userName, CourseDto courseDto) {
        UserCourse userCourse = generateUserCourse(userName, courseDto);
        userCourseRepository.save(userCourse);
    }

    public List<CourseDto> getAllRegisteredCourses(String userName) {
        Optional<User> userResult = userRepository.findOneByLogin(userName);
        List<CourseDto> result = new ArrayList<>();
        if (userResult.isPresent()) {
            User user = userResult.get();
            userCourseRepository.findAllByUser(user).ifPresent((courses) -> {
                for (UserCourse userCourse : courses) {
                    Course course = userCourse.getCourse();
                    result.add(new CourseDto(course.getCourseName(), course.getCourseLocation(), course.getCourseContent(), course.getTeacherId()));
                }
            });
        }
        return result;
    }

    public void withdrawCourse(String userName, CourseDto courseDto) {
        UserCourse userCourse = generateUserCourse(userName, courseDto);
        userCourseRepository.deleteUserCourseByUserAndCourse(userCourse.getUser(), userCourse.getCourse());
    }

    private UserCourse generateUserCourse(String userName, CourseDto courseDto) {
        UserCourse userCourse = new UserCourse();
        userRepository.findOneByLogin(userName).ifPresent(userCourse::setUser);
        courseRepository.findOneByCourseName(courseDto.getCourseName()).ifPresent(userCourse::setCourse);
        return userCourse;
    }
}
