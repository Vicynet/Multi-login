package org.project.multilogin.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BaseResponseModel {
    private int statusCode;
    private String status;
    private boolean isSuccessful;
    private String message;
    private Object data;
    private LocalDateTime timestamp;
}
