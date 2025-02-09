package org.proha.seed;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Address;
import org.proha.model.entity.Student;
import org.proha.repository.StudentRepository;

import java.util.Arrays;
import java.util.List;

@Singleton
@Startup
public class StudentSeeder {

    @Inject
    private StudentRepository studentRepository;

    @PostConstruct
    @Transactional
    public void seed() {
        if (studentRepository.count() == 0) {
            Address address1 = new Address("Kathmandu", "Bagmati");
            Address address2 = new Address("Lalitpur", "Bagmati");
            Address address3 = new Address("Bhaktapur", "Bagmati");
            List<Student> students = Arrays.asList(
                    new Student("Prakash Dorsey", "prakash@study.xyzuni.edu", "MSC. ITM", address2),
                    new Student("Sudeep Page", "sudeep@study.xyzuni.edu", "MSC. ITM", address3),
                    new Student("Nabin Tucker", "nabin@study.xyzuni.edu", "MSC. ITM", address1),
                    new Student("Alisha Kroschev", "alisha@study.xyzuni.edu", "MSC. ITM", address1)
            );
            for (Student student : students) {
                studentRepository.save(student);
            }
        }
    }
}