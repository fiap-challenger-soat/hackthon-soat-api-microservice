package video.microservice.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VideoErrorTest {

    @Test
    public void shouldCreateObjectWithBuilder() {
        VideoError error = VideoError.builder()
                .code(1)
                .message("Invalid payment method")
                .level("ERROR")
                .description("The payment method provided is not supported.")
                .build();

        assertEquals(1, error.getCode());
        assertEquals("Invalid payment method", error.getMessage());
        assertEquals("ERROR", error.getLevel());
        assertEquals("The payment method provided is not supported.", error.getDescription());
    }

    @Test
    public void shouldCreateObjectWithAllArgsConstructor() {
        VideoError error = new VideoError(
                2,
                "Missing amount",
                "WARNING",
                "The payment amount was not provided in the request."
        );

        assertEquals(2, error.getCode());
        assertEquals("Missing amount", error.getMessage());
        assertEquals("WARNING", error.getLevel());
        assertEquals("The payment amount was not provided in the request.", error.getDescription());
    }

    @Test
    public void shouldCreateObjectWithNoArgsConstructor() {
        VideoError error = new VideoError();

        error.setCode(3);
        error.setMessage("Currency not supported");
        error.setLevel("ERROR");
        error.setDescription("The provided currency is not supported by the system.");

        assertEquals(3, error.getCode());
        assertEquals("Currency not supported", error.getMessage());
        assertEquals("ERROR", error.getLevel());
        assertEquals("The provided currency is not supported by the system.", error.getDescription());
    }
}
