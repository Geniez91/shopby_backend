package org.shopby_backend.articlePhoto.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.article.model.ArticleEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "article_photo")
public class ArticlePhotoEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_photo")
   private Long idPhoto;

   @ManyToOne()
   @JoinColumn(name = "id_article")
   private ArticleEntity article;

   private String url;

   private String alt;

   private Integer position;

}
