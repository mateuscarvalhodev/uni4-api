package dto.curriculum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CurriculumRequestDTO {
  @NotBlank(message = "Academic year is required")
  public String academicYear;

  @NotNull(message = "Course ID is required")
  public Long courseId;
}
