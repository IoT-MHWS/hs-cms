package artgallery.cms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiErrorDTO {

  private HttpStatus status;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime timestamp;
  private String message;
  private String debugMessage;

  private ApiErrorDTO() {
    timestamp = LocalDateTime.now();
  }

  public ApiErrorDTO(HttpStatus status) {
    this();
    this.status = status;
  }

  public ApiErrorDTO(HttpStatus status, Throwable ex) {
    this();
    this.status = status;
    this.message = "Unexpected error";
    this.debugMessage = ex.getMessage();
  }

  public ApiErrorDTO(HttpStatus status, String message) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = null;
  }

  public ApiErrorDTO(HttpStatus status, String message, Throwable ex) {
    this();
    this.status = status;
    this.message = message;
    this.debugMessage = ex.getMessage();
  }
}
