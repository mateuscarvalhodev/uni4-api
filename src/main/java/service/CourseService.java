package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.Course;
import repository.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

import dto.course.CourseRequestDTO;
import dto.course.CourseResponseDTO;

@ApplicationScoped
public class CourseService {

  @Inject
  CourseRepository courseRepository;

  @Transactional
  public CourseResponseDTO addCourse(CourseRequestDTO dto) {
    Course course = new Course();
    course.setName(dto.name);
    course.setDescription(dto.description);
    course.setTotalHours(dto.totalHours);

    courseRepository.add(course);
    return CourseResponseDTO.fromEntity(course);
  }

  @Transactional
  public boolean deleteCourse(Long id) {
    return courseRepository.deleteById(id);
  }

  @Transactional
  public CourseResponseDTO editCourse(Long id, CourseRequestDTO dto) {
    Course course = courseRepository.find(id);
    if (course == null) {
      return null;
    }

    course.setName(dto.name);
    course.setDescription(dto.description);
    course.setTotalHours(dto.totalHours);

    return CourseResponseDTO.fromEntity(course);
  }

  public List<CourseResponseDTO> listCourses() {
    return courseRepository.getAll()
        .stream()
        .map(CourseResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }

  public CourseResponseDTO findById(Long id) {
    Course course = courseRepository.find(id);
    return course == null ? null : CourseResponseDTO.fromEntity(course);
  }

  public CourseResponseDTO findByName(String name) {
    Course course = courseRepository.getByName(name);
    return course == null ? null : CourseResponseDTO.fromEntity(course);
  }
}
