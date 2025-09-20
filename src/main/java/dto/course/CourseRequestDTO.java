package dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CourseRequestDTO {

  @NotBlank(message = "Name is required")
  public String name;

  public String description;

  @NotNull(message = "Total hours is required")
  public Integer totalHours;
}
