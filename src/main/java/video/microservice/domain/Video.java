package payment.microservice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Video {
    private String name;
    private VideoMimeType type;
    private String size;
}
