package dto.curriculum;

import java.util.List;

import dto.subject.SubjectResponseDTO;
import model.Curriculum;
import java.util.stream.Collectors;

public class CurriculumResponseDTO {
  public Long id;
  public String academicYear;
  public Long courseId;
  public List<SubjectResponseDTO> subjects;

  public static CurriculumResponseDTO fromEntity(Curriculum curriculum) {
    CurriculumResponseDTO dto = new CurriculumResponseDTO();
    dto.id = curriculum.id;
    dto.academicYear = curriculum.getAcademicYear();
    dto.courseId = curriculum.getCourse().getId();
    dto.subjects = curriculum.getSubjects() != null
        ? curriculum.getSubjects().stream().map(SubjectResponseDTO::fromEntity).collect(Collectors.toList())
        : List.of();
    return dto;
  }
}
