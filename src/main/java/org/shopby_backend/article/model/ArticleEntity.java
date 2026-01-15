package org.shopby_backend.article.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.brand.model.BrandEntity;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name="article")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_article")
    private Long idArticle;

    private String name;

    private String description;

    @Column(precision =  10, scale = 2)
    private BigDecimal price;

    private Long stock;

    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @ManyToOne
    @JoinColumn(name = "type_article_id_type_article")
    private TypeArticleEntity typeArticle;
}
