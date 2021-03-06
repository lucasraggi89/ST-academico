package br.ufal.ic.academico.models.person.student;

import br.ufal.ic.academico.models.course.Course;
import br.ufal.ic.academico.models.discipline.Discipline;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import br.ufal.ic.academico.models.person.Person;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Student extends Person {
    @Setter
    Integer credits;

    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    Course course;

    @ElementCollection
    List<String> completedDisciplines;

    public Student(String firstname, String lastName) {
        super(firstname, lastName, "STUDENT");
        this.credits = 0;
        this.completedDisciplines = new ArrayList<>();
    }

    public Student(StudentDTO entity) {
        super(entity.firstName, entity.lastName, "STUDENT");
        credits = 0;
        completedDisciplines = new ArrayList<>();
    }

    public void update(StudentDTO entity) {
        super.update(entity.firstName, entity.lastName, "STUDENT");
    }

    public boolean completeDiscipline(Discipline discipline) {
        if (discipline.removeStudent(this)) {
            this.credits += discipline.getCredits();
            return this.completedDisciplines.add(discipline.getCode());
        }
        return false;
    }
}
