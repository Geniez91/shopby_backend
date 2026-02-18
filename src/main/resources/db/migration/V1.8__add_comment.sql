create table comment (
id_comment int PRIMARY KEY AUTO_INCREMENT,
id_article int NOT NULL,
id_user int NOT NULL,
date_comment DATETIME DEFAULT CURRENT_TIMESTAMP,
description VARCHAR(252),
note int NOT NULL,
CONSTRAINT fk_comment_article
        FOREIGN KEY (id_article) REFERENCES article(id_article),
CONSTRAINT fk_comment_user
        FOREIGN KEY (id_user) REFERENCES users(id)
);

create table comment_like(
id_comment int,
id_user int,
PRIMARY KEY (id_comment,id_user),
CONSTRAINT fk_comment_user_user
        FOREIGN KEY (id_user) REFERENCES users(id),
CONSTRAINT fk_comment_user_comment
        FOREIGN KEY (id_comment) REFERENCES comment(id_comment)
);
