create table roles(
id int AUTO_INCREMENT PRIMARY KEY,
 libelle VARCHAR(255)
);

create table users(
id int AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL,
prenom VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
country VARCHAR(255) NOT NULL,
delivery_address VARCHAR(255),
billing_address VARCHAR(255),
enabled BOOLEAN,
role_id int,
CONSTRAINT fk_users_role FOREIGN KEY(role_id) references roles(id)
);
