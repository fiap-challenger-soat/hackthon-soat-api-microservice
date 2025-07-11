// package video.microservice.infraestructure.adapters.handler;

// import io.cucumber.java.an.E;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.HttpStatus;
// import video.microservice.domain.VideoException;

// public class ErrorHandlerTest {

//     @Test
//     public void shouldTestErrorHandler(){
//         VideoException videoException = new VideoException(HttpStatus.INTERNAL_SERVER_ERROR, "erro 500", "500");
//         ErrorHandler errorHandler = new ErrorHandler();
//         Assertions.assertNotNull(errorHandler.handlerBusinessException(videoException));
//         Exception e = new Exception("Error");
//         Assertions.assertNotNull(errorHandler.handlerGenericException(e));
//     }
// }
