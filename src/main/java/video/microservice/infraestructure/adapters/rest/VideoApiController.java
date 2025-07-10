package video.microservice.infraestructure.adapters.rest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.VideoProcessServiceImpl;
import video.microservice.infraestructure.adapters.aws.sqs.SqsMessageSender;
import video.microservice.infraestructure.adapters.rest.response.VideoDownloadResponse;
import video.microservice.infraestructure.adapters.rest.response.VideoProcessResponse;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/api/video")

public class VideoApiController {
   @Autowired
    private VideoProcessServiceImpl videoService;
   @Autowired
   private AmazonS3 amazonS3;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoProcessResponse> handleVideoUpload(
            @RequestParam("video") MultipartFile video, @RequestHeader("user_id") String userId) throws IOException {
        return ResponseEntity.ok(videoService.processVideo(video, userId));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoProcessResponse> getVideoStatusById(
            @PathVariable UUID videoId, @RequestHeader("user_id") String userId
    ) {
        return ResponseEntity.ok(videoService.getVideoStausById(videoId, userId));
    }

    @GetMapping("/download/{videoId}")
    public ResponseEntity<VideoDownloadResponse> getVideoById(@PathVariable String videoId){
            return ResponseEntity.ok(videoService.getVideoById(videoId));

    }
}
