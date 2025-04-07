package kz.bitlab.mainservice.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException courseNotFound(Long courseId) {
        return new ResourceNotFoundException(String.format("Course with id %d not found", courseId));
    }

    public static ResourceNotFoundException chapterNotFound(Long chapterId) {
        return new ResourceNotFoundException(String.format("Chapter with id %d not found", chapterId));
    }

    public static ResourceNotFoundException lessonNotFound(Long lessonId) {
        return new ResourceNotFoundException(String.format("Lesson with id %d not found", lessonId));
    }
}