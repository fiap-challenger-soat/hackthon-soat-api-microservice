package video.microservice.infraestructure.adapters.rest.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoProcessResponse {
    private String status;
    private String videoId;
}
