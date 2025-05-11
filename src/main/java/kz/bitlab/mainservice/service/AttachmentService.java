package kz.bitlab.mainservice.service;

import kz.bitlab.mainservice.dto.attachment.request.AttachmentCreateDTO;
import kz.bitlab.mainservice.dto.attachment.response.AttachmentResponseDTO;

import java.util.List;

public interface AttachmentService {
    AttachmentResponseDTO uploadAttachment(AttachmentCreateDTO attachmentCreateDTO);
    byte[] downloadAttachment(Long attachmentId);
    List<AttachmentResponseDTO> getAttachmentsByLessonId(Long lessonId);
    void deleteAttachment(Long attachmentId);
}