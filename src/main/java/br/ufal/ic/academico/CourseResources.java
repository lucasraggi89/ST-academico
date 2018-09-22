package br.ufal.ic.academico;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.course.CourseDTO;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDAO;
import br.ufal.ic.academico.models.discipline.DisciplineDTO;
import br.ufal.ic.academico.models.secretary.Secretary;
import br.ufal.ic.academico.models.secretary.SecretaryDAO;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("course")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class CourseResources {
    private final SecretaryDAO secretaryDAO;
    private final CourseDAO courseDAO;
    private final DisciplineDAO disciplineDAO;

    @GET
    @UnitOfWork
    public Response getAll() {
        log.info("getAll courses");

        return Response.ok(courseDAO.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response get(@PathParam("id") Long id) {
        log.info("get course: id={}", id);

        return Response.ok(courseDAO.get(id)).build();
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes("application/json")
    public Response update(@PathParam("id") Long id, CourseDTO entity) {
        log.info("update course: id={}", id);

        Course c = courseDAO.get(id);
        c.update(entity);
        return Response.ok(courseDAO.persist(c)).build();
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteCourse(@PathParam("id") Long id) {
        log.info("delete course {}", id);

        Course c = courseDAO.get(id);
        Secretary s = courseDAO.getSecretary(c);
        s.deleteCourse(c);

        secretaryDAO.persist(s);
        courseDAO.delete(c);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/discipline")
    @UnitOfWork
    public Response getAllDisciplinesFromCourse(@PathParam("id") Long id) {
        log.info("getAll disciplines from course {}", id);

        Course c = courseDAO.get(id);
        return Response.ok(c.getDisciplines()).build();
    }

    @POST
    @Path("/{id}/discipline")
    @UnitOfWork
    @Consumes("application/json")
    public Response createDiscipline(@PathParam("id") Long id, DisciplineDTO entity) {
        log.info("create discipline in course {}", id);

        Discipline d = new Discipline(entity);
        disciplineDAO.persist(d);

        Course c = courseDAO.get(id);
        c.addDiscipline(d);

        return Response.ok(courseDAO.persist(c)).build();
    }
}
