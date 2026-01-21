create table article_photo(
id_photo int PRIMARY KEY AUTO_INCREMENT,
id_article INT NOT NULL,
url VARCHAR(255) NOT NULL,
alt VARCHAR(255),
position INT,
FOREIGN KEY (id_article) REFERENCES article(id_article)
);