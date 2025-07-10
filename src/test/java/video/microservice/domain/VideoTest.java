package video.microservice.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoTest {

    @Test
    public void testVideo(){
        Assertions.assertThrows(VideoException.class, () -> new Video(null, null, null, null));
    }
}
