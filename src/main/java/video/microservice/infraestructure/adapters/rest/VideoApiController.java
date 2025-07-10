package video.microservice.infraestructure.adapters.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;
import video.microservice.application.service.VideoProcessServiceImpl;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/api/video")

public class VideoApiController {
    private static final String CLAIM = "clientId";
   @Autowired
    private VideoProcessServiceImpl videoService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoProcessResponse> handleVideoUpload(
            @RequestParam("video") MultipartFile video, @AuthenticationPrincipal Jwt jwt) throws IOException {
        return ResponseEntity.ok(videoService.processVideo(video, jwt.getClaimAsString(CLAIM)));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoProcessResponse> getVideoStatusById(
            @PathVariable UUID videoId, @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(videoService.getVideoStausById(videoId, jwt.getClaimAsString(CLAIM)));
    }

    @GetMapping("/download/{videoId}")
    public ResponseEntity<VideoDownloadResponse> getVideoById(@AuthenticationPrincipal Jwt jwt, @PathVariable String videoId){

            return ResponseEntity.ok(videoService.getVideoById(videoId, jwt.getClaimAsString(CLAIM)));

    }
}
