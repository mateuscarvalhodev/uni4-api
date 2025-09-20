package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import repository.SemesterRepository;
import repository.CurriculumRepository;
import model.Semester;
import model.Curriculum;
import dto.Semester.SemesterRequestDTO;
import dto.Semester.SemesterResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SemesterService {

  @Inject
  SemesterRepository semesterRepository;

  @Inject
  CurriculumRepository curriculumRepository;

  @Transactional
  public SemesterResponseDTO addSemester(SemesterRequestDTO dto) {
    Curriculum curriculum = curriculumRepository.findById(dto.curriculumId);
    if (curriculum == null)
      return null;

    Semester semester = new Semester();
    semester.setNumber(dto.number);
    semester.setCurriculum(curriculum);

    semesterRepository.persist(semester);
    return SemesterResponseDTO.fromEntity(semester);
  }

  @Transactional
  public boolean deleteSemester(Long id) {
    return semesterRepository.deleteById(id);
  }

  @Transactional
  public SemesterResponseDTO editSemester(Long id, SemesterRequestDTO dto) {
    Semester semester = semesterRepository.findById(id);
    if (semester == null)
      return null;

    Curriculum curriculum = curriculumRepository.findById(dto.curriculumId);
    if (curriculum == null)
      return null;

    semester.setNumber(dto.number);
    semester.setCurriculum(curriculum);

    return SemesterResponseDTO.fromEntity(semester);
  }

  public List<SemesterResponseDTO> listSemesters() {
    return semesterRepository.listAll()
        .stream()
        .map(SemesterResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }

  public SemesterResponseDTO findById(Long id) {
    Semester semester = semesterRepository.findById(id);
    return semester == null ? null : SemesterResponseDTO.fromEntity(semester);
  }

  public SemesterResponseDTO findByNumberAndCurriculum(Integer number, Long curriculumId) {
    Semester semester = semesterRepository.findByNumberAndCurriculum(number, curriculumId);
    return semester == null ? null : SemesterResponseDTO.fromEntity(semester);
  }
}
