package video.microservice.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;
import video.microservice.application.process.OutVideoProcess;
import video.microservice.application.response.VideoDownloadResponse;
import video.microservice.application.response.VideoProcessResponse;
import video.microservice.domain.Video;
import video.microservice.domain.VideoTest;
import video.microservice.domain.VideoException;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessServiceImplTest {

    @Mock
    private OutVideoProcess videoProcess;

    @InjectMocks
    private VideoProcessServiceImpl videoProcessService;

    @Test
    @DisplayName("Deve processar o vídeo com sucesso")
    void testShouldProcessVideoSuccessfully() throws IOException {
        String userId = "teste@example";
        byte[] content = "video".getBytes();
        MultipartFile file = new MockMultipartFile("video", "video.mp4", "video/mp4", content);
        VideoProcessResponse expectedResponse = new VideoProcessResponse();

        when(videoProcess.processVideo(any(Video.class), eq(userId))).thenReturn(expectedResponse);

        VideoProcessResponse response = videoProcessService.processVideo(file, userId);

        assertEquals(expectedResponse, response);
        verify(videoProcess, times(1)).processVideo(any(Video.class), eq(userId));
    }

    @Test
    @DisplayName("Deve lançar VideoException se o tipo do video nao for valido")
    void testShouldThrowExceptionForInvalidMimeType() {
        MultipartFile file = new MockMultipartFile("video", "video.xyz", "video/xyz", "data".getBytes());

        VideoException ex = assertThrows(VideoException.class, () -> {
            videoProcessService.processVideo(file, "teste@example");
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertEquals("Invalid video format", ex.getErrors().get(0).getDescription());
    }

    @Test
    @DisplayName("Deve lançar VideoException se ocorrer erro ao obter bytes do vídeo")
    void testShouldThrowExceptionOnGetBytesError() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("video/mp4");
        when(file.getOriginalFilename()).thenReturn("video.mp4");
        when(file.getSize()).thenReturn(123L);
        when(file.getBytes()).thenThrow(new IOException("erro"));

        VideoException ex = assertThrows(VideoException.class, () -> {
            videoProcessService.processVideo(file, "teste@example");
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getHttpStatus());
        assertEquals("Error on get multipart byte",ex.getErrors().get(0).getDescription());
    }

    @Test
    @DisplayName("getVideoStausById deve delegar corretamente")
    void testShouldGetVideoStatusById() {
        UUID videoId = UUID.randomUUID();
        String userId = "teste@example";
        VideoProcessResponse expected = new VideoProcessResponse();

        when(videoProcess.getVideoStausById(videoId, userId)).thenReturn(expected);

        VideoProcessResponse result = videoProcessService.getVideoStausById(videoId, userId);

        assertEquals(expected, result);
        verify(videoProcess).getVideoStausById(videoId, userId);
    }

    @Test
    @DisplayName("getVideoById deve delegar corretamente")
    void shouldGetVideoById() {
        String videoId = "abc-123";
        String userId = "teste@example";
        VideoDownloadResponse expected = new VideoDownloadResponse();

        when(videoProcess.getVideoById(videoId, userId)).thenReturn(expected);

        VideoDownloadResponse result = videoProcessService.getVideoById(videoId, userId);

        assertEquals(expected, result);
        verify(videoProcess).getVideoById(videoId, userId);
    }
}
