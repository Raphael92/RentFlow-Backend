package kz.brdevelopment.test.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class RentQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RentRequest request;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Landlord landlord;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String comment;

    @Enumerated(EnumType.STRING)
    private QueueStatus status = QueueStatus.PENDING;
}
