package artgallery.cms.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class CustomAuthenticationFilter extends AuthenticationFilter {

  private static final String HEADER_USER_ID = "X-User-Id";
  private static final String HEADER_USER_NAME = "X-User-Name";
  private static final String HEADER_USER_AUTHORITIES = "X-User-Authorities";

  public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager, new CustomAuthenticationConverter());
    this.setSuccessHandler(new CustomAuthenticationSuccessHandler());
  }


  public static class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
      // do nothing
    }
  }

  public static class CustomAuthenticationConverter implements AuthenticationConverter {
    @Override
    public Authentication convert(HttpServletRequest request) {
      Long userId = null;
      String userName = null;

      try {
        userId = Long.parseLong(request.getHeader(HEADER_USER_ID));
        userName = request.getHeader(HEADER_USER_NAME);
      } catch (NumberFormatException | NullPointerException ignored) {
        return null;
      }

      var authorities = this.extractAuthorities(request.getHeader(HEADER_USER_AUTHORITIES));
      var userDetails = new ServerUserDetails(userId, userName, authorities);
      return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private List<GrantedAuthority> extractAuthorities(String authoritiesHeader) {
      List<String> roles;
      if (StringUtils.hasText(authoritiesHeader)) {
        roles = Arrays.asList(authoritiesHeader.split(","));
      } else {
        roles = new ArrayList<>();
      }
      return roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    }
  }
}
