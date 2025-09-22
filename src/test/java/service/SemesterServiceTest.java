package service;

import dto.Semester.SemesterRequestDTO;
import dto.Semester.SemesterResponseDTO;
import model.Curriculum;
import model.Semester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.CurriculumRepository;
import repository.SemesterRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SemesterServiceTest {

  @Mock
  SemesterRepository semesterRepository;

  @Mock
  CurriculumRepository curriculumRepository;

  @InjectMocks
  SemesterService semesterService;

  Curriculum curriculum;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    curriculum = new Curriculum();
    curriculum.setAcademicYear("2025");
  }

  @Test
  void testAddSemester_Success() {
    SemesterRequestDTO dto = new SemesterRequestDTO();
    dto.number = 1;
    dto.curriculumId = 1L;

    when(curriculumRepository.findById(1L)).thenReturn(curriculum);

    SemesterResponseDTO response = semesterService.addSemester(dto);

    assertNotNull(response);
    assertEquals(1, response.number);
    verify(semesterRepository, times(1)).persist(any(Semester.class));
  }

  @Test
  void testAddSemester_CurriculumNotFound() {
    SemesterRequestDTO dto = new SemesterRequestDTO();
    dto.number = 1;
    dto.curriculumId = 99L;

    when(curriculumRepository.findById(99L)).thenReturn(null);

    SemesterResponseDTO response = semesterService.addSemester(dto);

    assertNull(response);
    verify(semesterRepository, never()).persist(any(Semester.class));
  }

  @Test
  void testDeleteSemester() {
    when(semesterRepository.deleteById(1L)).thenReturn(true);

    boolean result = semesterService.deleteSemester(1L);

    assertTrue(result);
    verify(semesterRepository, times(1)).deleteById(1L);
  }

  @Test
  void testEditSemester_Success() {
    Semester existing = new Semester();
    existing.setNumber(1);
    existing.setCurriculum(curriculum);

    SemesterRequestDTO dto = new SemesterRequestDTO();
    dto.number = 2;
    dto.curriculumId = 1L;

    when(semesterRepository.findById(1L)).thenReturn(existing);
    when(curriculumRepository.findById(1L)).thenReturn(curriculum);

    SemesterResponseDTO response = semesterService.editSemester(1L, dto);

    assertNotNull(response);
    assertEquals(2, response.number);
  }

  @Test
  void testEditSemester_SemesterNotFound() {
    SemesterRequestDTO dto = new SemesterRequestDTO();
    dto.number = 2;
    dto.curriculumId = 1L;

    when(semesterRepository.findById(1L)).thenReturn(null);

    SemesterResponseDTO response = semesterService.editSemester(1L, dto);

    assertNull(response);
  }

  @Test
  void testEditSemester_CurriculumNotFound() {
    Semester existing = new Semester();
    existing.setNumber(1);

    SemesterRequestDTO dto = new SemesterRequestDTO();
    dto.number = 2;
    dto.curriculumId = 99L;

    when(semesterRepository.findById(1L)).thenReturn(existing);
    when(curriculumRepository.findById(99L)).thenReturn(null);

    SemesterResponseDTO response = semesterService.editSemester(1L, dto);

    assertNull(response);
  }

  @Test
  void testListSemesters() {
    Semester s1 = new Semester();
    s1.setNumber(1);
    s1.setCurriculum(curriculum);

    Semester s2 = new Semester();
    s2.setNumber(2);
    s2.setCurriculum(curriculum);

    when(semesterRepository.listAll()).thenReturn(Arrays.asList(s1, s2));

    List<SemesterResponseDTO> result = semesterService.listSemesters();

    assertEquals(2, result.size());
    assertEquals(1, result.get(0).number);
    assertEquals(2, result.get(1).number);
  }

  @Test
  void testFindById_Found() {
    Semester semester = new Semester();
    semester.setNumber(1);
    semester.setCurriculum(curriculum);

    when(semesterRepository.findById(1L)).thenReturn(semester);

    SemesterResponseDTO result = semesterService.findById(1L);

    assertNotNull(result);
    assertEquals(1, result.number);
  }

  @Test
  void testFindById_NotFound() {
    when(semesterRepository.findById(1L)).thenReturn(null);

    SemesterResponseDTO result = semesterService.findById(1L);

    assertNull(result);
  }

  @Test
  void testFindByNumberAndCurriculum_Found() {
    Semester semester = new Semester();
    semester.setNumber(1);
    semester.setCurriculum(curriculum);

    when(semesterRepository.findByNumberAndCurriculum(1, 1L)).thenReturn(semester);

    SemesterResponseDTO result = semesterService.findByNumberAndCurriculum(1, 1L);

    assertNotNull(result);
    assertEquals(1, result.number);
  }

  @Test
  void testFindByNumberAndCurriculum_NotFound() {
    when(semesterRepository.findByNumberAndCurriculum(1, 1L)).thenReturn(null);

    SemesterResponseDTO result = semesterService.findByNumberAndCurriculum(1, 1L);

    assertNull(result);
  }
}
