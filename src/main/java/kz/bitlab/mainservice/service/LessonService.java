package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.request.LessonUpdateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonService {
    @Transactional(readOnly = true)
    List<LessonResponseDTO> getAllLessons();

    @Transactional(readOnly = true)
    LessonResponseDTO getLessonById(Long id);

    @Transactional(readOnly = true)
    List<LessonResponseDTO> getLessonsByChapterId(Long chapterId);

    LessonResponseDTO createLesson(LessonCreateDTO lessonCreateDTO);

    LessonResponseDTO updateLesson(LessonUpdateDTO lessonUpdateDTO);

    void deleteLesson(Long id);
}
