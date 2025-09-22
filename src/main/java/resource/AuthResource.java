package resource;

import dto.auth.UserCreateRequest;
import dto.auth.UserCreateResponse;
import dto.auth.UserLoginRequest;
import dto.auth.UserLoginResponse;
import dto.auth.UserLogoutRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;
import service.AuthService;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

  @Inject
  AuthService authService;

  @POST
  @Path("/login")
  public Response login(UserLoginRequest request) {
    UserLoginResponse tokens = authService.login(request);
    return Response.ok(tokens).build();
  }

  @POST
  @Path("/logout")
  public Response logout(UserLogoutRequest request) {
    authService.logout(request);
    return Response.noContent().build();
  }

  @POST
  @Path("/register")
  @RolesAllowed( "administrador")
  public Response createUser(UserCreateRequest request) {
    UserCreateResponse user = authService.createUser(request);
    return Response.status(Response.Status.CREATED).entity(user).build();
  }
}
