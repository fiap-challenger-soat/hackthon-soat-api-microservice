package video.microservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoErrorTest {
    @Test
    public void testVideoError(){
        VideoError videoError = new VideoError();
        videoError.setCode(100);
        videoError.setLevel("level");
        videoError.setDescription("desc");
        videoError.setMessage("message");

        Assertions.assertEquals("level", videoError.getLevel());
        Assertions.assertEquals("desc", videoError.getDescription());
        Assertions.assertEquals("message", videoError.getMessage());
        Assertions.assertNotNull(videoError.getCode());
    }
}
