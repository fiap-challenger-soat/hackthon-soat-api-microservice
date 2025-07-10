package video.microservice.infraestructure.adapters.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import video.microservice.domain.VideoError;
import video.microservice.domain.VideoException;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = VideoException.class)
    public ResponseEntity<?> handlerBusinessException(VideoException exception){
        return new ResponseEntity<>(exception.getErrors(), exception.getHttpStatus());
    }
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<?> handlerGenericException(Exception e){
        return new ResponseEntity<>(List.of(VideoError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .level("ERROR")
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .description(e.getMessage()).build()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
