package resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.CourseService;

import java.util.List;

import dto.course.CourseRequestDTO;
import dto.course.CourseResponseDTO;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseResource {

  @Inject
  CourseService courseService;

  @POST
  public Response addCourse(@Valid CourseRequestDTO dto) {
    CourseResponseDTO created = courseService.addCourse(dto);
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @GET
  public List<CourseResponseDTO> getAllCourses() {
    return courseService.listCourses();
  }

  @GET
  @Path("/{id}")
  public Response getCourseById(@PathParam("id") Long id) {
    CourseResponseDTO course = courseService.findById(id);
    if (course == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(course).build();
  }

  @GET
  @Path("/name/{name}")
  public Response getCourseByName(@PathParam("name") String name) {
    CourseResponseDTO course = courseService.findByName(name);
    if (course == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(course).build();
  }

  @PUT
  @Path("/{id}")
  public Response editCourse(@PathParam("id") Long id, @Valid CourseRequestDTO dto) {
    CourseResponseDTO course = courseService.editCourse(id, dto);
    if (course == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(course).build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteCourse(@PathParam("id") Long id) {
    boolean deleted = courseService.deleteCourse(id);
    if (!deleted) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.noContent().build();
  }
}
