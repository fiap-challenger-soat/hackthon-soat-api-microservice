package payment.microservice.domain;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface VideoProcess {
    VideoProcessResponse processVideo(MultipartFile video, UUID userId);
    VideoProcessResponse getVideoStausById(UUID userId);
    VideoDownloadResponse getVideoById(UUID videoId, UUID userId);
}
