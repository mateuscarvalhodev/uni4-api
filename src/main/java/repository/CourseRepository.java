package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Course;

import java.util.List;

@ApplicationScoped
public class CourseRepository implements PanacheRepository<Course> {

  public void add(Course course) {
    persist(course);
  }

  public boolean deleteById(Long id) {
    return delete("id", id) > 0;
  }

  public List<Course> getAll() {
    return listAll();
  }

  public Course getByName(String name) {
    return find("name", name).firstResult();
  }

  public Course find(Long id) {
    return find("id", id).firstResult();
  }

}
