create table wishlist(
id_wishlist int PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
name VARCHAR(255) NOT NULL,
description VARCHAR(255),
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wishlist_user
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

create table wishlist_item(
id_wishlist int,
article_id INT NOT NULL,
added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id_wishlist,article_id),
    CONSTRAINT fk_wishlist_item_wishlist
        FOREIGN KEY (id_wishlist) REFERENCES wishlist(id_wishlist)
        ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_item_article
        FOREIGN KEY (article_id) REFERENCES article(id_article)
);
