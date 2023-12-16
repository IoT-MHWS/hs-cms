package artgallery.cms.configuration;

import artgallery.cms.dto.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

  @ResponseBody
  @ExceptionHandler({  AccessDeniedException.class })
  public ResponseEntity<?> handleAuthenticationException(Exception ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiErrorDTO(HttpStatus.FORBIDDEN, "Authentication failed", ex));
  }
}
