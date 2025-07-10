package video.microservice.infraestructure.adapters.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;
import video.microservice.application.service.VideoProcessServiceImpl;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoApiControllerTest {

    @Mock
    private VideoProcessServiceImpl videoService;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private VideoApiController controller;

    private final String CLIENT_ID = "usuario@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(jwt.getClaimAsString("clientId")).thenReturn(CLIENT_ID);
    }

    @Test
    @DisplayName("deve retornar 200")
    void testShouldHandleVideoUpload() throws IOException {
        VideoProcessResponse expectedResponse = new VideoProcessResponse();
        when(videoService.processVideo(multipartFile, CLIENT_ID)).thenReturn(expectedResponse);

        ResponseEntity<VideoProcessResponse> response = controller.handleVideoUpload(multipartFile, jwt);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        verify(videoService).processVideo(multipartFile, CLIENT_ID);
    }

    @Test
    @DisplayName("deve retornar status")
    void testShouldGetVideoStatusById() {
        UUID videoId = UUID.randomUUID();
        VideoProcessResponse expectedResponse = new VideoProcessResponse();
        when(videoService.getVideoStausById(videoId, CLIENT_ID)).thenReturn(expectedResponse);

        ResponseEntity<VideoProcessResponse> response = controller.getVideoStatusById(videoId, jwt);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        verify(videoService).getVideoStausById(videoId, CLIENT_ID);
    }

    @Test
    @DisplayName(" deve retornar link auto assinado para download na aws /localstack")
    void testGetVideoById() {
        String videoId = UUID.randomUUID().toString();
        VideoDownloadResponse expectedResponse = new VideoDownloadResponse();
        expectedResponse.setLinkDownload("http://download.link");

        when(videoService.getVideoById(videoId, CLIENT_ID)).thenReturn(expectedResponse);

        ResponseEntity<VideoDownloadResponse> response = controller.getVideoById(jwt, videoId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        verify(videoService).getVideoById(videoId, CLIENT_ID);
    }
}
