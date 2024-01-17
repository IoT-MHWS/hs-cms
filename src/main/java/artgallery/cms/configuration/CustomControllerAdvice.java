package artgallery.cms.configuration;

import artgallery.cms.dto.ApiErrorDTO;
import artgallery.cms.exception.DoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<ApiErrorDTO> handleAuthenticationException(Exception ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiErrorDTO(HttpStatus.FORBIDDEN, "Authentication failed", ex));
  }

  @ResponseBody
  @ExceptionHandler({DoesNotExistException.class})
  public ResponseEntity<ApiErrorDTO> handleDoesNotExistException(DoesNotExistException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDTO(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  @ResponseBody
  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<ApiErrorDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorDTO(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

}
