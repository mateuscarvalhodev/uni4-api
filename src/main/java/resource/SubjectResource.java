package resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import dto.subject.SubjectRequestDTO;
import dto.subject.SubjectResponseDTO;
import service.SubjectService;

import java.util.List;

@Path("/subjects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SubjectResource {

  @Inject
  SubjectService subjectService;

  @POST
  public Response addSubject(@Valid SubjectRequestDTO dto) {
    SubjectResponseDTO created = subjectService.addSubject(dto);
    if (created == null) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Invalid curriculum ID").build();
    }
    return Response.status(Response.Status.CREATED).entity(created).build();
  }

  @GET
  public List<SubjectResponseDTO> getAllSubjects() {
    return subjectService.listSubjects();
  }

  @GET
  @Path("/{id}")
  public Response getSubjectById(@PathParam("id") Long id) {
    SubjectResponseDTO subject = subjectService.findById(id);
    if (subject == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(subject).build();
  }

  @GET
  @Path("/name/{name}")
  public Response getSubjectByName(@PathParam("name") String name) {
    SubjectResponseDTO subject = subjectService.findByName(name);
    if (subject == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(subject).build();
  }

  @PUT
  @Path("/{id}")
  public Response editSubject(@PathParam("id") Long id, @Valid SubjectRequestDTO dto) {
    SubjectResponseDTO subject = subjectService.editSubject(id, dto);
    if (subject == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(subject).build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteSubject(@PathParam("id") Long id) {
    boolean deleted = subjectService.deleteSubject(id);
    if (!deleted) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.noContent().build();
  }
}
