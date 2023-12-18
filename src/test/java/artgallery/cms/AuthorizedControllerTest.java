package artgallery.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public abstract class AuthorizedControllerTest {
  private static final String HEADER_USER_ID = "X-User-Id";
  private static final String HEADER_USER_NAME = "X-User-Name";
  private static final String HEADER_USER_AUTHORITIES = "X-User-Authorities";
  private static final String userId = "1";
  private static final String username = "user";
  private static final String publicAuthorities = "ROLE_PUBLIC";
  private static final String moderatorAuthorities = "ROLE_MODERATOR";

  static protected final ObjectMapper objectMapper = new ObjectMapper();

  protected static HttpHeaders moderatorAuthHeaders = new HttpHeaders();
  protected static HttpHeaders publicAuthHeaders = new HttpHeaders();

  @BeforeAll
  static void setupHeaders() {
    moderatorAuthHeaders.set(HEADER_USER_ID, userId);
    moderatorAuthHeaders.set(HEADER_USER_NAME, username);
    moderatorAuthHeaders.set(HEADER_USER_AUTHORITIES, moderatorAuthorities);

    publicAuthHeaders.set(HEADER_USER_ID, userId);
    publicAuthHeaders.set(HEADER_USER_NAME, username);
    publicAuthHeaders.set(HEADER_USER_AUTHORITIES, publicAuthorities);
  }
}
