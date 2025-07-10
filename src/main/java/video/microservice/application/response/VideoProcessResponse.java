package video.microservice.application.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoProcessResponse {
    private String status;
    private String videoId;
}
