package service;

import dto.subject.SubjectRequestDTO;
import dto.subject.SubjectResponseDTO;
import model.Semester;
import model.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.SemesterRepository;
import repository.SubjectRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectServiceTest {

  @Mock
  SubjectRepository subjectRepository;

  @Mock
  SemesterRepository semesterRepository;

  @InjectMocks
  SubjectService subjectService;

  Semester semester;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    semester = new Semester();
    semester.setNumber(1);
  }

  @Test
  void testAddSubject_Success() {
    SubjectRequestDTO dto = new SubjectRequestDTO();
    dto.name = "Math";
    dto.hours = 60;
    dto.semesterId = 1L;

    when(semesterRepository.findById(1L)).thenReturn(semester);

    SubjectResponseDTO response = subjectService.addSubject(dto);

    assertNotNull(response);
    assertEquals("Math", response.name);
    assertEquals(60, response.hours);
    verify(subjectRepository, times(1)).persist(any(Subject.class));
  }

  @Test
  void testAddSubject_SemesterNotFound() {
    SubjectRequestDTO dto = new SubjectRequestDTO();
    dto.name = "Math";
    dto.hours = 60;
    dto.semesterId = 99L;

    when(semesterRepository.findById(99L)).thenReturn(null);

    SubjectResponseDTO response = subjectService.addSubject(dto);

    assertNull(response);
    verify(subjectRepository, never()).persist(any(Subject.class));
  }

  @Test
  void testDeleteSubject() {
    when(subjectRepository.deleteById(1L)).thenReturn(true);

    boolean result = subjectService.deleteSubject(1L);

    assertTrue(result);
    verify(subjectRepository, times(1)).deleteById(1L);
  }

  @Test
  void testEditSubject_Success() {
    Subject existing = new Subject();
    existing.setName("Old Name");
    existing.setHours(30);
    existing.setSemester(semester);

    SubjectRequestDTO dto = new SubjectRequestDTO();
    dto.name = "New Name";
    dto.hours = 45;
    dto.semesterId = 1L;

    when(subjectRepository.findById(1L)).thenReturn(existing);
    when(semesterRepository.findById(1L)).thenReturn(semester);

    SubjectResponseDTO response = subjectService.editSubject(1L, dto);

    assertNotNull(response);
    assertEquals("New Name", response.name);
    assertEquals(45, response.hours);
  }

  @Test
  void testEditSubject_SubjectNotFound() {
    SubjectRequestDTO dto = new SubjectRequestDTO();
    dto.name = "New Name";
    dto.hours = 45;
    dto.semesterId = 1L;

    when(subjectRepository.findById(1L)).thenReturn(null);

    SubjectResponseDTO response = subjectService.editSubject(1L, dto);

    assertNull(response);
  }

  @Test
  void testEditSubject_SemesterNotFound() {
    Subject existing = new Subject();
    existing.setName("Old Name");
    existing.setHours(30);

    SubjectRequestDTO dto = new SubjectRequestDTO();
    dto.name = "New Name";
    dto.hours = 45;
    dto.semesterId = 99L;

    when(subjectRepository.findById(1L)).thenReturn(existing);
    when(semesterRepository.findById(99L)).thenReturn(null);

    SubjectResponseDTO response = subjectService.editSubject(1L, dto);

    assertNull(response);
  }

  @Test
  void testListSubjects() {
    Subject s1 = new Subject();
    s1.setName("Math");
    s1.setHours(60);
    s1.setSemester(semester);

    Subject s2 = new Subject();
    s2.setName("History");
    s2.setHours(40);
    s2.setSemester(semester);

    when(subjectRepository.listAll()).thenReturn(Arrays.asList(s1, s2));

    List<SubjectResponseDTO> result = subjectService.listSubjects();

    assertEquals(2, result.size());
    assertEquals("Math", result.get(0).name);
    assertEquals("History", result.get(1).name);
  }

  @Test
  void testFindById_Found() {
    Subject subject = new Subject();
    subject.setName("Math");
    subject.setHours(60);
    subject.setSemester(semester);

    when(subjectRepository.findById(1L)).thenReturn(subject);

    SubjectResponseDTO result = subjectService.findById(1L);

    assertNotNull(result);
    assertEquals("Math", result.name);
    assertEquals(60, result.hours);
  }

  @Test
  void testFindById_NotFound() {
    when(subjectRepository.findById(1L)).thenReturn(null);

    SubjectResponseDTO result = subjectService.findById(1L);

    assertNull(result);
  }

  @Test
  void testFindByName_Found() {
    Subject subject = new Subject();
    subject.setName("Math");
    subject.setHours(60);
    subject.setSemester(semester);

    when(subjectRepository.findByName("Math")).thenReturn(subject);

    SubjectResponseDTO result = subjectService.findByName("Math");

    assertNotNull(result);
    assertEquals("Math", result.name);
    assertEquals(60, result.hours);
  }

  @Test
  void testFindByName_NotFound() {
    when(subjectRepository.findByName("Science")).thenReturn(null);

    SubjectResponseDTO result = subjectService.findByName("Science");

    assertNull(result);
  }
}
