package kz.brdevelopment.test.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Landlord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;
    private String password;

    @Column(nullable = false)
    private int tokenVersion = 0;
}
