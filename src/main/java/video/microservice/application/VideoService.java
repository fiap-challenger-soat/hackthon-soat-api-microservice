package payment.microservice.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import payment.microservice.infraestructure.adapters.services.mercadopago.downstream.response.VideoDownloadResponse;
import payment.microservice.domain.VideoProcess;
import payment.microservice.infraestructure.adapters.services.mercadopago.downstream.response.VideoProcessResponse;
import payment.microservice.domain.authorize.PaymentAuthorizationCreated;
import payment.microservice.domain.authorize.PaymentAuthorizationInternalize;
import payment.microservice.domain.capture.PaymentCapture;
import payment.microservice.domain.capture.PaymentCaptureInternalize;

import java.util.UUID;

import static payment.microservice.domain.video.PaymentStatus.FAILED;
import static payment.microservice.domain.video.PaymentStatus.PENDING;

@Slf4j
@Service
public class VideoService implements VideoProcess {

    @Override
    public VideoProcessResponse processVideo(MultipartFile video, UUID userId) {
        return null;
    }

    @Override
    public VideoProcessResponse getVideoStausById(UUID userId) {
        return null;
    }

    @Override
    public VideoDownloadResponse getVideoById(UUID videoId, UUID userId) {
        return null;
    }
}
