package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.auth.UserCreateRequest;
import dto.auth.UserCreateResponse;
import dto.auth.UserLoginRequest;
import dto.auth.UserLoginResponse;
import dto.auth.UserLogoutRequest;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class AuthServiceTest {

  private AuthService authService;
  private HttpClient httpClient;
  private ObjectMapper mapper;

  @BeforeEach
  void setup() throws Exception {
    authService = new AuthService();
    mapper = new ObjectMapper();

    // Injetar httpClient mockado
    httpClient = mock(HttpClient.class);

    // reflection hack, pq no seu código ele está final
    var httpClientField = AuthService.class.getDeclaredField("httpClient");
    httpClientField.setAccessible(true);
    httpClientField.set(authService, httpClient);

    var mapperField = AuthService.class.getDeclaredField("mapper");
    mapperField.setAccessible(true);
    mapperField.set(authService, mapper);

    // Configurar env fake
    authService.REALM = "school-realm";
    authService.CLIENT_ID = "quarkus-app";
    authService.CLIENT_SECRET = "quarkus-secret";
    authService.BASE_URL = "http://localhost:8081";
  }

  @Test
  void testLoginSuccess() throws Exception {
    UserLoginRequest req = new UserLoginRequest();
    req.username = "admin1";
    req.password = "123456";

    String kcResponse = """
            {
              "access_token": "abc123",
              "refresh_token": "refresh123",
              "expires_in": 300,
              "refresh_expires_in": 1800,
              "token_type": "Bearer",
              "scope": "profile email"
            }
        """;

    HttpResponse<String> mockRes = mock(HttpResponse.class);
    when(mockRes.statusCode()).thenReturn(200);
    when(mockRes.body()).thenReturn(kcResponse);

    when(httpClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenReturn(mockRes);

    UserLoginResponse resp = authService.login(req);

    assertEquals("abc123", resp.accessToken);
    assertEquals("refresh123", resp.refreshToken);
  }

  @Test
  void testLoginFailure() throws Exception {
    UserLoginRequest req = new UserLoginRequest();
    req.username = "wrong";
    req.password = "bad";

    HttpResponse<String> mockRes = mock(HttpResponse.class);
    when(mockRes.statusCode()).thenReturn(401);
    when(mockRes.body()).thenReturn("{\"error\": \"invalid_grant\"}");

    when(httpClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenReturn(mockRes);

    assertThrows(WebApplicationException.class, () -> authService.login(req));
  }

  @Test
  void testLogoutSuccess() throws Exception {
    UserLogoutRequest req = new UserLogoutRequest();
    req.refreshToken = "refresh123";

    HttpResponse<String> mockRes = mock(HttpResponse.class);
    when(mockRes.statusCode()).thenReturn(204);

    when(httpClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenReturn(mockRes);

    assertDoesNotThrow(() -> authService.logout(req));
  }

  @Test
  void testLogoutFailure() throws Exception {
    UserLogoutRequest req = new UserLogoutRequest();
    req.refreshToken = "bad";

    HttpResponse<String> mockRes = mock(HttpResponse.class);
    when(mockRes.statusCode()).thenReturn(400);
    when(mockRes.body()).thenReturn("{\"error\":\"invalid_token\"}");

    when(httpClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenReturn(mockRes);

    assertThrows(WebApplicationException.class, () -> authService.logout(req));
  }

  @Test
  void testCreateUserSuccess() throws Exception {
    UserCreateRequest req = new UserCreateRequest();
    req.username = "testuser";
    req.password = "123456";
    req.email = "test@school.com";
    req.firstName = "Test";
    req.lastName = "User";
    req.role = "aluno";

    // Mock token admin
    HttpResponse<String> tokenRes = mock(HttpResponse.class);
    when(tokenRes.statusCode()).thenReturn(200);
    when(tokenRes.body()).thenReturn("{\"access_token\":\"admintoken\"}");

    // Mock criação usuário
    HttpResponse<String> createUserRes = mock(HttpResponse.class);
    when(createUserRes.statusCode()).thenReturn(201);
    when(createUserRes.headers()).thenReturn(HttpHeadersStub.withLocation("http://localhost/users/123"));

    // Mock buscar role
    HttpResponse<String> roleRes = mock(HttpResponse.class);
    when(roleRes.statusCode()).thenReturn(200);
    when(roleRes.body()).thenReturn("{\"id\":\"role123\",\"name\":\"aluno\"}");

    // Mock assign role
    HttpResponse<String> assignRoleRes = mock(HttpResponse.class);
    when(assignRoleRes.statusCode()).thenReturn(204);

    when(httpClient.send(any(), any(HttpResponse.BodyHandler.class)))
        .thenReturn(tokenRes, createUserRes, roleRes, assignRoleRes);

    UserCreateResponse resp = authService.createUser(req);

    assertEquals("testuser", resp.username);
    assertEquals("aluno", resp.role);
  }
}
