package dto.Semester;

import jakarta.validation.constraints.NotNull;

public class SemesterRequestDTO {
  @NotNull(message = "Semester number is required")
  public Integer number;

  @NotNull(message = "Curriculum ID is required")
  public Long curriculumId;
}
