package video.microservice.domain;

public enum VideoStatus {
    QUEUED("QUEUED");
    private final String status;

    VideoStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }


}
