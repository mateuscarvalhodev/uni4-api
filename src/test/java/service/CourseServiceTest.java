package service;

import dto.course.CourseRequestDTO;
import dto.course.CourseResponseDTO;
import model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.CourseRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private CourseService courseService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- addCourse ----------
  @Test
  void testAddCourseSuccess() {
    CourseRequestDTO dto = new CourseRequestDTO();
    dto.name = "Java";
    dto.description = "Curso de Java";
    dto.totalHours = 40;

    Course course = new Course();
    course.setName(dto.name);
    course.setDescription(dto.description);
    course.setTotalHours(dto.totalHours);

    doAnswer(invocation -> {
      return null;
    }).when(courseRepository).add(any(Course.class));

    CourseResponseDTO response = courseService.addCourse(dto);

    assertNotNull(response);
    assertEquals("Java", response.name);
    assertEquals(40, response.totalHours);
    verify(courseRepository, times(1)).add(any(Course.class));
  }

  // ---------- deleteCourse ----------
  @Test
  void testDeleteCourseSuccess() {
    when(courseRepository.deleteById(1L)).thenReturn(true);

    boolean result = courseService.deleteCourse(1L);

    assertTrue(result);
    verify(courseRepository).deleteById(1L);
  }

  @Test
  void testDeleteCourseNotFound() {
    when(courseRepository.deleteById(99L)).thenReturn(false);

    boolean result = courseService.deleteCourse(99L);

    assertFalse(result);
    verify(courseRepository).deleteById(99L);
  }

  // ---------- editCourse ----------
  @Test
  void testEditCourseSuccess() {
    Course existing = new Course();
    existing.setName("Old");
    existing.setDescription("Old Desc");
    existing.setTotalHours(20);

    CourseRequestDTO dto = new CourseRequestDTO();
    dto.name = "New";
    dto.description = "New Desc";
    dto.totalHours = 50;

    when(courseRepository.find(1L)).thenReturn(existing);

    CourseResponseDTO response = courseService.editCourse(1L, dto);

    assertNotNull(response);
    assertEquals("New", response.name);
    assertEquals(50, response.totalHours);
    verify(courseRepository).find(1L);
  }

  @Test
  void testEditCourseNotFound() {
    when(courseRepository.find(99L)).thenReturn(null);

    CourseRequestDTO dto = new CourseRequestDTO();
    CourseResponseDTO response = courseService.editCourse(99L, dto);

    assertNull(response);
    verify(courseRepository).find(99L);
  }

  // ---------- listCourses ----------
  @Test
  void testListCourses() {
    Course c1 = new Course();
    c1.setName("Java");
    Course c2 = new Course();
    c2.setName("Python");

    when(courseRepository.getAll()).thenReturn(Arrays.asList(c1, c2));

    List<CourseResponseDTO> result = courseService.listCourses();

    assertEquals(2, result.size());
    assertEquals("Java", result.get(0).name);
    assertEquals("Python", result.get(1).name);
    verify(courseRepository).getAll();
  }

  // ---------- findById ----------
  @Test
  void testFindByIdFound() {
    Course course = new Course();
    course.setName("Java");

    when(courseRepository.find(1L)).thenReturn(course);

    CourseResponseDTO response = courseService.findById(1L);

    assertNotNull(response);
    assertEquals("Java", response.name);
  }

  @Test
  void testFindByIdNotFound() {
    when(courseRepository.find(1L)).thenReturn(null);

    CourseResponseDTO response = courseService.findById(1L);

    assertNull(response);
  }

  // ---------- findByName ----------
  @Test
  void testFindByNameFound() {
    Course course = new Course();
    
    course.setName("Python");

    when(courseRepository.getByName("Python")).thenReturn(course);

    CourseResponseDTO response = courseService.findByName("Python");

    assertNotNull(response);
    assertEquals("Python", response.name);
  }

  @Test
  void testFindByNameNotFound() {
    when(courseRepository.getByName("Rust")).thenReturn(null);

    CourseResponseDTO response = courseService.findByName("Rust");

    assertNull(response);
  }
}
