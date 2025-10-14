package org.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.teacher.entity.StudentTeacher;

import java.util.List;

@Repository
public interface StudentTeacherRepository extends JpaRepository<StudentTeacher, Long> {

    @Query("SELECT st FROM StudentTeacher st " +
            "WHERE st.student.studentId = :studentId " +
            "AND st.teacher.teacherId = :teacherId")
    List<StudentTeacher> findByStudent_StudentIdAndTeacher_TeacherId(
            @Param("studentId") Long studentId,
            @Param("teacherId") Long teacherId);

    @Query("SELECT st FROM StudentTeacher st " +
            "WHERE st.student.studentId = :studentId ")
    List<StudentTeacher> findByStudent_StudentId(@Param("studentId") Long studentId);

    List<StudentTeacher> findByTeacher_TeacherId(Long teacherId);
}
