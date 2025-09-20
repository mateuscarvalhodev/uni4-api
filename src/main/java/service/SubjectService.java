package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import repository.CurriculumRepository;
import repository.SubjectRepository;
import model.Subject;
import model.Curriculum;
import dto.subject.SubjectRequestDTO;
import dto.subject.SubjectResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SubjectService {

  @Inject
  SubjectRepository subjectRepository;

  @Inject
  CurriculumRepository curriculumRepository;

  @Transactional
  public SubjectResponseDTO addSubject(SubjectRequestDTO dto) {
    Curriculum curriculum = curriculumRepository.findById(dto.curriculumId);
    if (curriculum == null)
      return null;

    Subject subject = new Subject();
    subject.setName(dto.name);
    subject.setHours(dto.hours);
    subject.setCurriculum(curriculum);

    subjectRepository.persist(subject);
    return SubjectResponseDTO.fromEntity(subject);
  }

  @Transactional
  public boolean deleteSubject(Long id) {
    return subjectRepository.deleteById(id);
  }

  @Transactional
  public SubjectResponseDTO editSubject(Long id, SubjectRequestDTO dto) {
    Subject subject = subjectRepository.findById(id);
    if (subject == null)
      return null;

    Curriculum curriculum = curriculumRepository.findById(dto.curriculumId);
    if (curriculum == null)
      return null;

    subject.setName(dto.name);
    subject.setHours(dto.hours);
    subject.setCurriculum(curriculum);

    return SubjectResponseDTO.fromEntity(subject);
  }

  public List<SubjectResponseDTO> listSubjects() {
    return subjectRepository.listAll()
        .stream()
        .map(SubjectResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }

  public SubjectResponseDTO findById(Long id) {
    Subject subject = subjectRepository.findById(id);
    return subject == null ? null : SubjectResponseDTO.fromEntity(subject);
  }

  public SubjectResponseDTO findByName(String name) {
    Subject subject = subjectRepository.findByName(name);
    return subject == null ? null : SubjectResponseDTO.fromEntity(subject);
  }
}
