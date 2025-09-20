package repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.Subject;

@ApplicationScoped
public class SubjectRepository implements PanacheRepository<Subject> {
  public Subject findByName(String name) {
    return find("name", name).firstResult();
  }
}