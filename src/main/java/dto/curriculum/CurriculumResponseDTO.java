package dto.curriculum;

import java.util.List;

import dto.Semester.SemesterResponseDTO;
import model.Curriculum;
import java.util.stream.Collectors;

public class CurriculumResponseDTO {
  public Long id;
  public String academicYear;
  public Long courseId;
  public List<SemesterResponseDTO> semesters;

  public static CurriculumResponseDTO fromEntity(Curriculum curriculum) {
    CurriculumResponseDTO dto = new CurriculumResponseDTO();
    dto.id = curriculum.id;
    dto.academicYear = curriculum.getAcademicYear();
    dto.courseId = curriculum.getCourse().getId();
    dto.semesters = curriculum.getSemesters() != null
        ? curriculum.getSemesters().stream().map(SemesterResponseDTO::fromEntity).collect(Collectors.toList())
        : List.of();
    return dto;
  }
}
