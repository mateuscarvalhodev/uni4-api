package config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

@ApplicationScoped
public class KeycloakProducer {

  @ConfigProperty(name = "keycloak.base-url")
  String keycloakBaseUrl;

  @ConfigProperty(name = "keycloak.realm")
  String realm;

  @ConfigProperty(name = "quarkus.oidc.client-id")
  String clientId;

  @ConfigProperty(name = "quarkus.oidc.credentials.secret")
  String clientSecret;

  @Produces
  @ApplicationScoped
  public Keycloak keycloak() {
    System.out.println("=== DEBUG KEYCLOAK CONFIG ===");
    System.out.println("ClientId: '" + clientId + "'");
    System.out.println("ClientSecret: '" + (clientSecret != null ? clientSecret : "NULL") + "'");
    System.out.println("Realm: '" + realm + "'");
    System.out.println("ServerUrl: '" + keycloakBaseUrl + "'");

    // Verificar se há espaços ou caracteres especiais
    System.out.println("ClientId length: " + (clientId != null ? clientId.length() : 0));
    System.out.println("ClientSecret length: " + (clientSecret != null ? clientSecret.length() : 0));
    System.out.println("Realm length: " + (realm != null ? realm.length() : 0));
    System.out.println("ServerUrl length: " + (keycloakBaseUrl != null ? keycloakBaseUrl.length() : 0));
    System.out.println("=============================");

    try {
      Keycloak keycloak = Keycloak.getInstance(
          keycloakBaseUrl,
          realm,
          clientId,
          clientSecret,
          OAuth2Constants.CLIENT_CREDENTIALS);

      System.out.println("Keycloak client criado com sucesso");
      System.out.println("Testando obtenção de token...");

      // Vamos tentar obter o token aqui para ver o erro específico
      try {
        String token = keycloak.tokenManager().getAccessToken().getToken();
        System.out.println("Token obtido com sucesso pelo Admin Client!");
        System.out.println("Token prefix: " + token.substring(0, 50) + "...");
      } catch (Exception tokenException) {
        System.err.println("Erro ao obter token pelo Admin Client: " + tokenException.getMessage());
        System.err.println("Tipo da exceção: " + tokenException.getClass().getSimpleName());
        tokenException.printStackTrace();
      }

      return keycloak;

    } catch (Exception e) {
      System.err.println("Erro ao criar Keycloak client: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Não foi possível conectar ao Keycloak", e);
    }
  }
}