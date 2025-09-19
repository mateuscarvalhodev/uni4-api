package service;

import java.util.List;

import dto.user.UserEditRequest;
import dto.user.UserRequest;
import dto.user.UserResponse;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import model.User;
import repository.UserRepository;

@ApplicationScoped
public class UserService {
  @Inject
  UserRepository userRepository;

  public List<UserResponse> getAllUsers() {
    return userRepository.listAllOrderedByName()
        .stream()
        .map(u -> new UserResponse(u.id, u.getName(), u.getEmail(), u.getRole()))
        .toList();
  }

  public UserResponse findById(Long id) {
    User u = userRepository.findById(id);
    if(u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    return new UserResponse(u.id, u.getName(), u.getEmail(), u.getRole());
  }

  public UserResponse findByEmail(String email) {
    User u = userRepository.findByEmail(email);
    if (u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    return new UserResponse(u.id, u.getName(), u.getEmail(), u.getRole());
  }

  @Transactional
  public UserResponse createUser(UserRequest user) {

    if (findByEmail(user.email) != null) {
      throw new WebApplicationException("Já existe um usuário com esse email", Response.Status.CONFLICT);
    }
    String hash = BcryptUtil.bcryptHash(user.password);

    User userEntity = new User(user.name, user.email, hash, user.role);

    userRepository.createUser(userEntity);
    return new UserResponse(userEntity.id, userEntity.getName(), userEntity.getEmail(), userEntity.getRole());
  }

  @Transactional
  public void deleteById(Long id) {
    User u = userRepository.findById(id);
    if (u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    userRepository.delete(u);
  }

  @Transactional
  public UserResponse update(Long id, UserEditRequest user) {
    User u = userRepository.findById(id);
    if (u == null) {
      throw new WebApplicationException("Usuário não encontrado", Response.Status.NOT_FOUND);
    }
    u.setName(user.name);
    u.setEmail(user.email);
    u.setRole(user.role);
    return new UserResponse(u.id, u.getName(), u.getEmail(), u.getRole());
  }
}
