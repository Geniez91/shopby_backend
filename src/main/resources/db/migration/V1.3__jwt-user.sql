create table jwt(
id int AUTO_INCREMENT PRIMARY KEY,
token VARCHAR(1024),
disabled BOOLEAN,
expired BOOLEAN,
user_id int,
CONSTRAINT fk_users_jwt FOREIGN KEY(user_id) references users(id)
)