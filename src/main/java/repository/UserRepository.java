package repository;

import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import model.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
  
  public User findByEmail(String email) {
    return find("email", email).firstResult();
  }

  public List<User> listAllOrderedByName() {
    return list("ORDER BY name");
  }

  public void createUser(User user) {
    persist(user);
  }

}
