package dto.course;

import java.util.List;

import dto.curriculum.CurriculumResponseDTO;
import model.Course;
import java.util.stream.Collectors;

public class CourseResponseDTO {
  public Long id;
  public String name;
  public String description;
  public Integer totalHours;
  public List<CurriculumResponseDTO> curriculum;

  public static CourseResponseDTO fromEntity(Course course) {
    CourseResponseDTO dto = new CourseResponseDTO();
    dto.id = course.getId();
    dto.name = course.getName();
    dto.description = course.getDescription();
    dto.totalHours = course.getTotalHours();

    dto.curriculum = course.getCurriculumn() != null
        ? course.getCurriculumn().stream()
            .map(CurriculumResponseDTO::fromEntity)
            .collect(Collectors.toList())
        : List.of(); 

    return dto;
  }
}
