create table brand(
    id_brand int PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255) NOT NULL
);

create table type_article(
    id_type_article int PRIMARY KEY AUTO_INCREMENT,
    libelle VARCHAR(255) NOT NULL,
    parent_id INT DEFAULT NULL,
    FOREIGN KEY (parent_id) REFERENCES TYPE_ARTICLE(id_type_article)
);

CREATE TABLE article (
id_article int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
description VARCHAR(255),
price DECIMAL(10,2),
stock INT DEFAULT 0,
creation_date DATETIME DEFAULT CURRENT_TIMESTAMP,
id_brand INT,
id_type_article int,
FOREIGN KEY (id_brand) REFERENCES BRAND(id_brand),
FOREIGN KEY (id_type_article) REFERENCES TYPE_ARTICLE(id_type_article)
);