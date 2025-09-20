package resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.SemesterService;
import dto.Semester.SemesterRequestDTO;
import dto.Semester.SemesterResponseDTO;

import java.util.List;

@Path("/semesters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SemesterResource {

  @Inject
  SemesterService semesterService;

  @POST
  public Response addSemester(@Valid SemesterRequestDTO dto) {
    dto.Semester.SemesterResponseDTO created = semesterService.addSemester(dto);
    if (created == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Invalid curriculum ID").build();
    }
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @GET
  public List<SemesterResponseDTO> getAllSemesters() {
    return semesterService.listSemesters();
  }

  @GET
  @Path("/{id}")
  public Response getSemesterById(@PathParam("id") Long id) {
    SemesterResponseDTO semester = semesterService.findById(id);
    if (semester == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(semester).build();
  }

  @GET
  @Path("/curriculum/{curriculumId}/number/{number}")
  public Response getSemesterByNumberAndCurriculum(@PathParam("number") Integer number,
      @PathParam("curriculumId") Long curriculumId) {
    SemesterResponseDTO semester = semesterService.findByNumberAndCurriculum(number, curriculumId);
    if (semester == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(semester).build();
  }

  @PUT
  @Path("/{id}")
  public Response editSemester(@PathParam("id") Long id, @Valid SemesterRequestDTO dto) {
    SemesterResponseDTO updated = semesterService.editSemester(id, dto);
    if (updated == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(updated).build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteSemester(@PathParam("id") Long id) {
    boolean deleted = semesterService.deleteSemester(id);
    if (!deleted) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.noContent().build();
  }
}
