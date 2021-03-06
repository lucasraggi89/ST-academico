package br.ufal.ic.academico.models.person.teacher;

import br.ufal.ic.academico.models.person.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@RequiredArgsConstructor
public class Teacher extends Person {
    public Teacher(TeacherDTO entity) {
        super(entity.firstName, entity.lastName, "TEACHER");
    }

    public Teacher(String firstName, String lastName) {
        super(firstName, lastName, "TEACHER");
    }

    public void update(TeacherDTO entity) {
        super.update(entity.firstName, entity.lastName, "TEACHER");
    }
}
