package dto.subject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubjectRequestDTO {
  @NotBlank(message = "Name is required")
  public String name;

  @NotNull(message = "Hours are required")
  public Integer hours;

  @NotNull(message = "Curriculum ID is required")
  public Long curriculumId;
}
