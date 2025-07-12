package video.microservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VideoSqs {

    @JsonProperty("job_id")
    private String videoId;

    public VideoSqs(String videoId) {
        this.videoId = videoId;
    }
}