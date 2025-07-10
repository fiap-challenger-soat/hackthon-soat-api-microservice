package video.microservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class VideoException extends RuntimeException{
    private List<VideoError> errors;
    private HttpStatus httpStatus;

    // we can use this constructor when we have a list of erros and want to pass to the client
    public VideoException(HttpStatus httpStatus, List<VideoError> errorList){
        super();
        this.errors = errorList;
        this.httpStatus = httpStatus;
    }
    //use this when you want to return one error
    public VideoException(HttpStatus httpStatus, String description, String msg){
        super();
        VideoError error = VideoError.builder()
                .code(httpStatus.value())
                .level("ERROR")
                .message(msg)
                .description(description)
                .build();
        this.errors = new ArrayList<>();
        errors.add(error);
        this.httpStatus = httpStatus;
    }

    //use this when you want to return one error and have level and code
    public VideoException(HttpStatus httpStatus, String description, String msg, String level, Integer code){
        super();
        VideoError error = VideoError.builder()
                .code(code)
                .level(level)
                .message(msg)
                .description(description)
                .build();
        this.errors = new ArrayList<>();
        errors.add(error);
        this.httpStatus = httpStatus;
    }

    //use this when you have an try catch for an specific exception and wants to throw our business to padronize
    public VideoException(HttpStatus httpStatus, String msg, String description, Exception cause){
        super(cause);
        VideoError error = VideoError.builder()
                .code(httpStatus.value())
                .level("ERROR")
                .message(msg)
                .description(description)
                .build();
        this.errors = new ArrayList<>();
        errors.add(error);
        this.httpStatus = httpStatus;
    }
}
