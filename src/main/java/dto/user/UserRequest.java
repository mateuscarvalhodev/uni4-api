package dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {
  @NotBlank(message = "O nome é obrigatório")
  public String name;

  @NotBlank(message = "O email é obrigatório")
  public String email;
  
  @NotBlank(message = "A senha é obrigatória")
  public String password;
  
  @NotBlank(message = "A role é obrigatória")
  public String role;
}
