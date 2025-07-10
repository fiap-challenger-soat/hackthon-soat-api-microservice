package video.microservice.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VideoExceptionTest {

    @Test
    void testConstructorWithErrorList() {
        VideoError error = VideoError.builder()
                .code(400)
                .level("ERROR")
                .message("Campo inválido")
                .description("O campo 'nome' é obrigatório")
                .build();

        VideoException exception = new VideoException(HttpStatus.BAD_REQUEST, List.of(error));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Campo inválido", exception.getErrors().get(0).getMessage());
    }

    @Test
    void testConstructorWithOneError() {
        VideoException exception = new VideoException(
                HttpStatus.NOT_FOUND,
                "Vídeo não encontrado",
                "Recurso ausente"
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(1, exception.getErrors().size());
        VideoError error = exception.getErrors().get(0);
        assertEquals("ERROR", error.getLevel());
        assertEquals("Recurso ausente", error.getMessage());
        assertEquals("Vídeo não encontrado", error.getDescription());
        assertEquals(HttpStatus.NOT_FOUND.value(), error.getCode());
    }

    @Test
    void testConstructorWithCustomError() {
        VideoException exception = new VideoException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Falha ao converter vídeo",
                "FATAL",
                1234
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals(1, exception.getErrors().size());
        VideoError error = exception.getErrors().get(0);
        assertEquals("FATAL", error.getLevel());
        assertEquals("Falha ao converter vídeo", error.getMessage());
        assertEquals("Erro interno", error.getDescription());
        assertEquals(1234, error.getCode());
    }

    @Test
    void testConstructorWithCause() {
        Exception cause = new IllegalArgumentException("Parâmetro inválido");

        VideoException exception = new VideoException(
                HttpStatus.BAD_REQUEST,
                "Erro ao processar vídeo",
                "Parâmetro inválido no upload",
                cause
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Erro ao processar vídeo", exception.getErrors().get(0).getMessage());
        assertEquals(cause, exception.getCause());
    }
}
