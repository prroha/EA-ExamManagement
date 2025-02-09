package org.proha.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.proha.model.dto.ExamDTO;
import org.proha.model.entity.Exam;
import org.proha.model.entity.Subject;

import java.time.LocalDate;

@ApplicationScoped
public class ExamMapper {

    public ExamDTO toDTO(Exam exam) {
        ExamDTO examDTO = new ExamDTO();
        examDTO.setId(exam.getId());
        examDTO.setSubjectId(exam.getSubject().getId());
        examDTO.setSubjectName(exam.getSubject().getName());
        examDTO.setMaxMarks(exam.getMaxMarks());
        examDTO.setExamDate(exam.getExamDate());
        examDTO.setExamTitle(exam.getExamTitle());
        return examDTO;
    }

    public Exam toEntity(ExamDTO examDTO) {
        Exam exam = new Exam();
        exam.setSubject(new Subject(examDTO.getSubjectId()));
        exam.setMaxMarks(examDTO.getMaxMarks());
        exam.setExamTitle(examDTO.getExamTitle());
        exam.setExamDate(examDTO.getExamDate() != null ? examDTO.getExamDate() : LocalDate.now());
        return exam;
    }
}
