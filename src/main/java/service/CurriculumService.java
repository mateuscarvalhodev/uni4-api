package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.Curriculum;
import repository.CourseRepository;
import repository.CurriculumRepository;
import model.Course;

import java.util.List;
import java.util.stream.Collectors;

import dto.curriculum.CurriculumRequestDTO;
import dto.curriculum.CurriculumResponseDTO;

@ApplicationScoped
public class CurriculumService {

  @Inject
  CourseRepository courseRepository;

  @Inject
  CurriculumRepository curriculumRepository;

  @Transactional
  public CurriculumResponseDTO addCurriculum(CurriculumRequestDTO dto) {
    Course course = courseRepository.findById(dto.courseId);
    if (course == null)
      return null;

    Curriculum curriculum = new Curriculum();
    curriculum.setAcademicYear(dto.academicYear);
    curriculum.setCourse(course);

    curriculumRepository.add(curriculum);
    return CurriculumResponseDTO.fromEntity(curriculum);
  }

  @Transactional
  public boolean deleteCurriculum(Long id) {
     return curriculumRepository.delete(id);
  }

  @Transactional
  public CurriculumResponseDTO editCurriculum(Long id, CurriculumRequestDTO dto) {
    Curriculum curriculum = curriculumRepository.find(id);
    if (curriculum == null)
      return null;

    Course course = courseRepository.find(dto.courseId);
    if (course == null)
      return null;

    curriculum.setAcademicYear(dto.academicYear);
    curriculum.setCourse(course);

    return CurriculumResponseDTO.fromEntity(curriculum);
  }

  public List<CurriculumResponseDTO> listCurriculum() {
    return Curriculum.listAll()
        .stream()
        .map(c -> CurriculumResponseDTO.fromEntity((Curriculum) c))
        .collect(Collectors.toList());
  }

  public CurriculumResponseDTO findById(Long id) {
    Curriculum curriculum = curriculumRepository.find(id);
    return curriculum == null ? null : CurriculumResponseDTO.fromEntity(curriculum);
  }

  public List<CurriculumResponseDTO> findByAcademicYear(String year) {
    return curriculumRepository.find("academicYear", year)
        .list()
        .stream()
        .map(CurriculumResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }
}
