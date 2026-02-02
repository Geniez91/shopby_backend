create table status(
id_status int PRIMARY KEY AUTO_INCREMENT,
libelle VARCHAR(255) NOT NULL
);

create table orders (
id_order int PRIMARY KEY AUTO_INCREMENT,
date_order DATETIME DEFAULT CURRENT_TIMESTAMP,
delivery_address VARCHAR(255) NOT NULL,
total_price INTEGER NOT NULL,
date_delivery DATETIME DEFAULT CURRENT_TIMESTAMP,
id_status int,
id_user int,
CONSTRAINT fk_order_status
        FOREIGN KEY (id_status) REFERENCES status(id_status),
CONSTRAINT fk_order_user
        FOREIGN KEY (id_user) REFERENCES users(id)
);

create table order_item (
id_order int,
id_article int,
quantity int,
unit_price int,
PRIMARY KEY (id_order,id_article),
CONSTRAINT fk_order_item_order
        FOREIGN KEY (id_order) REFERENCES orders(id_order)
,
CONSTRAINT fk_order_item_article
        FOREIGN KEY (id_article) REFERENCES article(id_article)
);
