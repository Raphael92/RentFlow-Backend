package kz.brdevelopment.test.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentRequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private RentRequest rentRequest;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String action;

    private RequestHistoryAction historyAction;
}
