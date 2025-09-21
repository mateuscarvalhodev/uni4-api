package dto.auth;

import jakarta.validation.constraints.NotBlank;

public class UserLoginRequest {
  @NotBlank(message = "Username is required")
  public String username;

  @NotBlank(message = "Password is required")
  public String password;
}