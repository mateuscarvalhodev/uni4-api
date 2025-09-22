package service;

import dto.curriculum.CurriculumRequestDTO;
import dto.curriculum.CurriculumResponseDTO;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import model.Course;
import model.Curriculum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.CourseRepository;
import repository.CurriculumRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CurriculumServiceTest {

  @Mock
  CourseRepository courseRepository;

  @Mock
  CurriculumRepository curriculumRepository;

  @InjectMocks
  CurriculumService curriculumService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddCurriculum_success() {
    CurriculumRequestDTO dto = new CurriculumRequestDTO();
    dto.academicYear = "2025";
    dto.courseId = 1L;

    Course mockCourse = new Course();
    mockCourse.setName("Engenharia");

    when(courseRepository.findById(1L)).thenReturn(mockCourse);

    Curriculum curriculum = new Curriculum();
    curriculum.setAcademicYear("2025");
    curriculum.setCourse(mockCourse);

    doNothing().when(curriculumRepository).add(any(Curriculum.class));

    CurriculumResponseDTO response = curriculumService.addCurriculum(dto);

    assertNotNull(response);
    assertEquals("2025", response.academicYear);
    assertEquals("Engenharia", mockCourse.getName());
    verify(curriculumRepository, times(1)).add(any(Curriculum.class));
  }

  @Test
  void testAddCurriculum_courseNotFound() {
    CurriculumRequestDTO dto = new CurriculumRequestDTO();
    dto.academicYear = "2025";
    dto.courseId = 99L;

    when(courseRepository.findById(99L)).thenReturn(null);

    CurriculumResponseDTO response = curriculumService.addCurriculum(dto);

    assertNull(response);
    verify(curriculumRepository, never()).add(any());
  }

  @Test
  void testDeleteCurriculum() {
    when(curriculumRepository.delete(1L)).thenReturn(true);

    boolean result = curriculumService.deleteCurriculum(1L);

    assertTrue(result);
    verify(curriculumRepository, times(1)).delete(1L);
  }

  @Test
  void testEditCurriculum_success() {
    CurriculumRequestDTO dto = new CurriculumRequestDTO();
    dto.academicYear = "2026";
    dto.courseId = 2L;

    Curriculum curriculum = new Curriculum();
    curriculum.setAcademicYear("2025");

    Course newCourse = new Course();
    newCourse.setName("Arquitetura");

    when(curriculumRepository.find(1L)).thenReturn(curriculum);
    when(courseRepository.find(2L)).thenReturn(newCourse);

    CurriculumResponseDTO response = curriculumService.editCurriculum(1L, dto);

    assertNotNull(response);
    assertEquals("2026", response.academicYear);
    assertEquals("Arquitetura", newCourse.getName());
  }

  @Test
  void testEditCurriculum_notFound() {
    CurriculumRequestDTO dto = new CurriculumRequestDTO();
    dto.academicYear = "2026";
    dto.courseId = 1L;

    when(curriculumRepository.find(1L)).thenReturn(null);

    CurriculumResponseDTO response = curriculumService.editCurriculum(1L, dto);

    assertNull(response);
  }

  @Test
  void testFindById() {
    Course course = new Course();
    course.setName("Engenharia");

    Curriculum curriculum = new Curriculum();
    curriculum.setAcademicYear("2025");
    curriculum.setCourse(course);

    when(curriculumRepository.find(10L)).thenReturn(curriculum);

    CurriculumResponseDTO result = curriculumService.findById(10L);

    assertNotNull(result);
    assertEquals("2025", result.academicYear);
  }

  @Test
  void testFindByAcademicYear() {
    Course course = new Course();
    course.setName("Engenharia");

    Curriculum c1 = new Curriculum();
    c1.setAcademicYear("2025");
    c1.setCourse(course);

    Curriculum c2 = new Curriculum();
    c2.setAcademicYear("2025");
    c2.setCourse(course);

    PanacheQuery<Curriculum> mockQuery = mock(PanacheQuery.class);
    when(curriculumRepository.find("academicYear", "2025")).thenReturn(mockQuery);
    when(mockQuery.list()).thenReturn(Arrays.asList(c1, c2));

    List<CurriculumResponseDTO> result = curriculumService.findByAcademicYear("2025");

    assertEquals(2, result.size());
    assertEquals("2025", result.get(0).academicYear);
  }

}
