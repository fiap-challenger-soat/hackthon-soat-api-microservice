package video.microservice.application.service;

import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;

import java.io.IOException;
import java.util.UUID;

public interface VideoProcessService {
    VideoProcessResponse processVideo(MultipartFile multipartFile, String userId) throws IOException;
    VideoProcessResponse getVideoStausById(UUID videoId, String userId);
    VideoDownloadResponse getVideoById(String videoId, String userId);
}
