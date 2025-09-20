package dto.Semester;

import dto.subject.SubjectResponseDTO;
import model.Semester;

import java.util.List;
import java.util.stream.Collectors;

public class SemesterResponseDTO {
  public Long id;
  public Integer number;
  public Long curriculumId;
  public List<SubjectResponseDTO> subjects;

  public static SemesterResponseDTO fromEntity(Semester semester) {
    SemesterResponseDTO dto = new SemesterResponseDTO();
    dto.id = semester.getId();
    dto.number = semester.getNumber();
    dto.curriculumId = semester.getCurriculum().getId();
    dto.subjects = semester.getSubjects() != null
        ? semester.getSubjects().stream()
            .map(SubjectResponseDTO::fromEntity)
            .collect(Collectors.toList())
        : List.of();
    return dto;
  }
}
