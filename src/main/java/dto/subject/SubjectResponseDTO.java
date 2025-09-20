package dto.subject;

import model.Subject;

public class SubjectResponseDTO {
  public Long id;
  public String name;
  public Integer hours;
  public Long semesterId;

  public static SubjectResponseDTO fromEntity(Subject subject) {
    SubjectResponseDTO dto = new SubjectResponseDTO();
    dto.id = subject.id;
    dto.name = subject.getName();
    dto.hours = subject.getHours();
    dto.semesterId = subject.getSemester().getId();
    return dto;
  }
}
