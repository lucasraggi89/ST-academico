package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.course.CourseDTO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import br.ufal.ic.academico.models.secretary.Secretary;
import br.ufal.ic.academico.models.secretary.SecretaryDAO;
import br.ufal.ic.academico.models.secretary.SecretaryDTO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("secretary")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class SecretaryResources {
    private final DepartmentDAO departmentDAO;
    private final SecretaryDAO secretaryDAO;
    private final CourseDAO courseDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll secretaries");

        return Response.ok(secretaryDAO.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response get(@PathParam("id") Long id) {
        log.info("get secretary: id={}", id);

        return Response.ok(new SecretaryDTO(secretaryDAO.get(id))).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, SecretaryDTO entity) {
        log.info("update secretary: id={}", id);

        Secretary s = secretaryDAO.get(id);
        s.update(entity);
        return Response.ok(secretaryDAO.persist(s)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response delete(@PathParam("id") Long id) {
        log.info("delete secretary: id={}", id);

        Secretary s = secretaryDAO.get(id);
        Department d = secretaryDAO.getDepartment(s);

        if (s.getType().equals("GRADUATION")) {
            d.setGraduation(null);
        } else {
            d.setPostGraduation(null);
        }
        departmentDAO.persist(d);

        secretaryDAO.delete(s);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/course")
    @UnitOfWork
    public Response getAllCoursesFromSecretary(@PathParam("id") Long id) {
        log.info("getAll courses from secretary {}", id);

        Secretary s = secretaryDAO.get(id);
        return Response.ok(s.getCourses()).build();
    }

    @POST
    @Path("/{id}/course")
    @UnitOfWork
    @Consumes("application/json")
    public Response createCourse(@PathParam("id") Long id, CourseDTO entity) {
        log.info("create course on secretary {}", id);

        Secretary s = secretaryDAO.get(id);
        Course c = s.addCourse(entity);
        if (c != null) {
            courseDAO.persist(c);
        }
        return Response.ok(secretaryDAO.persist(s)).build();
    }
}
