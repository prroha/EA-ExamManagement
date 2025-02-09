package org.proha.seed;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.DependsOn;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Exam;
import org.proha.model.entity.Subject;
import org.proha.repository.ExamRepository;
import org.proha.repository.SubjectRepository;
import org.proha.service.SubjectService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
@Startup
@DependsOn("SubjectSeeder")
public class ExamSeeder {

    @Inject
    private ExamRepository examRepository;
    @Inject
    private SubjectRepository subjectRepository;

    @PostConstruct
    @Transactional
    public void seed() {
        if (examRepository.count() == 0) {
            Optional<Subject> subject1 = subjectRepository.findByName("OOSE");
            Optional<Subject> subject2 = subjectRepository.findByName("DM");
            Optional<Subject> subject3 = subjectRepository.findByName("EA");

            if (subject1.isEmpty() || subject2.isEmpty() || subject3.isEmpty()) {
                return;
            }

            List<Exam> exams = Arrays.asList(
                    new Exam(subject1.get(),"Second Semester End",LocalDate.of(2025, 12, 1), 100),
                    new Exam(subject2.get(),"Second Semester End",LocalDate.of(2025, 12, 2), 100),
                    new Exam(subject3.get(),"Second Semester End",LocalDate.of(2025, 12, 3), 100)
            );

            for (Exam exam : exams) {
                examRepository.save(exam);
            }
        }
    }
}