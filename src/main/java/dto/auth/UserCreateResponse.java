package dto.auth;

public class UserCreateResponse {
  public String id;
  public String username;
  public String email;
  public String role;

  public UserCreateResponse(String id, String username, String email, String role) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.role = role;
  }
}
