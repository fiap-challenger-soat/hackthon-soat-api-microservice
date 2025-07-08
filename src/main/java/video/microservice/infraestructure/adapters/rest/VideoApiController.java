package video.microservice.infraestructure.adapters.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.VideoService;
import video.microservice.infraestructure.adapters.rest.response.VideoDownloadResponse;
import video.microservice.infraestructure.adapters.rest.response.VideoProcessResponse;


import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/api/video")

public class VideoApiController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<VideoProcessResponse> handleVideoUpload(
            @RequestParam("video") MultipartFile video, @RequestHeader("user_id") UUID userId) {
        return ResponseEntity.ok(videoService.processVideo(video, userId));
    }

    @GetMapping("/{videoId}")
    public ResponseEntity<VideoProcessResponse> getVideoStatusById(
            @PathVariable UUID videoId, @RequestHeader("user_id") UUID userId
    ) {
        return ResponseEntity.ok(videoService.getVideoStausById(userId));
    }

    @GetMapping("/download/{videoId}")
    public ResponseEntity<VideoDownloadResponse> getVideoById(@PathVariable UUID videoId, @RequestHeader("user_id") UUID userId){
        return ResponseEntity.ok(videoService.getVideoById(videoId, userId));
    }
}
