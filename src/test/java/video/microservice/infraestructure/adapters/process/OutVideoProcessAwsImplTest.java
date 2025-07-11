package video.microservice.infraestructure.adapters.process;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;
import video.microservice.domain.*;
import video.microservice.infraestructure.adapters.aws.AwsConfig;
import video.microservice.infraestructure.adapters.aws.sqs.SqsMessageSender;
import video.microservice.infraestructure.adapters.database.entity.UserEntity;
import video.microservice.infraestructure.adapters.database.entity.VideoEntity;
import video.microservice.infraestructure.adapters.database.repository.UserRepository;
import video.microservice.infraestructure.adapters.database.repository.VideoRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutVideoProcessAwsImplTest {

    @Mock
    private AmazonS3 amazonS3;

    @Mock
    private AwsConfig awsConfig;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SqsMessageSender sqsMessageSender;

    @InjectMocks
    private OutVideoProcessAwsImpl outVideoProcessAws;

    @Test
    void shouldProcessVideoSuccessfully() throws IOException {
        Video video = new Video("video.mp4", 1024L, "conteudo".getBytes(), VideoMimeType.WEBM);
        String userId = "user@example.com";
        String bucket = "bucket-test";
        when(awsConfig.getBucket()).thenReturn(bucket);
        when(awsConfig.getQueue()).thenReturn("video-queue");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userId);
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(userEntity));

        when(videoRepository.save(any(VideoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        doNothing().when(sqsMessageSender).sendMessage(anyString(), anyString());

        doAnswer(invocation -> {
            File fileArg = invocation.getArgument(2, File.class);
            return null;
        }).when(amazonS3).putObject(anyString(), anyString(), any(File.class));

        VideoProcessResponse response = outVideoProcessAws.processVideo(video, userId);

        assertNotNull(response);
        assertNotNull(response.getVideoId());
        assertEquals(VideoStatus.QUEUED.getValue(), response.getStatus());

        verify(amazonS3).putObject(eq(bucket), anyString(), any(File.class));
        verify(userRepository).findByEmail(userId);
        verify(videoRepository).save(any(VideoEntity.class));
        verify(sqsMessageSender).sendMessage(anyString(), eq("video-queue"));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Video video = new Video("video.mp4", 1024L, "conteudo".getBytes(), VideoMimeType.FLV);
        String userId = "notfound@example.com";
        when(userRepository.findByEmail(userId)).thenReturn(Optional.empty());

        VideoException ex = assertThrows(VideoException.class, () -> outVideoProcessAws.processVideo(video, userId));
        assertEquals(HttpStatus.FORBIDDEN, ex.getHttpStatus());
        assertEquals("User does not have access", ex.getErrors().get(0).getDescription());
    }

    @Test
    void shouldReturnVideoStatus() {
        UUID videoId = UUID.randomUUID();
        String userId = "user@example.com";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(videoId);
        videoEntity.setStatus(VideoStatus.QUEUED.getValue());

        when(videoRepository.findByIdAndUserEmail(videoId, userId)).thenReturn(Optional.of(videoEntity));

        var response = outVideoProcessAws.getVideoStausById(videoId, userId);

        assertEquals(videoId.toString(), response.getVideoId());
        assertEquals(VideoStatus.QUEUED.getValue(), response.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenVideoStatusNotFound() {
        UUID videoId = UUID.randomUUID();
        String userId = "user@example.com";
        when(videoRepository.findByIdAndUserEmail(videoId, userId)).thenReturn(Optional.empty());

        VideoException ex = assertThrows(VideoException.class, () -> outVideoProcessAws.getVideoStausById(videoId, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        assertEquals("Video not found", ex.getErrors().get(0).getDescription());
    }

    @Test
    void shouldReturnDownloadLink() {
        String videoIdStr = UUID.randomUUID().toString();
        String userId = "user@example.com";
        String outputPath = "videos/output.mp4";
        String bucket = "bucket-test";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(UUID.fromString(videoIdStr));
        videoEntity.setUser(new UserEntity());
        videoEntity.setOutputPath(outputPath);

        when(videoRepository.findByIdAndUserEmail(UUID.fromString(videoIdStr), userId)).thenReturn(Optional.of(videoEntity));
        when(awsConfig.getBucket()).thenReturn(bucket);

        URL presignedUrl = mock(URL.class);
        when(presignedUrl.toString()).thenReturn("http://presigned.url/download");

        when(amazonS3.generatePresignedUrl(any(GeneratePresignedUrlRequest.class))).thenReturn(presignedUrl);

        VideoDownloadResponse response = outVideoProcessAws.getVideoById(videoIdStr, userId);

        assertNotNull(response);
        assertEquals("http://presigned.url/download", response.getLinkDownload());

        verify(amazonS3).generatePresignedUrl(any(GeneratePresignedUrlRequest.class));
    }

    @Test
    void shouldThrowExceptionWhenVideoNotFound() {
        String videoIdStr = UUID.randomUUID().toString();
        String userId = "user@example.com";

        when(videoRepository.findByIdAndUserEmail(UUID.fromString(videoIdStr), userId)).thenReturn(Optional.empty());

        VideoException ex = assertThrows(VideoException.class, () -> outVideoProcessAws.getVideoById(videoIdStr, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        assertEquals("Video not found", ex.getErrors().get(0).getDescription());
    }

    @Test
    void shouldThrowExceptionWhenOutputPathEmpty() {

        String videoIdStr = UUID.randomUUID().toString();
        String userId = "user@example.com";

        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setId(UUID.fromString(videoIdStr));
        videoEntity.setOutputPath(null);

        when(videoRepository.findByIdAndUserEmail(UUID.fromString(videoIdStr), userId)).thenReturn(Optional.of(videoEntity));

        VideoException ex = assertThrows(VideoException.class, () -> outVideoProcessAws.getVideoById(videoIdStr, userId));
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        assertEquals("Video not found", ex.getErrors().get(0).getDescription());
    }
}
