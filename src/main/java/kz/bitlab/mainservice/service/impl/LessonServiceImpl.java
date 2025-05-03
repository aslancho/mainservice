package kz.bitlab.mainservice.service.impl;

import kz.bitlab.mainservice.dto.lesson.request.LessonCreateDTO;
import kz.bitlab.mainservice.dto.lesson.request.LessonUpdateDTO;
import kz.bitlab.mainservice.dto.lesson.response.LessonResponseDTO;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Lesson;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.LessonMapper;
import kz.bitlab.mainservice.repository.ChapterRepository;
import kz.bitlab.mainservice.repository.LessonRepository;
import kz.bitlab.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LessonServiceImpl implements LessonService {

    private static final Logger logger = LoggerFactory.getLogger(LessonServiceImpl.class);
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;
    private final LessonMapper lessonMapper;

    @Transactional(readOnly = true)
    @Override
    public List<LessonResponseDTO> getAllLessons() {
        logger.info("Fetching all lessons");
        return lessonRepository.findAll().stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public LessonResponseDTO getLessonById(Long id) {
        logger.info("Fetching lesson with id: {}", id);
        return lessonRepository.findById(id)
                .map(lessonMapper::toResponseDto)
                .orElseThrow(() -> ResourceNotFoundException.lessonNotFound(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<LessonResponseDTO> getLessonsByChapterId(Long chapterId) {
        logger.info("Fetching lessons for chapter with id: {}", chapterId);
        // Проверяем, существует ли глава
        if (!chapterRepository.existsById(chapterId)) {
            throw ResourceNotFoundException.chapterNotFound(chapterId);
        }

        // Здесь нужно добавить метод в репозиторий или использовать фильтрацию
        List<Lesson> lessons = lessonRepository.findAll().stream()
                .filter(lesson -> lesson.getChapter().getId().equals(chapterId))
                .toList();

        return lessons.stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponseDTO createLesson(LessonCreateDTO lessonCreateDTO) {
        logger.info("Creating new lesson: {}", lessonCreateDTO);

        // Поиск главы
        Chapter chapter = chapterRepository.findById(lessonCreateDTO.getChapterId())
                .orElseThrow(() -> ResourceNotFoundException.chapterNotFound(lessonCreateDTO.getChapterId()));

        // Преобразование DTO в сущность
        Lesson lesson = lessonMapper.toEntity(lessonCreateDTO);
        lesson.setChapter(chapter);

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        lesson.setCreatedTime(now);
        lesson.setUpdatedTime(now);

        // Сохранение в БД
        Lesson savedLesson = lessonRepository.save(lesson);
        logger.debug("Created lesson: {}", savedLesson);

        return lessonMapper.toResponseDto(savedLesson);
    }

    @Override
    public LessonResponseDTO updateLesson(LessonUpdateDTO lessonUpdateDTO) {
        Long lessonId = lessonUpdateDTO.getId();
        logger.info("Updating lesson with id: {}", lessonId);

        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> ResourceNotFoundException.lessonNotFound(lessonId));

        // Обновляем только те поля, которые есть в DTO
        if (lessonUpdateDTO.getName() != null) {
            existingLesson.setName(lessonUpdateDTO.getName());
        }
        if (lessonUpdateDTO.getDescription() != null) {
            existingLesson.setDescription(lessonUpdateDTO.getDescription());
        }
        if (lessonUpdateDTO.getContent() != null) {
            existingLesson.setContent(lessonUpdateDTO.getContent());
        }
        if (lessonUpdateDTO.getOrder() != null) {
            existingLesson.setOrder(lessonUpdateDTO.getOrder());
        }

        existingLesson.setUpdatedTime(LocalDateTime.now());
        Lesson savedLesson = lessonRepository.save(existingLesson);
        logger.debug("Updated lesson: {}", savedLesson);

        return lessonMapper.toResponseDto(savedLesson);
    }

    @Override
    public void deleteLesson(Long id) {
        logger.info("Deleting lesson with id: {}", id);
        if (!lessonRepository.existsById(id)) {
            throw ResourceNotFoundException.lessonNotFound(id);
        }

        lessonRepository.deleteById(id);
        logger.info("Deleted lesson with id: {}", id);
    }
}