package org.shopby_backend.wishlist.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.data.annotation.Id;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "wishlist")
public class WishlistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_wishlist")
    private Long idWishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    private String name;
    private String description;

    @Column(name = "created_at")
    private Date creationDate;

    @Column(name = "updated_at")
    private Date updatedDate;
}
