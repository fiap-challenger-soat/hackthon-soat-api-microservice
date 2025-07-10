package video.microservice.infraestructure.adapters.process;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import video.microservice.application.process.OutVideoProcess;
import video.microservice.domain.*;
import video.microservice.infraestructure.adapters.aws.AwsConfig;
import video.microservice.infraestructure.adapters.aws.sqs.SqsMessageSender;
import video.microservice.infraestructure.adapters.database.entity.UserEntity;
import video.microservice.infraestructure.adapters.database.entity.VideoEntity;
import video.microservice.infraestructure.adapters.database.repository.UserRepository;
import video.microservice.infraestructure.adapters.database.repository.VideoRepository;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;

import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OutVideoProcessAwsImpl implements OutVideoProcess {
    @Autowired
    private AmazonS3 amazonS3;
    @Autowired
    private AwsConfig awsConfig;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SqsMessageSender sqsMessageSender;

    @Override
    public VideoProcessResponse processVideo(Video video, String userId) throws IOException {
        String key = video.getName().concat(LocalDateTime.now().toString());
        amazonS3.putObject(awsConfig.getBucket(), key, convertBytesToFile(video));
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setVideoPath(key);
        Optional<UserEntity> user = userRepository.findByEmail(userId);
        user.ifPresentOrElse(videoEntity::setUser, ()->{
            throw new VideoException(HttpStatus.FORBIDDEN, "User does not have access", "Forbidden");
        });
        videoEntity.setStatus(VideoStatus.QUEUED.getValue());
        videoEntity.setCreatedAt(OffsetDateTime.now());
        videoEntity.setId(UUID.randomUUID());
        videoRepository.save(videoEntity);
        sqsMessageSender.sendMessage(String.valueOf(videoEntity.getId()), "video-queue");
        VideoProcessResponse videoProcessResponse = new VideoProcessResponse();
        videoProcessResponse.setVideoId(String.valueOf(videoEntity.getId()));
        videoProcessResponse.setStatus(VideoStatus.QUEUED.getValue());

        return videoProcessResponse;
    }
    public static File convertBytesToFile(Video video) throws IOException {
        File convFile = File.createTempFile("upload_", "_" + video.getName());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(video.getContent());
        }catch (IOException ioException){
            throw new VideoException(HttpStatus.INTERNAL_SERVER_ERROR, "Error on converting video bytes to file", "Internal server error");
        }
        return convFile;
    }
    @Override
    public VideoProcessResponse getVideoStausById(UUID videoId, String userId) {
        Optional<VideoEntity> videoEntity = videoRepository.findByIdAndUserEmail(videoId, userId);
        if(videoEntity.isEmpty()){
            throw new VideoException(HttpStatus.NOT_FOUND, "Video not found", "Not found");
        }
        VideoProcessResponse videoProcessResponse = new VideoProcessResponse();
        videoProcessResponse.setStatus(videoEntity.get().getStatus());
        videoProcessResponse.setVideoId(String.valueOf(videoEntity.get().getId()));

        return videoProcessResponse;
    }

    @Override
    public VideoDownloadResponse getVideoById(String videoId, String userId) {
        Optional<VideoEntity> videoEntity = videoRepository.findByIdAndUserEmail(UUID.fromString(videoId), userId);
        if(videoEntity.isEmpty() || ObjectUtils.isEmpty(videoEntity.get().getOutputPath())){
                throw new VideoException(HttpStatus.NOT_FOUND, "Video not found", "Not found");
            }

        Date expiration = Date.from(Instant.now().plus(5, ChronoUnit.HOURS));

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(awsConfig.getBucket(), videoEntity.get().getOutputPath())
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        VideoDownloadResponse videoDownloadResponse = new VideoDownloadResponse();
        videoDownloadResponse.setLinkDownload(amazonS3.generatePresignedUrl(request).toString());
        return videoDownloadResponse;
    }
}
