package org.teacher.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.teacher.common.dto.PageRequestDto;
import org.teacher.dto.LessonDto;
import org.teacher.entity.Lesson;
import org.teacher.entity.LessonStatus;
import org.teacher.entity.Student;
import org.teacher.entity.Teacher;
import org.teacher.mapper.LessonMapper;
import org.teacher.repository.LessonRepository;
import org.teacher.service.LessonService;
import org.teacher.service.StudentTeacherService;
import org.teacher.service.impl.LessonServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link LessonService}.
 *
 * <p>The goal of this test class is to verify the behavior of LessonService methods:
 * <ul>
 *   <li>Create a new Lesson and return its DTO</li>
 *   <li>Retrieve a Lesson by ID (present / not present)</li>
 *   <li>Retrieve all Lessons</li>
 *   <li>Update an existing Lesson</li>
 *   <li>Delete a Lesson</li>
 * </ul>
 *
 * <p>This test uses Mockito to mock dependencies (LessonRepository, LessonMapper)
 * and AssertJ for fluent assertions.
 */
@SpringBootTest
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentTeacherService studentTeacherService;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Lesson lesson;
    private LessonDto lessonDto;

    @BeforeEach
    void setUp() {
        lesson = Lesson.builder()
                .lessonId(1L)
                .student(Student.builder()
                        .studentId(10L)
                        .build())
                .teacher(Teacher.builder()
                        .teacherId(20L)
                        .build())
                .status(LessonStatus.COMPLETED)
                .build();

        lessonDto = new LessonDto(
                null,
                10L,
                20L,
                null,
                null,
                null,
                LessonStatus.COMPLETED,
                null,
                null
        );
    }

    @Test
    void addLesson_ShouldSaveAndReturnDto() {
        when(lessonMapper.toEntity(lessonDto)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDto);

        LessonDto result = lessonService.addLesson(lessonDto);

        assertThat(result).isEqualTo(lessonDto);
        verify(lessonRepository).save(lesson);
    }

    @Test
    void getById_ShouldReturnLesson_WhenExists() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDto);

        Optional<LessonDto> result = lessonService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(lessonDto);
    }

    @Test
    void getById_ShouldReturnEmpty_WhenNotExists() {
        when(lessonRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<LessonDto> result = lessonService.getById(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void getAll_ShouldReturnListOfLessons() {
        when(lessonRepository.findAll()).thenReturn(List.of(lesson));
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDto);

        PageRequestDto pageRequest = new PageRequestDto(1,50,null);

        Page<LessonDto> result = lessonService.getAll(pageRequest.toPageable());

        assertThat(result).hasSize(1).contains(lessonDto);
    }

    @Test
    void update_ShouldUpdateAndReturnDto() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toDto(lesson)).thenReturn(lessonDto);

        LessonDto result = lessonService.update(1L, lessonDto);

        assertThat(result).isEqualTo(lessonDto);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        lessonService.delete(1L);

        verify(lessonRepository).deleteById(1L);
    }
}