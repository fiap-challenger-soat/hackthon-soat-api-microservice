package video.microservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoSqsTest {

    @Test
    public void testVideoSqs(){
        VideoSqs videoSqs = new VideoSqs("videoId");
        Assertions.assertEquals("videoId", videoSqs.getVideoId());
        videoSqs.setVideoId("digimonVideo");
        Assertions.assertEquals("digimonVideo", videoSqs.getVideoId());
    }
}
