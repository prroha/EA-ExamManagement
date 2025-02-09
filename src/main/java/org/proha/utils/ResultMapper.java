package org.proha.utils;

import jakarta.enterprise.context.ApplicationScoped;
import org.proha.model.dto.ResultDTO;
import org.proha.model.entity.Exam;
import org.proha.model.entity.Result;
import org.proha.model.entity.Student;

import java.time.LocalDate;

@ApplicationScoped
public class ResultMapper {

    public ResultDTO toDTO(Result result) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setId(result.getId());
        resultDTO.setStudentId(result.getStudent().getId());
        resultDTO.setExamId(result.getExam().getId());
        resultDTO.setResultDate(result.getResultDate());
        resultDTO.setGrade(result.getGrade());
        resultDTO.setMarksObtained(result.getMarksObtained());
        resultDTO.setRemarks(result.getRemarks());
        return resultDTO;
    }

    public Result toEntity(ResultDTO resultDTO) {
        Result result = new Result();
        result.setStudent(new Student(resultDTO.getStudentId()));
        result.setExam(new Exam(resultDTO.getExamId()));
        result.setGrade(resultDTO.getGrade());
        result.setMarksObtained(resultDTO.getMarksObtained());
        result.setRemarks(resultDTO.getRemarks());
        result.setResultDate(resultDTO.getResultDate() != null ? resultDTO.getResultDate() : LocalDate.now());
        return result;
    }
}
