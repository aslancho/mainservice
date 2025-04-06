package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.entity.Chapter;
import kz.bitlab.mainservice.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository chapterRepository;

    public List<Chapter> getAll() {
        return chapterRepository.findAll();
    }

    public Optional<Chapter> getById(Long id) {
        return chapterRepository.findById(id);
    }

    public Chapter add(Chapter chapter) {
        return chapterRepository.save(chapter);
    }
}
