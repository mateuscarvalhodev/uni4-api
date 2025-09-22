package resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;
import service.CurriculumService;

import java.util.List;

import dto.curriculum.CurriculumRequestDTO;
import dto.curriculum.CurriculumResponseDTO;

@Path("/curriculum")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class CurriculumResource {

  @Inject
  CurriculumService curriculumService;

  @POST

  public Response addCurriculum(@Valid CurriculumRequestDTO dto) {
    CurriculumResponseDTO created = curriculumService.addCurriculum(dto);
    if (created == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Invalid course ID").build();
    }
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @GET
  public List<CurriculumResponseDTO> getAllCurriculumn() {
    return curriculumService.listCurriculum();
  }

  @GET
  @Path("/{id}")
  public Response getCurriculumById(@PathParam("id") Long id) {
    CurriculumResponseDTO curriculum = curriculumService.findById(id);
    if (curriculum == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(curriculum).build();
  }

  @GET
  @Path("/year/{year}")
  public Response getCurriculumByYear(@PathParam("year") String year) {
    List<CurriculumResponseDTO> curriculum = curriculumService.findByAcademicYear(year);
    if (curriculum == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(curriculum).build();
  }

  @PUT
  @Path("/{id}")
  @RolesAllowed("coordenador")

  public Response editCurriculum(@PathParam("id") Long id, @Valid CurriculumRequestDTO dto) {
    CurriculumResponseDTO curriculum = curriculumService.editCurriculum(id, dto);
    if (curriculum == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(curriculum).build();
  }

  @DELETE
  @Path("/{id}")
  @RolesAllowed("coordenador")

  public Response deleteCurriculum(@PathParam("id") Long id) {
    boolean deleted = curriculumService.deleteCurriculum(id);
    if (!deleted) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.noContent().build();
  }
}
