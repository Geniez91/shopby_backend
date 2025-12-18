create table validation(
    id int PRIMARY KEY AUTO_INCREMENT,
    creation_date DATETIME,
    expiration_date DATETIME,
    code VARCHAR(255),
    user_id int,
    CONSTRAINT fk_users_validation FOREIGN KEY(user_id) references users(id)
)