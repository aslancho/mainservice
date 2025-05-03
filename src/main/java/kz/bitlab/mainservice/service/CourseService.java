package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.course.request.CourseCreateDTO;
import kz.bitlab.mainservice.dto.course.request.CourseUpdateDTO;
import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseService {
    @Transactional(readOnly = true)
    List<CourseResponseDTO> getAllCourses();

    @Transactional(readOnly = true)
    CourseResponseDTO getCourseById(Long id);

    CourseResponseDTO createCourse(CourseCreateDTO courseCreateDTO);

    CourseResponseDTO updateCourse(CourseUpdateDTO courseUpdateDTO);

    void deleteCourse(Long id);
}
