package kz.bitlab.mainservice.service.impl;

import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.request.ChapterUpdateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.entity.Course;
import kz.bitlab.mainservice.exception.ResourceNotFoundException;
import kz.bitlab.mainservice.mapper.ChapterMapper;
import kz.bitlab.mainservice.repository.ChapterRepository;
import kz.bitlab.mainservice.repository.CourseRepository;
import kz.bitlab.mainservice.service.ChapterService;
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
public class ChapterServiceImpl implements ChapterService {

    private static final Logger logger = LoggerFactory.getLogger(ChapterServiceImpl.class);
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final ChapterMapper chapterMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ChapterResponseDTO> getAllChapters() {
        logger.info("Fetching all chapters");
        return chapterRepository.findAll().stream()
                .map(chapterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ChapterResponseDTO getChapterById(Long id) {
        logger.info("Fetching chapter with id: {}", id);
        return chapterRepository.findById(id)
                .map(chapterMapper::toResponseDto)
                .orElseThrow(() -> ResourceNotFoundException.chapterNotFound(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChapterResponseDTO> getChaptersByCourseId(Long courseId) {
        logger.info("Fetching chapters for course with id: {}", courseId);
        // Проверяем, существует ли курс
        if (!courseRepository.existsById(courseId)) {
            throw ResourceNotFoundException.courseNotFound(courseId);
        }

        // Здесь нужно добавить метод в репозиторий или использовать фильтрацию
        List<Chapter> chapters = chapterRepository.findAll().stream()
                .filter(chapter -> chapter.getCourse().getId().equals(courseId))
                .toList();

        return chapters.stream()
                .map(chapterMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChapterResponseDTO createChapter(ChapterCreateDTO chapterCreateDTO) {
        logger.info("Creating new chapter: {}", chapterCreateDTO);

        // Поиск курса
        Course course = courseRepository.findById(chapterCreateDTO.getCourseId())
                .orElseThrow(() -> ResourceNotFoundException.courseNotFound(chapterCreateDTO.getCourseId()));

        // Преобразование DTO в сущность
        Chapter chapter = chapterMapper.toEntity(chapterCreateDTO);
        chapter.setCourse(course);

        // Установка времени создания и обновления
        LocalDateTime now = LocalDateTime.now();
        chapter.setCreatedTime(now);
        chapter.setUpdatedTime(now);

        // Сохранение в БД
        Chapter savedChapter = chapterRepository.save(chapter);
        logger.debug("Created chapter: {}", savedChapter);

        return chapterMapper.toResponseDto(savedChapter);
    }

    @Override
    public ChapterResponseDTO updateChapter(ChapterUpdateDTO chapterUpdateDTO) {
        Long chapterId = chapterUpdateDTO.getId();
        logger.info("Updating chapter with id: {}", chapterId);

        Chapter existingChapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> ResourceNotFoundException.chapterNotFound(chapterId));

        // Обновляем только те поля, которые есть в DTO
        if (chapterUpdateDTO.getName() != null) {
            existingChapter.setName(chapterUpdateDTO.getName());
        }
        if (chapterUpdateDTO.getDescription() != null) {
            existingChapter.setDescription(chapterUpdateDTO.getDescription());
        }
        if (chapterUpdateDTO.getOrder() != null) {
            existingChapter.setOrder(chapterUpdateDTO.getOrder());
        }

        existingChapter.setUpdatedTime(LocalDateTime.now());
        Chapter savedChapter = chapterRepository.save(existingChapter);
        logger.debug("Updated chapter: {}", savedChapter);

        return chapterMapper.toResponseDto(savedChapter);
    }

    @Override
    public void deleteChapter(Long id) {
        logger.info("Deleting chapter with id: {}", id);
        if (!chapterRepository.existsById(id)) {
            throw ResourceNotFoundException.chapterNotFound(id);
        }

        chapterRepository.deleteById(id);
        logger.info("Deleted chapter with id: {}", id);
    }
}