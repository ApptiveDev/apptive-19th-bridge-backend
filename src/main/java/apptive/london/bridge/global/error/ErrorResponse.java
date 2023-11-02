package apptive.london.bridge.global.error;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(Exception e) {
        this.message = e.getMessage();
    }
}
