package org.shopby_backend.typeArticle.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="type_article")
public class TypeArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_article")
    private Long idTypeArticle;

    private String libelle;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TypeArticleEntity parent;
}
