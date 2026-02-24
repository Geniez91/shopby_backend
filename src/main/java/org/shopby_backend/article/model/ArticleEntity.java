package org.shopby_backend.article.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.shopby_backend.articlePhoto.model.ArticlePhotoEntity;
import org.shopby_backend.brand.model.BrandEntity;
import org.shopby_backend.typeArticle.model.TypeArticleEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_brand")
    private BrandEntity brand;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_type_article")
    private TypeArticleEntity typeArticle;

    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY)
    private List<ArticlePhotoEntity> photos;

    @Version
    private Long version;
}
