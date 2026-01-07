package org.shopby_backend.jwt.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.users.model.UsersEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "jwt")
public class JwtEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private boolean disabled;

    private boolean expired;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE})
    private UsersEntity user;
}
