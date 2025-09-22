package service;

import dto.user.UserEditRequest;
import dto.user.UserRequest;
import dto.user.UserResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {

  @Inject
  Keycloak keycloak;

  @ConfigProperty(name = "keycloak.realm")
  String REALM;

  @ConfigProperty(name = "keycloak.base-url")
  String BASE_URL;

  @ConfigProperty(name = "quarkus.oidc.client-id")
  String CLIENT_ID;

  @ConfigProperty(name = "quarkus.oidc.credentials.secret")
  String CLIENT_SECRET;

  private final HttpClient httpClient = HttpClient.newHttpClient();

  private UsersResource users() {
    return keycloak.realm(REALM).users();
  }

  // --------- Listar todos ---------
  public List<UserResponse> getAllUsers() {
    try {

      String body = "client_id=" + CLIENT_ID +
          "&client_secret=" + CLIENT_SECRET +
          "&grant_type=client_credentials";

      HttpRequest tokenRequest = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/token"))
          .header("Content-Type", "application/x-www-form-urlencoded")
          .POST(HttpRequest.BodyPublishers.ofString(body))
          .build();

      HttpResponse<String> tokenResponse = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());

      if (tokenResponse.statusCode() != 200) {
        throw new WebApplicationException("Erro ao obter token admin: " + tokenResponse.body(),
            tokenResponse.statusCode());
      }

      ObjectMapper mapper = new ObjectMapper();
      String accessToken = mapper.readTree(tokenResponse.body()).get("access_token").asText();

      HttpRequest usersRequest = HttpRequest.newBuilder()
          .uri(URI.create(BASE_URL + "/admin/realms/" + REALM + "/users"))
          .header("Authorization", "Bearer " + accessToken)
          .GET()
          .build();

      HttpResponse<String> usersResponse = httpClient.send(usersRequest, HttpResponse.BodyHandlers.ofString());

      if (usersResponse.statusCode() != 200) {
        throw new WebApplicationException("Erro ao buscar usuários: " + usersResponse.body(),
            usersResponse.statusCode());
      }

      JsonNode root = mapper.readTree(usersResponse.body());
      List<UserResponse> result = new ArrayList<>();

      for (JsonNode u : root) {
        String id = u.path("id").asText();
        String firstName = u.path("firstName").asText("");
        String lastName = u.path("lastName").asText("");
        String email = u.path("email").asText("");
        String name = (firstName + " " + lastName).trim();

        HttpRequest roleRequest = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/admin/realms/" + REALM + "/users/" + id + "/role-mappings/realm"))
            .header("Authorization", "Bearer " + accessToken)
            .GET()
            .build();

        HttpResponse<String> roleResponse = httpClient.send(roleRequest, HttpResponse.BodyHandlers.ofString());

        String role = null;
        if (roleResponse.statusCode() == 200) {
          JsonNode roles = mapper.readTree(roleResponse.body());
          if (roles.isArray() && roles.size() > 0) {
            role = roles.get(0).path("name").asText();
          }
        }

        result.add(new UserResponse(id, name, email, role));
      }

      return result;

    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new WebApplicationException("Erro ao se comunicar com Keycloak: " + e.getMessage(),
          Response.Status.INTERNAL_SERVER_ERROR);
    }
  }

  // --------- Buscar por ID ---------
  public UserResponse findById(String id) {
    UserRepresentation u = users().get(id).toRepresentation();
    if (u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    return new UserResponse(u.getId(), u.getFirstName(), u.getEmail(), null);
  }

  // --------- Buscar por email ---------
  public UserResponse findByEmail(String email) {
    List<UserRepresentation> result = users().search(email, true);
    if (result.isEmpty()) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    UserRepresentation u = result.get(0);
    return new UserResponse(u.getId(), u.getFirstName(), u.getEmail(), null);
  }

  // --------- Criar usuário ---------
  public UserResponse createUser(UserRequest user) {
    UserRepresentation newUser = new UserRepresentation();
    newUser.setEnabled(true);
    newUser.setFirstName(user.name);
    newUser.setUsername(user.email);
    newUser.setEmail(user.email);

    var response = users().create(newUser);
    if (response.getStatus() != 201) {
      throw new WebApplicationException("Erro ao criar usuário: " + response.getStatus(),
          Response.Status.fromStatusCode(response.getStatus()));
    }

    // pega o id do usuário recém-criado
    String createdId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

    // seta senha
    CredentialRepresentation cred = new CredentialRepresentation();
    cred.setTemporary(false);
    cred.setType(CredentialRepresentation.PASSWORD);
    cred.setValue(user.password);
    users().get(createdId).resetPassword(cred);

    return new UserResponse(createdId, user.name, user.email, user.role);
  }

  // --------- Excluir usuário ---------
  public void deleteById(String id) {
    try {
      users().get(id).remove();
    } catch (Exception e) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
  }

  // --------- Atualizar usuário ---------
  public UserResponse update(String id, UserEditRequest user) {
    UserResource userResource = users().get(id);
    UserRepresentation u = userResource.toRepresentation();
    if (u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }

    u.setFirstName(user.name);
    u.setEmail(user.email);
    u.setUsername(user.email);
    userResource.update(u);

    return new UserResponse(u.getId(), u.getFirstName(), u.getEmail(), null);
  }
}
