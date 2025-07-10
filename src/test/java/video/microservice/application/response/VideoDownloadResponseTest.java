package video.microservice.application.response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoDownloadResponseTest {

    @Test
    public void testShouldTestVideoDownload(){
        VideoDownloadResponse videoDownloadResponse = new VideoDownloadResponse();
        videoDownloadResponse.setLinkDownload("http://localhost:4566/arq");

        Assertions.assertEquals("http://localhost:4566/arq", videoDownloadResponse.getLinkDownload());
    }
}
