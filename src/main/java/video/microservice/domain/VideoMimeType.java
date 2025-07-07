package payment.microservice.domain;

public enum VideoMimeType {

    MP4("video/mp4"),
    AVI("video/x-msvideo"),
    MOV("video/quicktime"),
    MKV("video/x-matroska"),
    WMV("video/x-ms-wmv"),
    FLV("video/x-flv"),
    WEBM("video/webm");

    private final String mimeType;

    VideoMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static boolean isSupported(String mimeType) {
        for (VideoMimeType type : values()) {
            if (type.getMimeType().equalsIgnoreCase(mimeType)) {
                return true;
            }
        }
        return false;
    }
}
