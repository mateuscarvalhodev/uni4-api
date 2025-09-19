package dto.user;

import jakarta.validation.constraints.NotBlank;

public class UserEditRequest {
  @NotBlank(message = "O nome é obrigatório")
  public String name;

  @NotBlank(message = "O email é obrigatório")
  public String email;

  @NotBlank(message = "A role é obrigatória")
  public String role;
}
