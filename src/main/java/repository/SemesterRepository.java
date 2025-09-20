package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Semester;

@ApplicationScoped
public class SemesterRepository implements PanacheRepository<Semester> {

  public Semester findByNumberAndCurriculum(Integer number, Long curriculumId) {
    return find("number = ?1 and curriculum.id = ?2", number, curriculumId).firstResult();
  }

  public boolean deleteById(Long id) {
    return PanacheRepository.super.deleteById(id);
  }
}
