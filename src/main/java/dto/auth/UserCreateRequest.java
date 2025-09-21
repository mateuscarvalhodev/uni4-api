package dto.auth;

import jakarta.validation.constraints.NotBlank;

public class UserCreateRequest {
  @NotBlank(message = "Username is required")
  public String username;

  @NotBlank(message = "Password is required")
  public String password;

  @NotBlank(message = "Email is required")
  public String email;

  @NotBlank(message = "First name is required")
  public String firstName;

  @NotBlank(message = "Last name is required")
  public String lastName;

  @NotBlank(message = "Role is required")
  public String role;
}
