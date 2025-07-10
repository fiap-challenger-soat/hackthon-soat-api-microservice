package video.microservice.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.process.OutVideoProcess;
import video.microservice.domain.Video;
import video.microservice.domain.VideoException;
import video.microservice.domain.VideoMimeType;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;

import java.io.IOException;
import java.util.UUID;
@Service
public class VideoProcessServiceImpl implements VideoProcessService {
    @Autowired
    private OutVideoProcess videoProcess;
    @Override
    public VideoProcessResponse processVideo(MultipartFile multipartFile, String userId) throws IOException {
        Video video = null;
        if(!VideoMimeType.isSupported(multipartFile.getContentType())){
            throw new VideoException(HttpStatus.BAD_REQUEST, "Invalid video format", "Bad Request");
        }
        try {
            video =  new Video(multipartFile.getOriginalFilename(), multipartFile.getSize(), multipartFile.getBytes(), VideoMimeType.fromMime(multipartFile.getContentType()));
        } catch (IOException e) {
            throw new VideoException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on get multipart byte", "Internal Server Error");
        }

        return videoProcess.processVideo(video, userId);
    }

    @Override
    public VideoProcessResponse getVideoStausById(UUID videoId, String userId) {
        return videoProcess.getVideoStausById(videoId, userId);
    }

    @Override
    public VideoDownloadResponse getVideoById(String videoId, String userId) {
        return videoProcess.getVideoById(videoId, userId);
    }
}
