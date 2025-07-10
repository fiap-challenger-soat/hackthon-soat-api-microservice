package video.microservice.application.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoProcessResponseTest {

    @Test
    public void testShouldProcess(){
        VideoProcessResponse videoProcessResponse = new VideoProcessResponse();
        videoProcessResponse.setVideoId("videoId");
        videoProcessResponse.setStatus("QUEUED");

        Assertions.assertEquals("videoId", videoProcessResponse.getVideoId());
        Assertions.assertEquals("QUEUED", videoProcessResponse.getStatus());
    }
}
