package org.teacher.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    private LocalDateTime paymentDate;
    private BigDecimal amount;            // сумма оплаты

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;         // CARD, CASH, TRANSFER

    private String transactionId;         // ID транзакции (если онлайн)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId);
    }
}
