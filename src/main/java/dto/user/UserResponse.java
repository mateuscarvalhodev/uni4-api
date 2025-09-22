package dto.user;

public class UserResponse {
  public String id;
  public String name;
  public String email;
  public String role;

  public UserResponse(String id, String name, String email, String role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }
}
