package artgallery.cms.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import artgallery.cms.dto.ApiErrorDTO;

@ControllerAdvice
public class ServerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ InsufficientAuthenticationException.class, AccessDeniedException.class })
  @ResponseBody
  public ResponseEntity<ApiErrorDTO> handleAuthenticationException(Exception ex) {
    var re = new ApiErrorDTO(HttpStatus.UNAUTHORIZED,
        "Authentication failed", ex);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
  }

}
