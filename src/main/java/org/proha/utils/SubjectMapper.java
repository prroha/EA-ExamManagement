package org.proha.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.proha.model.dto.SubjectDTO;
import org.proha.model.entity.Subject;

@ApplicationScoped
public class SubjectMapper {

    public SubjectDTO toDTO(Subject course) {
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(course.getId());
        subjectDTO.setName(course.getName());
        subjectDTO.setDescription(course.getDescription());
        return subjectDTO;
    }

    public Subject toEntity(SubjectDTO subjectDTO) {
        Subject subject = new Subject();
        subject.setId(subjectDTO.getId());
        subject.setName(subjectDTO.getName());
        subject.setDescription(subjectDTO.getDescription());
        return subject;
    }
}
