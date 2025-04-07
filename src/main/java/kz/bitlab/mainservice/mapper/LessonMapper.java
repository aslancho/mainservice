package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import kz.bitlab.mainservice.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(target = "chapterId", source = "chapter.id")
    LessonResponseDTO toResponseDto(Lesson lesson);

    @Mapping(target = "chapter.id", source = "chapterId")
    Lesson toEntity(LessonCreateDTO lessonCreateDTO);
}