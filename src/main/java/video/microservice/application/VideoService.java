package video.microservice.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.domain.VideoProcess;
import video.microservice.infraestructure.adapters.rest.response.VideoDownloadResponse;
import video.microservice.infraestructure.adapters.rest.response.VideoProcessResponse;

import java.util.UUID;

@Slf4j
@Service
public class VideoService implements VideoProcess {

    @Override
    public VideoProcessResponse processVideo(MultipartFile video, UUID userId) {
        return null;
    }

    @Override
    public VideoProcessResponse getVideoStausById(UUID userId) {
        return null;
    }

    @Override
    public VideoDownloadResponse getVideoById(UUID videoId, UUID userId) {
        return null;
    }
}
