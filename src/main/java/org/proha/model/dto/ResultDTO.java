package org.proha.model.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ResultDTO {

    private UUID id;
    private UUID studentId;
    private UUID examId;
    private StudentDTO student;
    private ExamDTO exam;
    private Integer marksObtained;
    private String grade;
    private LocalDate resultDate;
    private String remarks;
    private SubjectDTO subject;

    public StudentDTO getStudent() {
        return student;
    }

    public void setStudent(StudentDTO student) {
        this.student = student;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    public ResultDTO() {}

    public ResultDTO(UUID id, UUID studentId, UUID examId, Integer marksObtained, String grade, LocalDate resultDate, String remarks) {
        this.id = id;
        this.studentId = studentId;
        this.examId = examId;
        this.marksObtained = marksObtained;
        this.grade = grade;
        this.resultDate = resultDate;
        this.remarks = remarks;
    }
    public ResultDTO(UUID id, StudentDTO student, ExamDTO exam) {
        this.id = id;
        this.student = student;
        this.exam = exam;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getExamId() {
        return examId;
    }

    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    public Integer getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(Integer marksObtained) {
        this.marksObtained = marksObtained;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getResultDate() {
        return resultDate;
    }

    public void setResultDate(LocalDate resultDate) {
        this.resultDate = resultDate;
    }
    public SubjectDTO getSubject() {
        return subject;
    }
    public void setSubject(SubjectDTO subjectDTO) {
        this.subject = subjectDTO;
    }
}
