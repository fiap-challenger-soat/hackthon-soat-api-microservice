package payment.microservice.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoError {
    /*
        code of the error, if you want to pass a dictionary of possible errors, you can use this to return to your client
        and with this code, he/she can do some actions to resolve
     */
    private Integer code;
    /*
        simple error message
     */
    private String message;
    /*
        It's a good practice have some level of your errors (info, warning, error and etc)
        In our case, the majority will be error
     */
    private String level;
    /*
        Detail of the issue/error
     */
    private String description;
}
