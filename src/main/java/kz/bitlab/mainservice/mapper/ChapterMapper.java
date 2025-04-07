package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import kz.bitlab.mainservice.entity.Chapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(target = "courseId", source = "course.id")
    ChapterResponseDTO toResponseDto(Chapter chapter);

    @Mapping(target = "course.id", source = "courseId")
    Chapter toEntity(ChapterCreateDTO chapterCreateDTO);
}