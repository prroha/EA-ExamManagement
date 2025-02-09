package org.proha.seed;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.proha.model.entity.Subject;
import org.proha.repository.SubjectRepository;

import java.util.Arrays;
import java.util.List;

@Singleton
@Startup
public class SubjectSeeder {

    @Inject
    private SubjectRepository subjectRepository;

    @PostConstruct
    @Transactional
    public void seed() {
        if (subjectRepository.count() == 0) {
            List<Subject> subjects = Arrays.asList(
                    new Subject("OOSE", "Object Oriented System"),
                    new Subject("DM", "Data Management"),
                    new Subject("EA", "Enterprise Applications"),
                    new Subject("SQE", "Software Quality Engineering")
            );
            for (Subject subject : subjects) {
                subjectRepository.save(subject);
            }
        }
    }
}