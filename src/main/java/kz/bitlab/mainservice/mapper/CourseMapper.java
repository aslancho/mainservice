package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.course.response.CourseResponseDTO;
import kz.bitlab.mainservice.entity.Course;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseResponseDTO toResponseDto(Course course);
}