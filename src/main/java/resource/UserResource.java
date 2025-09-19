package resource;

import java.net.URI;
import java.util.List;

import dto.user.UserEditRequest;
import dto.user.UserRequest;
import dto.user.UserResponse;
import io.quarkus.security.PermissionsAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import service.UserService;

@Path("/users")
@PermissionsAllowed({ "administrador" })
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public List<UserResponse> getAllUsers() {
      return userService.getAllUsers();
    }

    @Path("/create")
    @POST
    public Response createUser(UserRequest user) {
      UserResponse saved = userService.createUser(user);
      return Response.created(URI.create("/users/" + saved.id)).entity("Usuário criado com sucesso").build();
    }

    @Path("/{id}")
    @GET
    public UserResponse findById(Long id) {
      return userService.findById(id);
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(Long id) {
      userService.deleteById(id);
      return Response.noContent().build();
    }

    @Path("/{id}")
    @POST
    public UserResponse update(Long id, UserEditRequest user) {
      return userService.update(id, user);
    }
}
