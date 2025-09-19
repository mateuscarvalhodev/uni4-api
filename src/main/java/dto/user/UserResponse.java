package dto.user;

public class UserResponse {
  public long id;
  public String name;
  public String email;
  public String role;

  public UserResponse(long id, String name, String email, String role) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
  }
}
