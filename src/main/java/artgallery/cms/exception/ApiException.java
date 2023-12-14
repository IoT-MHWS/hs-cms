package artgallery.cms.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import artgallery.cms.dto.ApiErrorDTO;

@RequiredArgsConstructor
public class ApiException extends Throwable {
  private final ApiErrorDTO apiError;

  ApiException(HttpStatus status) {
    apiError = new ApiErrorDTO(status);
  }

  ApiException(HttpStatus status, Throwable ex) {
    apiError = new ApiErrorDTO(status, ex);
  }

  ApiException(HttpStatus status, String message) {
    apiError = new ApiErrorDTO(status, message);
  }

  ApiException(HttpStatus status, String message, Throwable ex) {
    apiError = new ApiErrorDTO(status, message, ex);
  }

  public ApiErrorDTO get() {
    return apiError;
  }
}
