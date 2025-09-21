package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.auth.UserCreateRequest;
import dto.auth.UserCreateResponse;
import dto.auth.UserLoginRequest;
import dto.auth.UserLoginResponse;
import dto.auth.UserLogoutRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AuthService {
  @ConfigProperty(name = "keycloak.realm")
  String REALM;

  @ConfigProperty(name = "quarkus.oidc.client-id")
  String CLIENT_ID;

  @ConfigProperty(name = "quarkus.oidc.credentials.secret")
  String CLIENT_SECRET;

  @ConfigProperty(name = "quarkus.oidc.auth-server-base-url")
  String BASE_URL;


  private final HttpClient httpClient = HttpClient.newHttpClient();
  private final ObjectMapper mapper = new ObjectMapper();

  public UserLoginResponse login(UserLoginRequest req) {
    try {
      String body = "client_id=" + CLIENT_ID +
          "&client_secret=" + CLIENT_SECRET +
          "&username=" + URLEncoder.encode(req.username, StandardCharsets.UTF_8) +
          "&password=" + URLEncoder.encode(req.password, StandardCharsets.UTF_8) +
          "&grant_type=password";

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/token"))
          .header("Content-Type", "application/x-www-form-urlencoded")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        throw new WebApplicationException(response.body(), response.statusCode());
      }

      return  mapper.readValue(response.body(), UserLoginResponse.class);
    } catch (Exception e) {
      throw new WebApplicationException("Erro ao autenticar no Keycloak: " + e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public void logout(UserLogoutRequest req) {
    try {
      String body = "client_id=" + CLIENT_ID +
          "&client_secret=" + CLIENT_SECRET +
          "&refresh_token=" + URLEncoder.encode(req.refreshToken, StandardCharsets.UTF_8);

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/logout"))
          .header("Content-Type", "application/x-www-form-urlencoded")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 204) {
        throw new WebApplicationException("Erro ao realizar logout: " + response.body(),
            response.statusCode());
      }

    } catch (Exception e) {
      throw new WebApplicationException("Erro ao se comunicar com Keycloak: " + e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  public UserCreateResponse createUser(UserCreateRequest req) {
    try {

      String body = "client_id=admin-cli" +
          "&username=admin" +
          "&password=admin" +
          "&grant_type=password";
      HttpRequest tokenReq = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/realms/master/protocol/openid-connect/token"))
          .header("Content-Type", "application/x-www-form-urlencoded")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> tokenRes = httpClient.send(tokenReq, HttpResponse.BodyHandlers.ofString());
      if (tokenRes.statusCode() != 200) {
        throw new WebApplicationException("Falha ao obter token admin", tokenRes.statusCode());
      }
      String accessToken = mapper.readTree(tokenRes.body()).get("access_token").asText();

      Map<String, Object> kcUser = new HashMap<>();
      kcUser.put("username", req.username);
      kcUser.put("enabled", true);
      kcUser.put("email", req.email);
      kcUser.put("firstName", req.firstName);
      kcUser.put("lastName", req.lastName);
      kcUser.put("credentials", List.of(Map.of(
          "type", "password",
          "value", req.password,
          "temporary", false)));

      String userJson = mapper.writeValueAsString(kcUser);

      HttpRequest createUserReq = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/admin/realms/" + REALM + "/users"))
          .header("Authorization", "Bearer " + accessToken)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(userJson))
          .build();

      HttpResponse<String> createUserRes = httpClient.send(createUserReq, HttpResponse.BodyHandlers.ofString());
      if (createUserRes.statusCode() != 201) {
        throw new WebApplicationException("Erro ao criar usuário: " + createUserRes.body(),
            createUserRes.statusCode());
      }

      String location = createUserRes.headers().firstValue("Location").orElse(null);
      String userId = location.substring(location.lastIndexOf("/") + 1);

      HttpRequest roleReq = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/admin/realms/" + REALM + "/roles/" + req.role))
          .header("Authorization", "Bearer " + accessToken)
          .GET()
          .build();

      HttpResponse<String> roleRes = httpClient.send(roleReq, HttpResponse.BodyHandlers.ofString());
      if (roleRes.statusCode() != 200) {
        throw new WebApplicationException("Role não encontrada", roleRes.statusCode());
      }

      Map<String, Object> role = mapper.readValue(roleRes.body(), new TypeReference<Map<String, Object>>() {
      });

      String roleJson = mapper.writeValueAsString(List.of(role));

      HttpRequest assignRoleReq = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/admin/realms/" + REALM + "/users/" + userId + "/role-mappings/realm"))
          .header("Authorization", "Bearer " + accessToken)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(roleJson))
          .build();

      HttpResponse<String> assignRoleRes = httpClient.send(assignRoleReq, HttpResponse.BodyHandlers.ofString());
      if (assignRoleRes.statusCode() != 204) {
        throw new WebApplicationException("Erro ao atribuir role: " + assignRoleRes.body(),
            assignRoleRes.statusCode());
      }

      return new UserCreateResponse(userId, req.username, req.email, req.role);

    } catch (Exception e) {
      throw new WebApplicationException("Erro ao criar usuário no Keycloak: " + e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }
}
