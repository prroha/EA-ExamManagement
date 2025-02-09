package org.proha.model.entity;

import jakarta.persistence.*;

import org.proha.model.entity.Subject;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "exam_title")
    private String examTitle;

    @Column(name = "exam_remarks")
    private String examRemarks;
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "exam_date")
    private LocalDate examDate;
    @Column(name = "max_marks")
    private Integer maxMarks;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Result> results;

    public Exam(UUID id) {
        this.id = id;
    }
    public Exam(UUID id, Subject subject, String examTitle, LocalDate examDate) {
        this.id = id;
        this.subject = subject;
        this.examTitle = examTitle;
        this.examDate = examDate;
    }

    public Exam(Subject subject, String examTitle, LocalDate examDate, int maxMarks) {
        this.subject = subject;
        this.examTitle = examTitle;
        this.examDate = examDate;
        this.maxMarks = maxMarks;
    }
    public Exam() {

    }

    @Override
    public String toString() {
        return String.format("Exam{id=%s, subject=%s, examTitle='%s', examDate=%s, maxMarks=%d, results=%s}", id, subject, examTitle, examDate, maxMarks, results);
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
