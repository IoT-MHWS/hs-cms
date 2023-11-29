package artgallery.cms.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ServerAuthenticationFilter extends OncePerRequestFilter {

  private static final String HEADER_USER_ID = "X-User-Id";
  private static final String HEADER_USER_NAME = "X-User-Name";
  private static final String HEADER_USER_ROLES = "X-User-Authorities";

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      Long userId = Long.parseLong(request.getHeader(HEADER_USER_ID));
      String userName = request.getHeader(HEADER_USER_NAME);
      List<String> userAuthorities = Arrays.asList(request.getHeader(HEADER_USER_ROLES).split(","));

      var userDetails = new ServerUserDetails(userId, userName, userAuthorities);
      var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);

    } catch (NumberFormatException | NullPointerException ex) {
      resolver.resolveException(request, response, null, ex);
    }

    filterChain.doFilter(request, response);

  }

}
