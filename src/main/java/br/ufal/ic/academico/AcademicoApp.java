package br.ufal.ic.academico;

import br.ufal.ic.academico.exemplos.MyResource;
import br.ufal.ic.academico.exemplos.Person;
import br.ufal.ic.academico.exemplos.PersonDAO;
import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.course.CourseDAO;
import br.ufal.ic.academico.models.department.Department;
import br.ufal.ic.academico.models.department.DepartmentDAO;
import br.ufal.ic.academico.models.discipline.Discipline;
import br.ufal.ic.academico.models.discipline.DisciplineDAO;
import br.ufal.ic.academico.models.person.student.Student;
import br.ufal.ic.academico.models.person.student.StudentDAO;
import br.ufal.ic.academico.models.person.teacher.Teacher;
import br.ufal.ic.academico.models.person.teacher.TeacherDAO;
import br.ufal.ic.academico.models.secretary.Secretary;
import br.ufal.ic.academico.models.secretary.SecretaryDAO;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Slf4j
public class AcademicoApp extends Application<ConfigApp> {

    public static void main(String[] args) throws Exception {
        new AcademicoApp().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ConfigApp> bootstrap) {
        log.info("initialize");
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ConfigApp config, Environment environment) {
        final PersonDAO dao = new PersonDAO(hibernate.getSessionFactory());
        final StudentDAO studentDAO = new StudentDAO(hibernate.getSessionFactory());
        final TeacherDAO teacherDAO = new TeacherDAO(hibernate.getSessionFactory());
        final DepartmentDAO departmentDAO = new DepartmentDAO(hibernate.getSessionFactory());
        final SecretaryDAO secretaryDAO = new SecretaryDAO(hibernate.getSessionFactory());
        final CourseDAO courseDAO = new CourseDAO(hibernate.getSessionFactory());
        final DisciplineDAO disciplineDAO = new DisciplineDAO(hibernate.getSessionFactory());

        final MyResource resource = new MyResource(dao);
        final EnrollmentResources enrollmentResources = new EnrollmentResources(studentDAO, teacherDAO, courseDAO, disciplineDAO);
        final DepartmentResources departmentResources = new DepartmentResources(departmentDAO, secretaryDAO);
        final SecretaryResources secretaryResources = new SecretaryResources(departmentDAO, secretaryDAO, courseDAO);
        final CourseResources courseResources = new CourseResources(secretaryDAO, courseDAO, disciplineDAO);
        final DisciplineResources disciplineResources = new DisciplineResources(courseDAO, disciplineDAO);

        environment.jersey().register(resource);
        environment.jersey().register(enrollmentResources);
        environment.jersey().register(departmentResources);
        environment.jersey().register(secretaryResources);
        environment.jersey().register(courseResources);
        environment.jersey().register(disciplineResources);
    }

    private final HibernateBundle<ConfigApp> hibernate
            = new HibernateBundle<ConfigApp>(Person.class,
            Student.class, Teacher.class, Department.class, Secretary.class, Course.class, Discipline.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(ConfigApp configuration) {
            return configuration.getDatabase();
        }
    };
}
