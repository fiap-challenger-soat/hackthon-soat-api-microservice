package video.microservice.domain;

import io.netty.util.internal.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

@Getter
@Setter

public class Video {
    private String name;
    private VideoMimeType type;
    private Long size;
    private byte[] content;

    public Video (String name, Long size, byte[] content, VideoMimeType type){
        if(ObjectUtils.isEmpty(name) || ObjectUtils.isEmpty(content) || ObjectUtils.isEmpty(size) || ObjectUtils.isEmpty(type)){
            throw new VideoException(HttpStatus.BAD_REQUEST, "One or more value are wrong or null", "Bad Request");
        }
        this.name = name;
        this.size = size;
        this.content = content;
        this.type = type;
    }
}
