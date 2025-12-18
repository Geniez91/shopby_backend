package org.shopby_backend.users.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="validation")
public class ValidationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant creationDate;
    private Instant expirationDate;
    private String code;

    @OneToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH})
    private UsersEntity user;
}
