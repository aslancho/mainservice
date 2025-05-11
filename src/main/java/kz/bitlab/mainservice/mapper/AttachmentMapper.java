package kz.bitlab.mainservice.mapper;

import kz.bitlab.mainservice.dto.attachment.response.AttachmentResponseDTO;
import kz.bitlab.mainservice.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    @Mapping(target = "lessonId", source = "lesson.id")
    AttachmentResponseDTO toResponseDto(Attachment attachment);
}