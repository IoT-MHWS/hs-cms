package artgallery.cms.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final String[] WHITE_LIST_URLS = {
    "/api-docs",
    "/api-docs/**",
    "/swagger-ui",
    "/swagger-ui/**",
    "/webjars/**"
  };

  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
    http
      .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
      .authorizeHttpRequests((req) -> req
        .requestMatchers(WHITE_LIST_URLS).permitAll()
        .anyRequest().authenticated())
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .authenticationProvider(authenticationProvider)
      .addFilterAt(customAuthenticationFilter(authenticationManager), BasicAuthenticationFilter.class)
      .exceptionHandling(configurer -> {
        configurer.authenticationEntryPoint(customAuthenticationEntryPoint());
      });

    return http.build();
  }

  @Bean
  public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
    return new CustomAuthenticationFilter(authenticationManager);
  }

  @Bean
  public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint();
  }

}
