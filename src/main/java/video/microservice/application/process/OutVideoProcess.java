package video.microservice.application.process;

import video.microservice.domain.Video;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;

import java.io.IOException;
import java.util.UUID;

public interface OutVideoProcess {
    VideoProcessResponse processVideo(Video video, String userId) throws IOException;
    VideoProcessResponse getVideoStausById(UUID videoId, String userId);
    VideoDownloadResponse getVideoById(String videoId, String userId);
}
