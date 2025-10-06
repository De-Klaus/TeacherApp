package org.teacher.unit.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.teacher.entity.*;
import org.teacher.repository.PaymentRepository;
import org.teacher.repository.StudentRepository;
import org.teacher.service.PaymentService;

/**
 * Unit tests for {@link PaymentService}.
 *

 */
@SpringBootTest
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Student student;

    /*@BeforeEach
    void setUp() {
        student = Student.builder()
                .studentId(1L)
                .timeZone("Europe/Moscow")
                .grade(5)
                .build();
    }

    @Test
    void createPayment_shouldSaveAndReturnPayment() {
        // given
        Long studentId = student.getStudentId();
        BigDecimal amount = new BigDecimal("1000");
        String description = "First payment";

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Payment savedPayment = Payment.builder()
                .paymentId(1L)
                .student(student)
                .amount(amount)
                .paymentDate(LocalDateTime.now())
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // when
        Payment result = paymentService.addPayment(studentId, amount);

        // then
        assertThat(result.getAmount()).isEqualByComparingTo("1000");
        assertThat(result.getStudent()).isEqualTo(student);

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_shouldThrowException_whenStudentNotFound() {
        // given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> paymentService.addPayment(studentId, BigDecimal.TEN, "fail"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student not found");

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void getPaymentsByStudent_shouldReturnList() {
        // given
        UUID studentId = student.getStudentId();
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Payment p1 = Payment.builder()
                .paymentId(UUID.randomUUID())
                .student(student)
                .amount(new BigDecimal("500"))
                .description("Lesson 1")
                .paymentDate(LocalDateTime.now())
                .build();

        Payment p2 = Payment.builder()
                .paymentId(UUID.randomUUID())
                .student(student)
                .amount(new BigDecimal("700"))
                .description("Lesson 2")
                .paymentDate(LocalDateTime.now())
                .build();

        when(paymentRepository.findByStudent(student)).thenReturn(List.of(p1, p2));

        // when
        List<Payment> result = paymentService.getPaymentsByStudent(studentId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Payment::getAmount)
                .containsExactlyInAnyOrder(new BigDecimal("500"), new BigDecimal("700"));
    }

    @Test
    void getPaymentsByStudent_shouldThrowException_whenStudentNotFound() {
        // given
        UUID studentId = UUID.randomUUID();
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> paymentService.getPaymentsByStudent(studentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Student not found");

        verify(paymentRepository, never()).findByStudent(any());
    }*/
}