package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Curriculum;

@ApplicationScoped
public class CurriculumRepository implements PanacheRepository<Curriculum> {

  public void add(Curriculum curriculum) {
    persist(curriculum);
  }

  public Curriculum findByAcademicYear(String year) {
    return find("academicYear", year).firstResult();
  }

  public boolean delete(Long id) {
    return deleteById(id);
  }

  public Curriculum find(Long id) {
    return findById(id);
  }
}
