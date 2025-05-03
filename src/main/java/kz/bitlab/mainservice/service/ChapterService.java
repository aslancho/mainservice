package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.chapter.request.ChapterCreateDTO;
import kz.bitlab.mainservice.dto.chapter.request.ChapterUpdateDTO;
import kz.bitlab.mainservice.dto.chapter.response.ChapterResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChapterService {
    @Transactional(readOnly = true)
    List<ChapterResponseDTO> getAllChapters();

    @Transactional(readOnly = true)
    ChapterResponseDTO getChapterById(Long id);

    @Transactional(readOnly = true)
    List<ChapterResponseDTO> getChaptersByCourseId(Long courseId);

    ChapterResponseDTO createChapter(ChapterCreateDTO chapterCreateDTO);

    ChapterResponseDTO updateChapter(ChapterUpdateDTO chapterUpdateDTO);

    void deleteChapter(Long id);
}
