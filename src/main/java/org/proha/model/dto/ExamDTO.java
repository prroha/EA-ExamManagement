package org.proha.model.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ExamDTO {

    private UUID id;
    private UUID subjectId;
    private String examTitle;
    private String examRemarks;
    private String subjectName;
    private LocalDate examDate;
    private Integer maxMarks;

    public ExamDTO() {}

    public ExamDTO(UUID id, UUID subjectId, String examTitle, String subjectName, LocalDate examDate, Integer maxMarks, String examRemarks) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.examTitle = examTitle;
        this.examDate = examDate;
        this.maxMarks = maxMarks;
        this.examRemarks = examRemarks;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(UUID subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getExamTitle() {
        return examTitle;
    }
    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }
    public String getExamRemarks() {
        return examRemarks;
    }
    public void setExamRemarks(String examRemarks) {
        this.examRemarks = examRemarks;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public Integer getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-15s %-10d", examTitle, examDate, maxMarks);
    }
}