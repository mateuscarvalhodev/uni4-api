package dto.subject;

import model.Subject;

public class SubjectResponseDTO {
  public Long id;
  public String name;
  public Integer hours;
  public Long curriculumId;

  public static SubjectResponseDTO fromEntity(Subject subject) {
    SubjectResponseDTO dto = new SubjectResponseDTO();
    dto.id = subject.id;
    dto.name = subject.getName();
    dto.hours = subject.getHours();
    dto.curriculumId = subject.getCurriculum().getId();
    return dto;
  }
}
