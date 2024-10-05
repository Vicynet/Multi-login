package org.project.multilogin.exception;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException{
    private final Object data;
    private EApplicationExceptionCode eApplicationExceptionCode;

    public ApplicationException(String message, Object data) {
        super(message);
        this.data = data;
    }

    public ApplicationException(String message, Object data, EApplicationExceptionCode eApplicationExceptionCode) {
        super(message);
        this.data = data;
        this.eApplicationExceptionCode = eApplicationExceptionCode;
    }
}
