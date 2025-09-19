package dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
  @NotBlank(message = "O email é obrigatório")
  public String email;

  @NotBlank(message = "A senha é obrigatória")
  public String password;
}
